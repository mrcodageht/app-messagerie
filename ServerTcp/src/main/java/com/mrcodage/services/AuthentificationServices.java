package com.mrcodage.services;

import com.mrcodage.InvalidUserConnectionException;
import com.mrcodage.model.User;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.repository.UserRepository;
import com.mrcodage.utilitaires.PasswordTools;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class AuthentificationServices {
    private final UserRepository userRepository;
    public AuthentificationServices() throws IOException {
        this.userRepository = new UserRepository();
    }
    public boolean isUserToConnectValid(UserToConnect userToConnect) throws IOException, NoSuchAlgorithmException {
        User user = this.userRepository.getByUsername(userToConnect.username());

        System.out.println(DatatypeConverter.printBase64Binary(PasswordTools.getGenerateSalt()));

        throw new InvalidUserConnectionException("Identifiant invalide");

    }


}
