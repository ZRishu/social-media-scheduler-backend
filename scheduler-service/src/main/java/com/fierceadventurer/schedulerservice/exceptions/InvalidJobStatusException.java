package com.fierceadventurer.schedulerservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJobStatusException extends RuntimeException {
    public InvalidJobStatusException(String message) {
        super(message);
    }
}
