package com.mrcodage;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;

public class ClientTCP {
    static boolean FIRST_CONNECTION = true;
    public static void main(String[] args) {
        try{
            var server = InetAddress.getByName("localhost");
            var socket = new Socket(server,ServerTCP.PORT);

            etablishConnection(socket);

            communicate(socket);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void etablishConnection(Socket socket) throws IOException, ClassNotFoundException{
        var out = new ObjectOutputStream(socket.getOutputStream());
        String clientId = generate_uuid();
        Message connectionMessage = new Message(clientId,"connection request");
        out.writeObject(connectionMessage);
        readMessage(socket);
    }

    private static void communicate(Socket socket) throws IOException,ClassNotFoundException{
        while (!socket.isClosed()) {
            sendMessage(socket);
            readMessage(socket);
        }
    }

    private static void readMessage(Socket socket) throws IOException, ClassNotFoundException {
        var in = new ObjectInputStream(socket.getInputStream());
        Message messageLu = (Message) in.readObject();
        System.out.println("* Serveur * : "+messageLu.content());
    }

    private static void sendMessage(Socket socket) throws IOException {
        var out = new ObjectOutputStream(socket.getOutputStream());
        String content="";
        Scanner sc = new Scanner(System.in);

        System.out.print("Client > ");
        content = sc.nextLine();

        Message message = new Message("client",content);
        out.writeObject(message);
    }

    private static String generate_uuid(){
        var uuid = UUID.randomUUID();
        var uuidSplit = String.valueOf(uuid).split("-");
        return uuidSplit[uuidSplit.length-1];
    }
}
