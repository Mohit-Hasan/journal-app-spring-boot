package com.mohithasan.journalapp.service;

import lombok.var;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class EmailServiceTests {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    public EmailServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMail_ShouldCallJavaMailSender() {
        String to = "test@domain.com";
        String subject = "Hello";
        String body = "This is a test";

        emailService.sendMail(to, subject, body);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendMail_ShouldSetMailPropertiesCorrectly() {
        String to = "user@domain.com";
        String subject = "Subject Line";
        String body = "Email body";

        var captor = org.mockito.ArgumentCaptor.forClass(SimpleMailMessage.class);
        emailService.sendMail(to, subject, body);

        verify(javaMailSender).send(captor.capture());
        SimpleMailMessage sentMail = captor.getValue();
        assert sentMail.getTo()[0].equals(to);
        assert sentMail.getSubject().equals(subject);
        assert sentMail.getText().equals(body);
    }
}
