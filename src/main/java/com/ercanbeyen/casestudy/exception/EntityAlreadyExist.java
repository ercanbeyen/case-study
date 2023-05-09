package com.ercanbeyen.casestudy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.CONFLICT) // Baglanti hatasi
public class EntityAlreadyExist extends RuntimeException {
    public EntityAlreadyExist(String message) {
        super(message);
    }
}
