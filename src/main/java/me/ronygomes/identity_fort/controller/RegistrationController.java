package me.ronygomes.identity_fort.controller;

import me.ronygomes.identity_fort.entity.User;
import me.ronygomes.identity_fort.entity.VerificationToken;
import me.ronygomes.identity_fort.repository.UserRepository;
import me.ronygomes.identity_fort.repository.VerificationTokenRepository;
import me.ronygomes.identity_fort.service.UserService;
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
import java.util.Objects;
import java.util.UUID;

import static me.ronygomes.identity_fort.entity.VerificationToken.TokenType.FORGET_PASSWORD;
import static me.ronygomes.identity_fort.util.WebHelper.putErrorRedirectMessage;
import static me.ronygomes.identity_fort.util.WebHelper.putSuccessRedirectMessage;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private static final String VIEW_NAME = "registration";
    private static final String RESET_PASSWORD_VIEW_NAME = "resetPassword";

    private static final String COMMAND_NAME = "user";

    private UserRepository userRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private PasswordEncoder encoder;
    private UserRegistrationValidator userRegistrationValidator;
    private UserService userService;

    @Autowired
    public RegistrationController(UserRepository userRepository,
                                  VerificationTokenRepository verificationTokenRepository,
                                  PasswordEncoder encoder,
                                  UserRegistrationValidator userRegistrationValidator,
                                  UserService userService) {

        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.encoder = encoder;
        this.userRegistrationValidator = userRegistrationValidator;
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("firstName", "lastName", "email", "rawPassword", "confirmPassword");
        binder.addValidators(userRegistrationValidator);
    }

    @GetMapping("/register")
    public String showRegistrationPage(ModelMap map) {
        map.put(COMMAND_NAME, new User());

        return VIEW_NAME;
    }

    @PostMapping("/register")
    public String saveRegistrationPage(@Valid @ModelAttribute User user,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return VIEW_NAME;
        }

        userService.registerUser(user, UUID.randomUUID().toString());
        putSuccessRedirectMessage(redirectAttributes, "User Successfully Registered. Verify Email to Login");

        return "redirect:/login";
    }

    @GetMapping("/confirmToken/{token}")
    public String showConfirmTokenPage(@PathVariable String token, RedirectAttributes redirectAttributes) {
        VerificationToken verificationToken = verificationTokenRepository.getOne(token);


        if (verificationToken.isExpired()) {
            putErrorRedirectMessage(redirectAttributes, "Invalid Verification Code");
            return "redirect:/login";
        }

        switch (verificationToken.getType()) {
            case EMAIL_CONFIRMATION:
                userRepository.findById(verificationToken.getOwner().getId())
                        .ifPresent(u -> u.setEnabled(true));

                verificationTokenRepository.delete(verificationToken);
                putSuccessRedirectMessage(redirectAttributes, "Email Verified Successfully");
                return "redirect:/login";

            case FORGET_PASSWORD:
                return RESET_PASSWORD_VIEW_NAME;

            default:
                throw new IllegalStateException("Unknown token type");

        }
    }

    @PostMapping("/confirmToken/{token}")
    public String submitConfirmTokenPage(@PathVariable String token,
                                         @RequestParam String password,
                                         @RequestParam String confirmPassword,
                                         ModelMap model,
                                         RedirectAttributes redirectAttributes) {

        VerificationToken verificationToken = verificationTokenRepository.getOne(token);

        if (verificationToken.isExpired() || FORGET_PASSWORD != verificationToken.getType()) {
            putErrorRedirectMessage(redirectAttributes, "Invalid Verification Code");
            return "redirect:/login";
        }

        String message = null;
        if (Objects.isNull(password) || Objects.isNull(confirmPassword)) {
            message = "Both fields are required";

        } else if (!password.equals(confirmPassword)) {
            message = "Password and Confirm Password didn't match";
        }

        if (Objects.isNull(message)) {

            verificationToken.getOwner().setHashedPassword(encoder.encode(password));
            verificationTokenRepository.delete(verificationToken);

            putSuccessRedirectMessage(redirectAttributes, "Password Updated Successfully");
            return "redirect:/login";
        }

        model.put("errorMessage", message);
        return RESET_PASSWORD_VIEW_NAME;
    }

    @GetMapping("/forgetPassword")
    public String showForgetPasswordForm() {
        return "forgetPassword";
    }

    @PostMapping("/forgetPassword")
    public String submitForgetPasswordEmail(@RequestParam String email,
                                            RedirectAttributes ra) {

        // TODO: Validate email format

        userRepository.findByEmail(email).ifPresent(u -> {
            String token = UUID.randomUUID().toString();
            verificationTokenRepository.save(new VerificationToken(token, u, FORGET_PASSWORD));
            log.error("Generated token: {}", token);
        });

        // Don't show any message whether the email was found
        putSuccessRedirectMessage(ra, "Password reset instructions sent in email");
        return "redirect:/login";
    }

}
