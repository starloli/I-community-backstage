package com.example.demo2.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo2.entity.Reservation;
import com.example.demo2.enums.ReservationStatus;
import com.example.demo2.repository.ReservationDao;

import lombok.RequiredArgsConstructor;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class NotificationService {

        private final ReservationDao reservationRepository;
        private final JavaMailSender mailSender;

        @Scheduled(cron = "0 00 20 * * *", zone = "Asia/Taipei")
        public void scheduled() {

                LocalDate tomorrow = LocalDate.now().plusDays(1);

                List<ReminderDto> reminders = reservationRepository
                                .findAllWithUserByDateAndStatus(ReservationStatus.CONFIRMED, tomorrow)
                                .stream().collect(Collectors.groupingBy(Reservation::getUser))
                                .entrySet().stream()
                                .map(entry -> new ReminderDto(entry.getKey().getEmail(),
                                                entry.getValue().stream()
                                                                .map(r -> new ReservationDto(r.getFacility().getName(),
                                                                                r.getStartTime()))
                                                                .toList()))
                                .toList();
                reminders.forEach(dto -> {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setTo(dto.userEmail);
                        message.setSubject("【明日預約通知】您明天有" + dto.reservations.size() + "個預約");
                        String content = "";
                        if (dto.reservations.size() >= 1) {
                                for (ReservationDto r : dto.reservations) {
                                        content += r.facilityName + " " + r.startTime + "\n";
                                }
                        }
                        message.setText(String.format(content));
                        message.setText(String.format("親愛的使用者您好"));
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
                        LocalTime startTime) {
        }
}
