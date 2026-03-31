package com.example.demo2.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async // 👈 關鍵：這會讓寄信在背景執行，不會讓前端轉圈圈太久
    public void sendReservationSuccess(String userEmail, String facilityName, String time) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("【預約成功通知】您已成功預約 " + facilityName);
        message.setText(String.format(
            "親愛的使用者您好：\n\n您已成功預約「%s」。\n時間:%s\n\n感謝您的使用!",
            facilityName, time
        ));

        mailSender.send(message);
    }
}
