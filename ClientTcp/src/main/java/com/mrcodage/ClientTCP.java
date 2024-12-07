package com.mrcodage;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientTCP {
    public static void main(String[] args) {
        try{
            var server = InetAddress.getByName("localhost");
            var socket = new Socket("10.0.x.x",ServerTCP.PORT);

            while(true) {
                sendMessage(socket);
                readMessage(socket);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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

        Message message = new Message("Server",content);
        out.writeObject(message);
    }
}
