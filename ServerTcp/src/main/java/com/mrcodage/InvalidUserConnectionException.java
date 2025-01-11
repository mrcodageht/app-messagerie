package com.mrcodage;

public class InvalidUserConnectionException extends RuntimeException {
    public InvalidUserConnectionException(String message) {
        super(message);
    }
}
