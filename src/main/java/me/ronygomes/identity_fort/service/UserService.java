package me.ronygomes.identity_fort.service;

import me.ronygomes.identity_fort.entity.User;
import me.ronygomes.identity_fort.entity.VerificationToken;
import me.ronygomes.identity_fort.event.EmailEvent;
import me.ronygomes.identity_fort.repository.UserRepository;
import me.ronygomes.identity_fort.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static me.ronygomes.identity_fort.entity.VerificationToken.TokenType.EMAIL_CONFIRMATION;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       VerificationTokenRepository verificationTokenRepository,
                       ApplicationEventPublisher eventPublisher,
                       PasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.eventPublisher = eventPublisher;
        this.encoder = encoder;
    }


    @Transactional
    public void registerUser(User user, String verificationToken) {
        user.setRegistrationDate(new Date());
        user.setHashedPassword(encoder.encode(user.getRawPassword()));

        userRepository.save(user);
        verificationTokenRepository.save(new VerificationToken(verificationToken, user, EMAIL_CONFIRMATION));

        eventPublisher.publishEvent(new EmailEvent(user, verificationToken));
    }
}
