package com.mrcodage.services;

import com.mrcodage.InvalidUserConnectionException;
import com.mrcodage.UserNotFoundException;
import com.mrcodage.model.User;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.repository.UserRepository;
import com.mrcodage.utilitaires.PasswordTools;
import com.mrcodage.utilitaires.Tools;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class AuthentificationServerServices {
    private final UserRepository userRepository;
    public AuthentificationServerServices() throws IOException {
        this.userRepository = new UserRepository();
    }
    public boolean isUserToConnectValid(UserToConnect userToConnect) throws IOException, NoSuchAlgorithmException, UserNotFoundException {
        User user = this.userRepository.getByUsername(userToConnect.username());

        String saltGeneratedFromUser_str = user.getAccount().getSalt();
        byte[] saltGeneratedFromUser_byte = Tools.getByteSaltEncodeBase64(saltGeneratedFromUser_str);
        byte[] passwordHashed_byte = PasswordTools.hashPassword(userToConnect.password(),saltGeneratedFromUser_byte);
        String passwordHashed_str = Tools.getStringFromByteEncodeBase64(passwordHashed_byte);

        if(!passwordHashed_str.equals(user.getAccount().getPassword())){
            throw new InvalidUserConnectionException("Identifiants de connection invalide");
        }
        return true;
    }


}
