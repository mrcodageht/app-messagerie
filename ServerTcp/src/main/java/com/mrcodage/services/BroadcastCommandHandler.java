package com.mrcodage.services;

import com.mrcodage.UserManage;
import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class BroadcastCommandHandler implements CommandHandler{
    @Override
    public void execute(Message message, Socket socketClient, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        Message messageDeconnection = new Message("server", "Vous etes offline", 105);
        writeObject(oos, messageDeconnection);
        String uuid = "";
        for (Map.Entry<String, Socket> entry : UserManage.clientsConnected.entrySet()) {
            if (entry.getValue() == socketClient) {
                uuid = entry.getKey();
                break;
            }
        }
//        logger.warn("Deconnection du client avec le uuid : {}", uuid);
        socketClient.close();
    }

    private void writeObject(ObjectOutputStream oos, Message message) throws IOException {
        oos.reset();
        oos.writeObject(message);
        oos.flush();
    }
}
