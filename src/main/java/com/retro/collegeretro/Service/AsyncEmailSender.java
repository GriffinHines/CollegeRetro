package com.retro.collegeretro.Service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AsyncEmailSender {
    @Async
    void sendEmails(List<String> recipients, String from, String subject, String message);
}
