package com.retro.collegeretro.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The class which actually sends the emails.
 */
@Component
public class AsyncEmailSenderImpl implements AsyncEmailSender {

    private final JavaMailSender javaMailSender;

    public AsyncEmailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sendEmails(List<String> recipients, String from, String subject, String messageString) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(messageString);
        for (String email : recipients) {
            message.setTo(email);
            javaMailSender.send(message);
        }
    }
}
