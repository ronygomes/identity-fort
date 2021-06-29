package me.ronygomes.identity_fort.validator;

import me.ronygomes.identity_fort.entity.Application;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ApplicationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Application.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
