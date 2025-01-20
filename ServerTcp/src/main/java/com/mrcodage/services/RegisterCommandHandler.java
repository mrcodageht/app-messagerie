package com.mrcodage.services;

import com.mrcodage.model.Account;
import com.mrcodage.model.Message;
import com.mrcodage.model.User;
import com.mrcodage.model.UserToRegister;
import com.mrcodage.repository.UserRepository;
import com.mrcodage.utilitaires.PasswordTools;
import com.mrcodage.utilitaires.Tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class RegisterCommandHandler implements CommandHandler {
    @Override
    public void execute(Message message, Socket socketClient, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        UserToRegister userToRegister = message.getUserToRegister();
        byte[] saltGenerated = null;
        byte[] passwordHashed = null;
        try {
            saltGenerated = PasswordTools.getGenerateSalt();
            passwordHashed = PasswordTools.hashPassword(userToRegister.password(),saltGenerated);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Une erreur s'est produite : "+e.getMessage());
        }

        Account accountUser = new Account(
                userToRegister.username(),
                Tools.getStringFromByteEncodeBase64(saltGenerated),
                Tools.getStringFromByteEncodeBase64(passwordHashed)
        );

        User newUser = new User(
                userToRegister.username(),
                userToRegister.lastname(),
                accountUser
        );

        UserRepository userRepository = new UserRepository();
        userRepository.createUser(newUser);




    }

//    private void isNullOrEmpty(String str_to_check){
//        if(str_to_check.isBlank()){
//            throw
//        }
//    }

}
