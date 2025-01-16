package com.mrcodage.utilitaires;

import com.mrcodage.services.AuthentificationServerServices;

import java.io.IOException;

public class AuthentificationServerServicesIntance {
    private static final AuthentificationServerServices AUTHENTIFICATION_SERVER_SERVICES;

    static {
        try {
            AUTHENTIFICATION_SERVER_SERVICES = new AuthentificationServerServices();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthentificationServerServices getInstance(){
        return AUTHENTIFICATION_SERVER_SERVICES;
    }
}
