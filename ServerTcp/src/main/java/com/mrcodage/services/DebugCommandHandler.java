package com.mrcodage.services;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DebugCommandHandler implements CommandHandler{
    @Override
    public void execute(Message message, Socket socketClient, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        System.out.println("Message reçu de " + message.getSender() + ": " + message.getContent());
        Message messageReponseDebug = new Message("server","Ceci est un message de test pour les commandes du server");
        writeObject(oos,messageReponseDebug);

    }

    private void writeObject(ObjectOutputStream oos, Message message) throws IOException {
        oos.reset();
        oos.writeObject(message);
        oos.flush();
    }
}
