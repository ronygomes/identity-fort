package me.ronygomes.identity_fort.validator;

import me.ronygomes.identity_fort.entity.Application;
import me.ronygomes.identity_fort.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserRegistrationValidatorTest {

    private static final String PASSWORD_FIELD_NAME = "rawPassword";

    private final UserRegistrationValidator validator = new UserRegistrationValidator();

    @Test
    void testSupportsWorkWithUserAssignable() {
        Assertions.assertTrue(validator.supports(User.class));
        Assertions.assertTrue(validator.supports(UserDetails.class));
        Assertions.assertTrue(validator.supports(Serializable.class));

        Assertions.assertFalse(validator.supports(Application.class));
    }

    @Test
    void testPasswordMismatchRaisesError() {
        User u = new User();
        u.setRawPassword("password1");
        u.setConfirmPassword("password2");

        Map<String, Object> targets = new HashMap<>();
        targets.put("user", u);

        Errors error = new MapBindingResult(targets, "user");

        validator.validate(u, error);
        Assertions.assertEquals(1, error.getErrorCount());
        Assertions.assertTrue(error.hasFieldErrors(PASSWORD_FIELD_NAME));

        Assertions.assertNotNull(error.getFieldError(PASSWORD_FIELD_NAME));
        Assertions.assertEquals("error.password.and.confirm.password.mismatch",
                error.getFieldError(PASSWORD_FIELD_NAME).getCode());
    }

    @Test
    void testPasswordMatchRaisesNoError() {
        User u = new User();
        u.setRawPassword("password");
        u.setConfirmPassword("password");

        Map<String, Object> targets = new HashMap<>();
        targets.put("user", u);

        Errors error = new MapBindingResult(targets, "user");

        validator.validate(u, error);
        Assertions.assertEquals(0, error.getErrorCount());
        Assertions.assertFalse(error.hasFieldErrors(PASSWORD_FIELD_NAME));
        Assertions.assertNull(error.getFieldError(PASSWORD_FIELD_NAME));
    }
}
