package com.ercanbeyen.casestudy.exception;


public class DocumentAlreadyExistException extends RuntimeException {
    public DocumentAlreadyExistException(String message) {
        super(message);
    }
}
