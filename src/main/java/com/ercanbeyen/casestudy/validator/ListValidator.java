package com.ercanbeyen.casestudy.validator;

import com.ercanbeyen.casestudy.constant.annotation.ListRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.function.Predicate;


public class ListValidator implements ConstraintValidator<ListRequest, List<String>> {
    private final Predicate<List<String>> listChecker = list -> list != null && !list.isEmpty();

    @Override
    public void initialize(ListRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<String> list, ConstraintValidatorContext constraintValidatorContext) {
        return listChecker.test(list);
    }
}
