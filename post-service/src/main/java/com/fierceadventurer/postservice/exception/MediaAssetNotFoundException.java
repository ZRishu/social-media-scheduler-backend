package com.fierceadventurer.postservice.exception;

public class MediaAssetNotFoundException extends RuntimeException {
    public MediaAssetNotFoundException(String message) {
        super(message);
    }
}
