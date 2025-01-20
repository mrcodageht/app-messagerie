package com.mrcodage.services;

import com.mrcodage.CommandServer;
import com.mrcodage.model.Message;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.model.UserToRegister;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AuthentificationClientServices {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public AuthentificationClientServices(ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        this.oos = oos;
        this.ois = ois;
    }

    public boolean isConnectionEtablished(Map<String,String> credentials) throws IOException, ClassNotFoundException {
        String username = credentials.get("username");
        String password = credentials.get("password");

        UserToConnect userToConnect = new UserToConnect(username,password);
        Message connectionMessage = new Message(username,userToConnect, CommandServer.CONNECT);

        sendMessage(connectionMessage);

        Message receiveMessage =  readMessage();


        if (receiveMessage == null) {
            System.err.println("Le message recu est null");
        }else{
            System.out.println("message non null");
        }



        assert receiveMessage != null;
        return receiveMessage.getCode()==200;
    }

    public boolean isRegisterCorrectly(HashMap<String,String> registerInfos) throws IOException, ClassNotFoundException {
        UserToRegister newUser = new UserToRegister(
                registerInfos.get("firstname"),
                registerInfos.get("lastname"),
                registerInfos.get("username"),
                registerInfos.get("password")
        );

        Message registerMessage = new Message(newUser.username(), newUser,CommandServer.REGISTER);

        sendMessage(registerMessage);
        Message receiveMessage = readMessage();

        String text = receiveMessage==null ? "Le message recu est null" : "Le message recu est correct : "+receiveMessage.getContent();

        System.out.println(text);

        assert receiveMessage != null;
        return receiveMessage.getCode()==200;
    }

    private void sendMessage(Message message) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    private Message readMessage() throws IOException, ClassNotFoundException {
        return (Message) ois.readObject();
    }
}
