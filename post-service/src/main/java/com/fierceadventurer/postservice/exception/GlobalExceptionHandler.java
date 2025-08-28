package com.fierceadventurer.postservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalExceptionHandler {
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex , WebRequest request) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status" , HttpStatus.NOT_FOUND.value());
        body.put("error" , "NOT FOUND");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=",""));
        return new ResponseEntity<>(body , HttpStatus.NOT_FOUND);
    }
}
