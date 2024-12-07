package com.mrcodage;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    static int PORT = 9360;
    public static void main(String[] args) throws IOException {

        try(ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("10.0.x.x"))){
            System.out.println("----------------------");
            System.out.println("Lancement du serveur");
            System.out.println("----------------------");

            while(true){
                Socket socketClient = serverSocket.accept();

                Runnable socketThread = ()->{
                    try {
                        processThread(socketClient);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                };
                socketThread.run();
            }

        }
    }

    private static void processThread(Socket socketClient) throws IOException, ClassNotFoundException {
        System.out.println("-------------------------------------------------");
        System.out.println("Connexion avec : "+socketClient.getInetAddress());
        System.out.println("-------------------------------------------------");
        System.out.println();

        while(socketClient.isConnected()) {
            readClientInput(socketClient);
            sendResponseToClient(socketClient);
        }
    }

    private static void sendResponseToClient(Socket socketClient) throws IOException {
        var out = new ObjectOutputStream(socketClient.getOutputStream());
        Message serverMessage = new Message("Client connecte","Message bien recu");
        out.writeObject(serverMessage);
    }

    private static void readClientInput(Socket socketClient) throws IOException, ClassNotFoundException {
        var in = new ObjectInputStream(socketClient.getInputStream());
        Message message = (Message) in.readObject();

        System.out.println("--------------------------------------------------------------");
        System.out.println("Message du client : "+message);
        System.out.println("--------------------------------------------------------------");
    }
}