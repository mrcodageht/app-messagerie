package com.mrcodage;

public class FieldNullException extends RuntimeException {
    public FieldNullException(String fieldName) {
        super("Le champ '"+fieldName+"' ne peut pas etre null");
    }
}
