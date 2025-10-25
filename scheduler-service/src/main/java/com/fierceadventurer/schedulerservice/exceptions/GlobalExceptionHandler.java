package com.fierceadventurer.schedulerservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex,  WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp" , LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error" , "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=",""));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidJobStatusException.class)
    public ResponseEntity<Object> handleInvalidJobStatusException(
            InvalidJobStatusException ex,  WebRequest request
    ){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp" , LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error" , "Bad Request");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=",""));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
