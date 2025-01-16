package com.mrcodage.services;

import com.mrcodage.CommandServer;
import com.mrcodage.UserManage;
import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ListCommandHandler implements CommandHandler{
    @Override
    public void execute(Message message, Socket socketClient, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String userName : UserManage.userTracker.keySet()) {
            String uuid = UserManage.userTracker.get(userName);
            if (UserManage.clientsConnected.get(uuid) == socketClient) {
                sb.append(userName).append(" (vous)").append("|");
            } else {
                sb.append(userName).append("|");
            }
        }
        Message msg = new Message("server", sb.toString(), CommandServer.LIST);
        writeObject(oos, msg);
    }
    private void writeObject(ObjectOutputStream oos, Message message) throws IOException {
        oos.reset();
        oos.writeObject(message);
        oos.flush();
    }
}
