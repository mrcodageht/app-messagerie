package com.mrcodage;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
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
        }catch(SocketException esx){
            System.out.println("Vous etes deconnecte");
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
        Runnable Thread = ()->{
            while (!socket.isClosed()) {
                try {
                    sendMessage(socket);
                    readMessage(socket);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        };
        Thread.run();

    }

    private static void readMessage(Socket socket) throws IOException, ClassNotFoundException {
        var in = new ObjectInputStream(socket.getInputStream());
        Message messageLu = (Message) in.readObject();
        if(messageLu.getCode()==210){
            socket.close();
        } else if (messageLu.getCode()==105) {
            socket.close();
            return;
        }
        if(messageLu.getCommandStr() != null){
            switch(messageLu.getCommandStr().trim()){
                case "/list":
                    int compteur =1;
                    String[] usersConnectedList = messageLu.getContent().split("\\|");
                    System.out.println("Les utilisateurs connectes");
                    for(String userConnected : usersConnectedList){
                        System.out.printf("%s - %s\n",compteur,userConnected);
                        compteur++;
                    }
                    break;
            }
        }else
            System.out.println(messageLu);
    }

    private static void sendMessage(Socket socket) throws IOException {
        var out = new ObjectOutputStream(socket.getOutputStream());
        String content="";
        Scanner sc = new Scanner(System.in);

        while(content.isBlank()) {
            System.out.print("Client > ");
            content = sc.nextLine();
        }

        Message message = new Message("client",content);
        out.writeObject(message);
    }

    private static String generate_uuid(String username){
        var uuid = UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8));
        return String.valueOf(uuid);
    }
}
