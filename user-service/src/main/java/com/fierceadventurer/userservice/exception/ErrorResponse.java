package com.fierceadventurer.userservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timeStamp,
        int status,
        String error,
        String message,
        String path,
        Map<String , List<String>> validationErrors){

    public ErrorResponse(HttpStatus status , String message , String path){
        this(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message , path , null);
    }

    public ErrorResponse(HttpStatus status , String message , String path , Map<String , List<String>> validationErrors){
        this(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message , path , validationErrors);
    }
}
