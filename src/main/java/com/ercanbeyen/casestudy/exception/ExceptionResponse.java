package com.ercanbeyen.casestudy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private int status;
    private LocalDateTime localDateTime;
    private Object message;
}
