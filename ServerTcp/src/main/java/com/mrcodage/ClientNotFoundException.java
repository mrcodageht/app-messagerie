package com.mrcodage;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String uuid) {
        super("Le socket lie a l'utilisateur avec uuid {"+uuid+"} n'a pas ete trouve");
    }
}
