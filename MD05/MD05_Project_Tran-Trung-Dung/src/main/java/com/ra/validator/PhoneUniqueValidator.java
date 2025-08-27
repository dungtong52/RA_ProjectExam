package com.ra.validator;

import com.ra.model.dto.UserRegisterRequest;
import com.ra.model.entity.User;
import com.ra.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class PhoneUniqueValidator implements ConstraintValidator<PhoneUnique, UserRegisterRequest> {
    private final UserService userService;

    public PhoneUniqueValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(PhoneUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegisterRequest request, ConstraintValidatorContext context) {
        if (request.getPhone() == null || request.getPhone().isEmpty()) return true;

        boolean exists = userService.findUserByEmail(request.getPhone()).isPresent();
        if (exists) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Số điện thoại đã tồn tại!")
                    .addPropertyNode("phone")
                    .addConstraintViolation();
        }
        return !exists;
    }
}
