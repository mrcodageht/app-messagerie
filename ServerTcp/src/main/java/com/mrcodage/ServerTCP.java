package com.mrcodage;

import com.mrcodage.file_utilitaires.FileManagement;
import com.mrcodage.model.Message;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerTCP {
    static int PORT = 9360;

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

                Thread threadCheckingConnection = new Thread(ServerTCP::checkIsUserConnected);
                threadCheckingConnection.start();
                Thread.sleep(5000);

            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
        Message message = clientInput(socketClient);

        if(message.getCommand() != null) {
            switch (message.getCommand()) {
                case CommandServer.LIST -> commandUser(CommandServer.LIST, socketClient, message);
                case CommandServer.SENDTO -> commandUser(CommandServer.SENDTO, socketClient, message);
                case CommandServer.BROADCAST -> commandUser(CommandServer.BROADCAST, socketClient, message);
                case CommandServer.QUIT -> {
                    Message messageDeconnection = new Message("server", "Vous etes offline", 105);
                    sendMessageCommand(socketClient, messageDeconnection);
                    socketClient.close();
                }
            }
        }else
            System.out.println(message);
    }

    private static Message clientInput(Socket socketClient)throws IOException, ClassNotFoundException{
        var in = new ObjectInputStream(socketClient.getInputStream());
       return (Message) in.readObject();
    }

    private static void commandUser(CommandServer cmd, Socket socketClient,Message message) throws IOException, ClassNotFoundException {
        switch (cmd){
            case CommandServer.LIST -> {
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
                sendMessageCommand(socketClient, msg);
            }
            case CommandServer.SENDTO->{
                String[] infosCommand = message.getContent().split(" ",3);
                if(infosCommand.length == 3) {
                    String userCleanSplit = infosCommand[1].substring(1,infosCommand[1].length()-1);
                    String userToChatId = UserManage.userTracker.get(userCleanSplit);
                    if (vericateUser(userToChatId)) {
                        sendMessageCommand(socketClient, new Message("server", "Demande de tchat avec id : " + userToChatId));
                    } else {
                        sendMessageCommand(socketClient, new Message("server", "Utilisateur non trouve",410));
                    }
                }else{
                    sendMessageCommand(socketClient, new Message("server", "Format de la commande incorrect : /sendto <nom d'utilisateur de la personne> <message a envoye>"));
                }
            }
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
                UserManage.clientsConnected.put(message.getContent(),socketClient);
                UserManage.userTracker.put(message.getSender(), message.getContent());

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
        if(!UserManage.clientsConnected.isEmpty()){
            for(Map.Entry<String,Socket> client : UserManage.clientsConnected.entrySet() ){
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

    private static void checkIsUserConnected(){
        if(UserManage.clientsConnected.isEmpty()) {
            System.out.println("Aucun client connecte");
            return;
        }
        Lock verrou = new ReentrantLock();
        verrou.lock();
        try {
            for (Map.Entry<String, Socket> map : UserManage.clientsConnected.entrySet()) {
                Socket socketClient = map.getValue();
                String idUser = map.getKey();

                if (socketClient.isClosed() || !socketClient.isConnected()) {
                    System.out.println("L'utilisateur avec l'id : " + idUser + " n'est pas connecte");
                    UserManage.clientsConnected.remove(idUser);
                }
            }
        }finally {
            verrou.unlock();
        }
    }
}
