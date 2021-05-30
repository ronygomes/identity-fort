package me.ronygomes.identity_fort.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private MailSender mailSender;

    @Autowired
    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(SimpleMailMessage message) {
        mailSender.send(message);
    }
}
