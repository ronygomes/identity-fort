package me.ronygomes.identity_fort.validator;

import me.ronygomes.identity_fort.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class UserRegistrationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (Objects.nonNull(user.getRawPassword())
                && Objects.nonNull(user.getConfirmPassword())
                && !user.getRawPassword().equals(user.getConfirmPassword())) {

            errors.rejectValue("rawPassword", "error.password.and.confirm.password.mismatch");
        }
    }
}
