package com.ercanbeyen.casestudy.exception.advise;


import com.ercanbeyen.casestudy.exception.EntityAlreadyExist;
import com.ercanbeyen.casestudy.exception.EntityNotFound;
import com.ercanbeyen.casestudy.exception.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), errors);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<?> handleEntityNotFoundException(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(EntityAlreadyExist.class)
    public ResponseEntity<?> handleEntityAlreadyExist(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.CONFLICT.value(), LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
