package com.mrcodage.services;

import com.mrcodage.InvalidUserConnectionException;
import com.mrcodage.model.User;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.repository.UserRepository;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AuthentificationServerServicesTest {

    private static AuthentificationServerServices authentificationServerServices;
    private static UserToConnect userToConnect;
    private static UserRepository userRepository;
    private static User user;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException, IOException {
        authentificationServerServices = new AuthentificationServerServices();
        userRepository = new UserRepository();
        userToConnect = new UserToConnect("joedoe","joedoe123");

        String salt = "y4YlDlH9cZ5swq6gTyZY2A==";
//        user = new User("joe","doe",new Account("joedoe",
//                Tools.getStringFromByteEncodeBase64(salt),
//                Tools.getStringFromByteEncodeBase64(
//                        PasswordTools.hashPassword("joedoe123",salt))));
//        userRepository.createUser(user);
    }

    @AfterAll
    static void tearDown() {
        authentificationServerServices = null;
    }

    @Test
    void isUserToConnectValid_should_be_true() throws IOException, NoSuchAlgorithmException {
//        TODO : reecrire mes tests unitaires pour la classe Authentification services
        assertTrue(authentificationServerServices.isUserToConnectValid(userToConnect));
    }

    @Test
    void isUserToConnectionValid_shoul_be_throw_exception() throws NoSuchAlgorithmException {
//        TODO : reecrire mes tests unitaires pour la classe Authentification services
        assertThrows(InvalidUserConnectionException.class,()->{
            authentificationServerServices.isUserToConnectValid(new UserToConnect("joedoe","toto123"));
        });
    }

}