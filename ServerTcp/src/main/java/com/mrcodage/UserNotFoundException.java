package com.mrcodage;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("L'utilisateur avec le nom d'utilisateur {"+username+"} n'a pas ete trouve");
    }
}
