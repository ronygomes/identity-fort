package me.ronygomes.identity_fort.controller;

import me.ronygomes.identity_fort.entity.EmailVerificationToken;
import me.ronygomes.identity_fort.entity.User;
import me.ronygomes.identity_fort.repository.EmailVerificationTokenRepository;
import me.ronygomes.identity_fort.repository.UserRepository;
import me.ronygomes.identity_fort.validator.UserRegistrationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

import static me.ronygomes.identity_fort.util.WebHelper.putErrorRedirectMessage;
import static me.ronygomes.identity_fort.util.WebHelper.putSuccessRedirectMessage;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private static final String VIEW_NAME = "registration";
    private static final String COMMAND_NAME = "user";

    private UserRepository userRepository;
    private EmailVerificationTokenRepository emailVerificationTokenRepository;
    private PasswordEncoder encoder;
    private UserRegistrationValidator userRegistrationValidator;

    @Autowired
    public RegistrationController(UserRepository userRepository,
                                  EmailVerificationTokenRepository emailVerificationTokenRepository,
                                  PasswordEncoder encoder,
                                  UserRegistrationValidator userRegistrationValidator) {

        this.userRepository = userRepository;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.encoder = encoder;
        this.userRegistrationValidator = userRegistrationValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("firstName", "lastName", "email", "rawPassword", "confirmPassword");
        binder.addValidators(userRegistrationValidator);
    }

    @GetMapping(value = "/register")
    public String showRegistrationPage(ModelMap map) {
        map.put(COMMAND_NAME, new User());

        return VIEW_NAME;
    }

    @PostMapping(value = "/register")
    public String saveRegistrationPage(@Valid @ModelAttribute User user,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return VIEW_NAME;
        }

        user.setRegistrationDate(new Date());
        user.setHashedPassword(encoder.encode(user.getRawPassword()));

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        emailVerificationTokenRepository.save(new EmailVerificationToken(token, user));
        log.error("Generated token: {}", token);

        putSuccessRedirectMessage(redirectAttributes, "User Successfully Registered. Verify Email to Login");

        return "redirect:/login";
    }

    @GetMapping(value = "/confirmToken")
    public String saveRegistrationPage(@RequestParam String token, RedirectAttributes redirectAttributes) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.getOne(token);
        emailVerificationTokenRepository.delete(verificationToken);

        if (verificationToken.isExpired()) {
            putErrorRedirectMessage(redirectAttributes, "Invalid Verification Code");
            return "redirect:/login";
        }

        userRepository.findById(verificationToken.getOwner().getId())
                .ifPresent(u -> u.setEnabled(true));

        putSuccessRedirectMessage(redirectAttributes, "Email Verified Successfully");
        return "redirect:/login";
    }
}
