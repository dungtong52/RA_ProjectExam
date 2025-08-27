package com.ra.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = { EmailUniqueValidator.class })
@Target({ ElementType.TYPE  })
@Retention(RUNTIME)
public @interface EmailUnique {
    String message() default "Email đã tồn tại!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
