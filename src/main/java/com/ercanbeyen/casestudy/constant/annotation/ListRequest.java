package com.ercanbeyen.casestudy.constant.annotation;

import com.ercanbeyen.casestudy.validator.ListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ListValidator.class)
public @interface ListRequest {
    String message() default "List should not be neither null nor empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
