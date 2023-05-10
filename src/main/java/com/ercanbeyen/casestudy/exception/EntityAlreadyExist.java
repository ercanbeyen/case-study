package com.ercanbeyen.casestudy.exception;


public class EntityAlreadyExist extends RuntimeException {
    public EntityAlreadyExist(String message) {
        super(message);
    }
}
