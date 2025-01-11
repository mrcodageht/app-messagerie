package com.mrcodage;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

//        User user = new User("mrcodage","mrc@mrcodage.com","mrcodage1234");
//        UserJsonMapper.toJson(user);
        File file = new File(System.getProperty("user.dir")+File.separator+"users.json");
        List<User> users = List.copyOf(UserJsonMapper.toUser(file));

        users.forEach(System.out::println);

    }
}