package me.ronygomes.identity_fort.event;

import me.ronygomes.identity_fort.entity.User;
import org.springframework.context.ApplicationEvent;

public class EmailEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final User user;
    private final String verificationToken;

    public EmailEvent(User user, String verificationToken) {
        super(user);

        this.user = user;
        this.verificationToken = verificationToken;
    }

    public User getUser() {
        return user;
    }

    public String getVerificationToken() {
        return verificationToken;
    }
}
