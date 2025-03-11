package org.example.notificationservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthListener {

    private final NotificationService notificationService;

    public AuthListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener (topics = "auth_events", groupId = "notification_group")
    public void consume(String message) {
        log.info("Consumed message: {}", message);
        String[] parts = message.split(",");
        String to = parts[0];
        String subject = parts[1];
        String text = parts[2];
        notificationService.sendAuthNotification(to, subject, text);
    }

}