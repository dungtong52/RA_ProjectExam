package com.ra.validator;

import com.ra.model.entity.User;
import com.ra.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, User> {
    private final UserService userService;

    public EmailUniqueValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(EmailUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) return true;

        Optional<User> existingUser = userService.findUserByEmail(user.getEmail());
        return existingUser
                .map(u -> u.getId().equals(user.getId()))
                .orElse(true);
    }
}
