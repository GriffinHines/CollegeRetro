package com.retro.collegeretro.Service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * A helper class for sending emails to one or multiple recipients.
 * This class uses AsyncEmailSender to send emails asynchronously.
 * The class will apply a subject prefix to all subjects.
 */
@Component
public class MyEmailSender {

    private final AsyncEmailSender asyncEmailSender;
    private String subject = "CollegeRetro";

    public MyEmailSender(AsyncEmailSender asyncEmailSender) {
        this.asyncEmailSender = asyncEmailSender;
    }

    /**
     * Sets the subject and adds a subject header.
     * @param subject the body of the subject.
     */
    public void setSubject(String subject) {
        this.subject = "CollegeRetro: " + subject;
    }

    /**
     * Sends an email to a single recipient asynchronously.
     * @param email the address of the recipient.
     * @param messageString the message content.
     */
    public void sendTo(String email, String messageString) {
        asyncEmailSender.sendEmails(
                Collections.singletonList(email),
                "gelber.programming@gmail.com",
                subject,
                messageString);
    }

    /**
     * Sends an email to a list of recipients asynchronously.
     * @param emails list of the email addresses of the recipients.
     * @param messageString the message content.
     */
    public void sendTo(List<String> emails, String messageString) {
        asyncEmailSender.sendEmails(
                emails,
                "gelber.programming@gmail.com",
                subject,
                messageString);
    }

}