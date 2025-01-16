package com.mrcodage.services;

import com.mrcodage.CommandServer;
import com.mrcodage.model.Message;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.utilitaires.UserMethodeInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class AuthentificationClientServices {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public AuthentificationClientServices(ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        this.oos = oos;
        this.ois = ois;
    }

    public boolean isConnectionEtablished(Map<String,String> credentials) throws IOException, ClassNotFoundException {
        String username = "";
        String password = "";
        for(Map.Entry<String,String> userCredentials : credentials.entrySet()){
            username = userCredentials.getKey();
            password = userCredentials.getValue();
        }
        UserToConnect userToConnect = new UserToConnect(username,password);
        Message connectionMessage = new Message(username,userToConnect, CommandServer.CONNECT);

        sendMessageConnetion(connectionMessage);

        Message receiveMessage =  readMessageConnection();

        try {
            if (receiveMessage == null) {
                throw new Exception("Message recu est null");
            }else{
                System.out.println("message non null");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        assert receiveMessage != null;
        return receiveMessage.getCode()==200;
    }

    private void sendMessageConnetion(Message message) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    private Message readMessageConnection() throws IOException, ClassNotFoundException {
        return (Message) ois.readObject();
    }
}
