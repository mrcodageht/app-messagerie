package com.mrcodage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserJsonMapper {
    public static List<User> toUser(File jsonFile) throws IOException {
        List<User> users = new ArrayList<>();
        var object = new ObjectMapper();
        users = object.readValue(jsonFile, new TypeReference<List<User>>() {});
        return users;
    }

    public static void toJson(User user) throws IOException {
        File file = new File(System.getProperty("user.dir")+File.separator+"users.json");
        if(!file.exists()){
            file.createNewFile();
        }
        var object = new ObjectMapper();
        List<User> users;

        if(file.exists() && file.length()>0){
            users = object.readValue(file, new TypeReference<List<User>>() {});
        }else{
            users = new ArrayList<>();
        }
        users.add(user);

        object.enable(SerializationFeature.INDENT_OUTPUT);
        object.writeValue(file,users);
        System.out.println("Utilisateur ecrit dans le fichier json");
    }
}
