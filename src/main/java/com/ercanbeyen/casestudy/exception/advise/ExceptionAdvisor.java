package com.ercanbeyen.casestudy.exception.advise;


import com.ercanbeyen.casestudy.exception.DocumentAlreadyExistException;
import com.ercanbeyen.casestudy.exception.DocumentNotFoundException;
import com.ercanbeyen.casestudy.exception.ExceptionResponse;
import com.ercanbeyen.casestudy.exception.FileNotHandledException;
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

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(DocumentAlreadyExistException.class)
    public ResponseEntity<Object> handleEntityAlreadyExistException(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.CONFLICT.value(), LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(FileNotHandledException.class)
    public ResponseEntity<Object> handleFileNotHandledException(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
