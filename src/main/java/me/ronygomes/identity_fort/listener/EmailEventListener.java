package me.ronygomes.identity_fort.listener;

import me.ronygomes.identity_fort.event.EmailEvent;
import me.ronygomes.identity_fort.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailEventListener implements ApplicationListener<EmailEvent> {

    private static final Logger log = LoggerFactory.getLogger(EmailEventListener.class);

    private EmailService emailService;

    @Autowired
    public EmailEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(EmailEvent event) {
        log.error("EmailEventListener triggered with token: {}", event.getVerificationToken());

        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom("noreply@identity-fort.me");
        smm.setSubject("Verify Email");
        smm.setTo(event.getUser().getEmail());
        smm.setText("http://localhost:8080/confirmToken/" + event.getVerificationToken());

        emailService.sendMessage(smm);
    }
}
