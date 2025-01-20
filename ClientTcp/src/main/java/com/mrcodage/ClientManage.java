package com.mrcodage;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientManage {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public void startConnection(InetAddress ip, int port) throws IOException {
        socket = new Socket(ip, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connecté au serveur : " + ip + " sur le port " + port);
    }

    public void sendMessage(Message message) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) ois.readObject();
    }

    public void stopConnection() throws IOException {
        ois.close();
        oos.close();
        socket.close();
        System.out.println("Connexion fermée.");
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

}
