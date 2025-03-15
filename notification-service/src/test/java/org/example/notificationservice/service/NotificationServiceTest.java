package org.example.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private final JavaMailSender javaMailSender = mock(JavaMailSender.class);
    private final NotificationService notificationService = new NotificationService(javaMailSender);

    @Test
    void testSendAuthNotification() {
        notificationService.sendAuthNotification("test@example.com", "Test Subject", "Test Message");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
