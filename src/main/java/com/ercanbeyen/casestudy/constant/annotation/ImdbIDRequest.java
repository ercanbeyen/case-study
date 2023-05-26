package com.ercanbeyen.casestudy.constant.annotation;

import com.ercanbeyen.casestudy.validator.ImdbIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImdbIDValidator.class)
public @interface ImdbIDRequest {
    String message() default "Invalid Imdb ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
