package com.mrcodage;

import com.mrcodage.data.UserJsonMapper;
import com.mrcodage.model.Account;
import com.mrcodage.model.User;
import com.mrcodage.utilitaires.PasswordTools;
import com.mrcodage.utilitaires.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

//        hNeRC5TJKA0qhHL4JhP+V4gF/Wa6m2WRkrbhy+36ZZs=

        byte[] salt = Tools.getByteSaltEncodeBase64("1BFX4BdLjaXGqUvrnoDydQ==");
//        System.out.println(Arrays.toString(salt));
        byte[] passwordHash = PasswordTools.hashPassword("wesner123",salt);
        System.out.println(DatatypeConverter.printBase64Binary(passwordHash));

        Account account = new Account("bobby",DatatypeConverter.printBase64Binary(salt),DatatypeConverter.printBase64Binary(passwordHash));
        User user = new User("wesner","philogene",account);
        System.exit(0);
        System.out.println(Variables.PATHUSERSFILEDATA);
        File file = new File(System.getProperty("user.dir")+ File.separator+"users.json");

        UserJsonMapper.toJson(user,file);
        List<User> users = UserJsonMapper.toUser(file);
        users.forEach(System.out::println);
    }
}
