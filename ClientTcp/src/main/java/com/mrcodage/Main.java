package com.mrcodage;

import com.mrcodage.model.UserToRegister;
import com.mrcodage.utilitaires.UserMethodeInterface;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Map<String,String> mapInfos = UserMethodeInterface.userRegisterInfos();

        UserToRegister newUser = new UserToRegister(
                mapInfos.get("firstname"),
                mapInfos.get("lastname"),
                mapInfos.get("username"),
                mapInfos.get("password")
        );

        System.out.println("Nouvel utilisateur : ");
        System.out.println(newUser);
    }
}
