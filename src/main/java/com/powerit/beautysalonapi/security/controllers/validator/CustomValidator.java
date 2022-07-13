package com.powerit.beautysalonapi.security.controllers.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;

@Component
public class CustomValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return true; //для всех
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("validation");
        for (Field declaredField : target.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            try {
                Object field = declaredField.get(target);
                if (field == null) {
                    System.out.println(declaredField.getName() + " is null");
                    //rejectValue(как в твоем примере) не делало ничего, так что пошла моя фантазия
                    //errors.rejectValue(declaredField.getName(), "is null"); return;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, declaredField.getName() + " is null");
                }
                if (field.getClass() == String.class && field.toString().equals("")) {
                    System.out.println(declaredField.getName() + " is empty");
                    //errors.rejectValue(declaredField.getName(), "is empty"); return;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, declaredField.getName() + " is empty");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
