package com.mrcodage.services;

import com.mrcodage.InvalidUserConnectionException;
import com.mrcodage.Variables;
import com.mrcodage.model.User;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.repository.UserRepository;
import com.mrcodage.utilitaires.PasswordTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AuthentificationServicesTest {

    private AuthentificationServices authentificationServices;
    private UserToConnect userToConnect;
    User user;
    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, IOException {
        this.authentificationServices = new AuthentificationServices();
        UserRepository userRepository = new UserRepository();
        this.userToConnect = new UserToConnect("bobby","wesner123");
//        this.user = new User("wesner","philo","wesy", PasswordTools.hashPassword("mrcodage123"));
//        userRepository.createUser(user);
    }

    @AfterEach
    void tearDown() {
        this.authentificationServices = null;
    }

    @Test
    void isUserToConnectValid_should_be_true() throws IOException, NoSuchAlgorithmException {
//        TODO : reecrire mes tests unitaires pour la classe Authentification services
        System.out.println(Variables.PATHUSERSFILEDATA);
        assertTrue(this.authentificationServices.isUserToConnectValid(userToConnect));
    }

    @Test
    void isUserToConnectionValid_shoul_be_throw_exception() throws NoSuchAlgorithmException {
//        TODO : reecrire mes tests unitaires pour la classe Authentification services
    }

}