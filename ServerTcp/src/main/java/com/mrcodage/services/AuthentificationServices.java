package com.mrcodage.services;

import com.mrcodage.InvalidUserConnectionException;
import com.mrcodage.model.User;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.repository.UserRepository;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthentificationServices {
    private final UserRepository userRepository;
    public AuthentificationServices(){
        this.userRepository = new UserRepository();
    }
    public boolean isUserToConnectValid(UserToConnect userToConnect) throws IOException, NoSuchAlgorithmException {
        User user = this.userRepository.getByUsername(userToConnect.username());
        if(userToConnect.password().equals(user.getPassword())){
            return true;
        }else{
            throw new InvalidUserConnectionException("Identifiant invalide");
        }
    }

}
