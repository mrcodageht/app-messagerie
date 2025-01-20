package com.mrcodage.utilitaires;

import com.mrcodage.FieldNullException;
import com.mrcodage.model.Message;

import java.util.*;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public interface UserMethodeInterface {
    static HashMap<String,String> getUserIdentifiantConnection(){
        String username = ScannerString("Entrez votre nom d'utilisateur : ","username");
        String password = ScannerString("Entrez votre mot de passe","password");
        HashMap<String,String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        return map;
    }

    static HashMap<String, String> userRegisterInfos(){
        String firstname = ScannerString("Entrez votre nom prenom","firstname");
        String lastname = ScannerString("Entrez votre nom de famille","lastname");
        String username = ScannerString("Entrez votre nom d'utilisateur : ","username");
        String password = ScannerString("Entrez votre mot de passe","password");

        HashMap<String, String> mapToReturn = new HashMap<>();
        mapToReturn.put("firstname",firstname);
        mapToReturn.put("lastname",lastname);
        mapToReturn.put("username",username);
        mapToReturn.put("password",password);
        return mapToReturn;
    }

    static String ScannerString(String message,String fieldName){
        Scanner sc = new Scanner(System.in);
        boolean isCorrect = false;
        String response = "";
        do {
            System.out.println(message);
            response = sc.nextLine();
            try {
                isNullFieldInput(response, fieldName);
                isCorrect = !response.isBlank();
            } catch (FieldNullException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
                System.out.println();
            }

        }while(!isCorrect);
        return response;
    }
    static void formatMessage(Message messageToFormat){
        System.out.printf("\n\t\t[%s] : %s : [%s]\n",messageToFormat.getSender(),messageToFormat.getContent(),messageToFormat.getDateTime().format(ISO_LOCAL_DATE));
    }

    static void isNullFieldInput(String input, String fieldName){
        if(input.isBlank()) throw new FieldNullException(fieldName);
    }
}
