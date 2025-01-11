package com.mrcodage.repository;

import com.mrcodage.UserNotFoundException;
import com.mrcodage.data.UserJsonMapper;
import com.mrcodage.model.User;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.utilitaires.PasswordTools;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserRepository {
    private File jsonFile;
    public UserRepository(){
        this.jsonFile = new File(System.getProperty("user.dir")+File.separator+"users.json");
    }
    public List<User> getAll()throws IOException {
        return UserJsonMapper.toUser(this.jsonFile);
    }

    public User getByUsername(String username) throws IOException, NoSuchAlgorithmException {
        if(UserJsonMapper.toUser(this.jsonFile)==null){
            User userExample = new User("joe","Doe","jdoe", PasswordTools.hashPassword("joeDoe"));
            UserJsonMapper.toJson(userExample,this.jsonFile);
        }
        List<User> users = List.copyOf(UserJsonMapper.toUser(this.jsonFile));
        return users.stream()
                .filter(u->u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(()-> new UserNotFoundException(username));
    }

    public void createUser(User user) throws IOException {
        UserJsonMapper.toJson(user,this.jsonFile);
    }
}
