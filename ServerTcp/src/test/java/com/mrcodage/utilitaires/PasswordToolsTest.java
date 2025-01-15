package com.mrcodage.utilitaires;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PasswordToolsTest {

    @BeforeAll
    static void setUp(){

    }


    @Test
    void hashPassword() {
    }

    @Test
    void getGenerateSalt_should_succesfully() throws NoSuchAlgorithmException {
        /*
            [B@49c90a9c
            [B@69ee81fc
         **/
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt1 = new byte[16];
        byte[] salt2 = new byte[16];
        secureRandom.nextBytes(salt1);
        secureRandom.nextBytes(salt2);


        System.out.println(DatatypeConverter.printBase64Binary(salt1));
        System.out.println(DatatypeConverter.printBase64Binary(salt2));

//        assertNotNull(salt);
    }
}
