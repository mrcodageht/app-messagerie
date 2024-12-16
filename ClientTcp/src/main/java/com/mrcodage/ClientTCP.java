package com.mrcodage;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

public class ClientTCP {
    static boolean FIRST_CONNECTION = true;
    public static void main(String[] args) {
        try{
            var server = InetAddress.getByName("localhost");
            var socket = new Socket(server,ServerTCP.PORT);

            if(etablishConnection(socket)) {
                communicate(socket);
            }else{
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean etablishConnection(Socket socket) throws IOException, ClassNotFoundException{
        var out = new ObjectOutputStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez votre username de connection");
        System.out.print("username %> ");
        String username = sc.nextLine();
        String clientId = generate_uuid(username);
        Message connectionMessage = new Message(username,clientId,100);
        out.writeObject(connectionMessage);

//        Lecture de la response du serveur
        var in = new ObjectInputStream(socket.getInputStream());
        Message messageLu = (Message) in.readObject();
        System.out.println(messageLu);
        if(messageLu.getCode()==210){
            return false;
        } else if (messageLu.getCode() == 200) {
            return true;
        }
        return true;
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
        if(messageLu.getCode()==210){
            socket.close();
        }
        System.out.println(messageLu);
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

    private static String generate_uuid(String username){
        var uuid = UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8));
        return String.valueOf(uuid);
    }
}
