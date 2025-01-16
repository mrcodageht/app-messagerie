package com.mrcodage.utilitaires;

import com.mrcodage.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

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
//        sc.close();
        return response;
    }
    static void formatMessage(Message messageToFormat){
        System.out.printf("\n\t\t[%s] : %s : [%s]\n",messageToFormat.getSender(),messageToFormat.getContent(),messageToFormat.getDateTime().format(ISO_LOCAL_DATE));
    }
}
