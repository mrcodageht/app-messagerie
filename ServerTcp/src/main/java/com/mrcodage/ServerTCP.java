package com.mrcodage;

import com.mrcodage.file_utilitaires.FileManagement;
import com.mrcodage.model.Message;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ServerTCP {
    static int PORT = 9360;
    static Map<String,Socket> clientsConnected = new HashMap<String, Socket>();
    static Map<String,String> userTracker = new HashMap<>();

    public static void main(String[] args) throws IOException {

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("----------------------");
            System.out.println("Lancement du serveur");
            System.out.println("----------------------");

            while(true){
                Socket socketClient = serverSocket.accept();

                Runnable socketThread = ()->{
                    try {
                        processThread(socketClient);
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                };
                new Thread(socketThread).start();
            }

        }
    }

    private static void processThread(Socket socketClient) throws IOException, ClassNotFoundException {
        System.out.println("-------------------------------------------------");
        System.out.println("Connexion avec : "+socketClient.getInetAddress());
        System.out.println("-------------------------------------------------");
        System.out.println();

        requestConnectionClient(socketClient);


        while(socketClient.isConnected()) {
            readClientInput(socketClient);
            sendResponseToClient(socketClient);
        }
    }

    private static void sendResponseToClient(Socket socketClient) throws IOException {
        var out = new ObjectOutputStream(socketClient.getOutputStream());
        Message serverMessage = new Message("server","Message bien recu");
        out.writeObject(serverMessage);
    }

    private static void sendMessageCommand(Socket socketClient, Message message) throws IOException{
        var out = new ObjectOutputStream(socketClient.getOutputStream());
        out.writeObject(message);
    }

    private static void sendResponseRequestConnection(Socket socketClient, String message,int code) throws IOException{
        var out = new ObjectOutputStream(socketClient.getOutputStream());
        Message responseConnection = new Message("server",message,code);
        out.writeObject(responseConnection);
    }

    private static void readClientInput(Socket socketClient) throws IOException, ClassNotFoundException {
        var in = new ObjectInputStream(socketClient.getInputStream());
        Message message = (Message) in.readObject();
        switch (message.getContent().toLowerCase(Locale.ROOT).trim()){
            case "/list":
                commandUser("/list",socketClient);
                break;
            default:
                System.out.println(message);

        }


    }

    private static void commandUser(String command_str, Socket socketClient) throws IOException {
        switch (command_str){
            case "/list":
                StringBuilder sb = new StringBuilder();
                for(String userName : userTracker.keySet()){
                    sb.append(userName).append("\n");
                }
                Message message = new Message("server", sb.toString());
                sendMessageCommand(socketClient,message);
                break;
        }
    }

    private static void requestConnectionClient(Socket socketClient) throws IOException, ClassNotFoundException{
        var in = new ObjectInputStream(socketClient.getInputStream());
        Message message = (Message) in.readObject();

        if(message.getCode()==100){
            if(!vericateUser(message.getContent())) {

                File file = FileManagement.getFile("user-data.txt");
                FileManagement.verifyIsFileExists(file.getName());
                if(!FileManagement.findDataOnFile(file, message.getContent())){
                    FileManagement.writeOnFile(file,message.getContent());
                }
                sendResponseRequestConnection(socketClient,"approuve",200);
                clientsConnected.put(message.getContent(),socketClient);
                userTracker.put(message.getSender(), message.getContent());

            }else{
                sendResponseRequestConnection(socketClient,"Ce client est deja connecte",210);
                socketClient.close();
            }

        }else{
            System.out.println("--------------------------------------------------------------");
            System.out.println("Message du client : "+message);
            System.out.println("--------------------------------------------------------------");
        }
    }

    public static boolean vericateUser(String idClient){
        if(!clientsConnected.isEmpty()){
            for(Map.Entry<String,Socket> client : clientsConnected.entrySet() ){
                String id = client.getKey();
                if(id.equals(idClient)){
                    return true;
                }
            }
        }else{
            return false;
        }
        return false;
    }
}
