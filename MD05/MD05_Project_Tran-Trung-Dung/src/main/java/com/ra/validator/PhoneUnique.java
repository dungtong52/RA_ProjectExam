package com.ra.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {PhoneUniqueValidator.class})
@Target({TYPE_USE})
@Retention(RUNTIME)
public @interface PhoneUnique {
    String message() default "Số điện thoại đã tồn tại!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
