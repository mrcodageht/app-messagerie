package com.mrcodage.utilitaires;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface PasswordTools {
    public static String hashPassword(String passwordToHash) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        byte[] input = passwordToHash.getBytes();
        byte[] digest = digester.digest(input);
        return DatatypeConverter.printBase64Binary(digest);
    }
}
