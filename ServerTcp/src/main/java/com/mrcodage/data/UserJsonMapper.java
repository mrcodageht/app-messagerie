package com.mrcodage.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mrcodage.model.User;
import com.mrcodage.model.UserToConnect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserJsonMapper {
    public static List<User> toUser(File jsonFile) throws IOException {
        List<User> users = new ArrayList<>();
        var object = new ObjectMapper();
        users = object.readValue(jsonFile, new TypeReference<List<User>>() {});
        return users;
    }

    public static void toJson(User user, File jsonFile) throws IOException{
        if(!jsonFile.exists()){
            jsonFile.createNewFile();
        }
        var object = new ObjectMapper();
        List<User> users;

        if(jsonFile.exists() && jsonFile.length()>0){
            users = object.readValue(jsonFile, new TypeReference<List<User>>() {});
        }else{
            users = new ArrayList<>();
        }
        users.add(user);

        object.enable(SerializationFeature.INDENT_OUTPUT);
        object.writeValue(jsonFile,users);
        System.out.println("L'operation dans le fichier json a bien ete effectue");
    }
}
