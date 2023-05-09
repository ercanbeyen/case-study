package com.ercanbeyen.casestudy.exception.advise;


import com.ercanbeyen.casestudy.exception.EntityAlreadyExist;
import com.ercanbeyen.casestudy.exception.EntityNotFound;
import com.ercanbeyen.casestudy.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {
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
