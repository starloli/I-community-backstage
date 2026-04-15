package com.example.demo2.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.entity.Reservation;
import com.example.demo2.enums.ReservationStatus;
import com.example.demo2.repository.ReservationDao;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class NotificationService {

        private final ReservationDao reservationRepository;
        private final JavaMailSender mailSender;
        private final ThreadPoolTaskScheduler taskScheduler;
        private ScheduledFuture<?> scheduledTask;
        private ZoneId zoneId = ZoneId.of("Asia/Taipei");

        @PostConstruct
        public void init() {
                System.out.println("初始化通知服務，通知時間為20：00");
                updateSchedule(LocalTime.of(20, 00));
        }

        public String updateSchedule(@NonNull LocalTime timecron) {
                if (scheduledTask != null) {
                        scheduledTask.cancel(false);
                }
                String cron = "0 " + timecron.getMinute() + " " + timecron.getHour() + " * * *";
                System.out.println("更新通知時間為" + cron.split(" ")[2] + ":" + cron.split(" ")[1]);
                scheduledTask = taskScheduler.schedule(
                                () -> scheduled(),
                                new CronTrigger(cron, zoneId));
                return "已更新通知時間為" + cron.split(" ")[2] + ":" + cron.split(" ")[1];
        }

        @Transactional(readOnly = true)
        private void scheduled() {

                LocalDate tomorrow = LocalDate.now().plusDays(1);

                List<ReminderDto> reminders = reservationRepository
                                .findAllWithUserByDateAndStatus(ReservationStatus.CONFIRMED, tomorrow)
                                .stream().collect(Collectors.groupingBy(Reservation::getUser))
                                .entrySet().stream()
                                .map(entry -> new ReminderDto(entry.getKey().getEmail(),
                                                entry.getValue().stream()
                                                                .sorted(Comparator.comparing(Reservation::getStartTime))
                                                                .map(r -> new ReservationDto(r.getFacility().getName(),
                                                                                r.getDate(),
                                                                                r.getStartTime()))
                                                                .toList()))
                                .toList();
                reminders.forEach(dto -> {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setTo(dto.userEmail);
                        message.setSubject("【明日預約通知】您在明天有" + dto.reservations.size() + "個預約");
                        StringBuilder content = new StringBuilder();
                        for (ReservationDto r : dto.reservations) {
                                content.append(String.format("● %s：%s\n",
                                                r.facilityName,
                                                r.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))));
                        }
                        message.setText(String.format(
                                        "親愛的使用者您好，明天" + tomorrow.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                                                        + "您有以下預約：\n\n%s\n\n感謝您的使用!",
                                        content));
                        mailSender.send(message);
                        System.out.println("已發送給 " + dto.userEmail);
                });
        }

        @Async // 👈 關鍵：這會讓寄信在背景執行，不會讓前端轉圈圈太久
        public void sendReservationSuccess(String userEmail, String facilityName, LocalDateTime time) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(userEmail);
                message.setSubject("【預約成功通知】您已成功預約 " + facilityName);
                message.setText(String.format(
                                "親愛的使用者您好：\n\n您已成功預約「%s」。\n時間:%s\n\n感謝您的使用!",
                                facilityName, time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
                mailSender.send(message);
                System.out.println("已發送給 " + userEmail);
        }

        private record ReminderDto(
                        String userEmail,
                        List<ReservationDto> reservations) {
        }

        private record ReservationDto(
                        String facilityName,
                        LocalDate date,
                        LocalTime startTime) {
        }
}
