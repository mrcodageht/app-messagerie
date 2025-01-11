package com.mrcodage.utilitaires;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public interface UserMethodeInterface {
    static HashMap<String,String> getUserIdentifiantConnection(){
        String username = ScannerString("Entrez votre nom d'utilisateur : ");
        String password = ScannerString("Entrez votre mot de passe");
        HashMap<String,String> map = new HashMap<>();
        map.put(username,password);
        return map;
    }

    static String ScannerString(String message){
        Scanner sc = new Scanner(System.in);
        System.out.println(message);
        String response = sc.nextLine();
        sc.close();
        return response;
    }
}
