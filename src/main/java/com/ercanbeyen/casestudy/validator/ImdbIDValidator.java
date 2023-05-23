package com.ercanbeyen.casestudy.validator;

import com.ercanbeyen.casestudy.constant.annotation.ImdbIDRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.function.Predicate;

public class ImdbIDValidator implements ConstraintValidator<ImdbIDRequest, String> {

    private final Predicate<String> imdbIDChecker = imdbID -> imdbID.startsWith("tt") && imdbID.length() >= 3;

    @Override
    public void initialize(ImdbIDRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String imdbID, ConstraintValidatorContext constraintValidatorContext) {
        return imdbIDChecker.test(imdbID);
    }
}
