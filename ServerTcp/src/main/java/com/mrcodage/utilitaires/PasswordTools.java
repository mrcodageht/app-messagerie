package com.mrcodage.utilitaires;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface PasswordTools {
    static byte[] hashPassword(String passwordToHash, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        digester.update(salt); // On génère un salt pour le mot de passe
        return digester.digest(passwordToHash.getBytes());
    }

    static byte[] getGenerateSalt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(salt);
        return salt;
    }
}
