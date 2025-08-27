package com.ra.validator;

import com.ra.model.dto.UserRegisterRequest;
import com.ra.model.entity.User;
import com.ra.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, UserRegisterRequest> {
    private final UserService userService;

    public EmailUniqueValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(EmailUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegisterRequest request, ConstraintValidatorContext context) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) return true;

        boolean exists = userService.findUserByEmail(request.getEmail()).isPresent();
        if (exists) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email đã tồn tại!")
                    .addPropertyNode("email")
                    .addConstraintViolation();
        }
        return !exists;
    }
}
