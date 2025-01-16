package com.mrcodage;

import com.mrcodage.model.Message;

import com.mrcodage.services.AuthentificationClientServices;

import com.mrcodage.utilitaires.SynopsisCMD;
import com.mrcodage.utilitaires.UserMethodeInterface;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import java.nio.charset.StandardCharsets;

import java.util.*;

public class ClientTCP {
    private static volatile boolean isRunning = true;
    static String username = "";

    public static void main(String[] args) {
        ClientManage client = new ClientManage();
        Scanner scanner = new Scanner(System.in);

        try {
            InetAddress ip = InetAddress.getByName("localhost"); // Adresse du serveur
            client.startConnection(ip, ServerTCP.PORT);

            System.out.println("Tapez 'exit' pour quitter.");

            while (true) {
                // Lire l'entrée utilisateur
                System.out.print("Vous: ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    client.stopConnection();
                    break;
                }

                // Créer un message à envoyer au serveur
                Message message = new Message("client", userInput);
                client.sendMessage(message);

                // Recevoir la réponse du serveur
                Message response = client.receiveMessage();
                System.out.println("Serveur: " + response.getContent());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur : " + e.getMessage());
        } finally {
            try {
                client.stopConnection();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    private static void sendMessage(Socket socket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        Message greetingMessage = new Message("client","hello server");
        oos.writeObject(greetingMessage);
        oos.flush();
    }

    private static void readMessage(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Message recu : "+(Message) ois.readObject());
    }

    private static boolean etablishConnection(Socket socket) throws IOException, ClassNotFoundException{
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        AuthentificationClientServices authentificationClientServices = new AuthentificationClientServices(ois,oos);
        HashMap<String,String> credentials = UserMethodeInterface.getUserIdentifiantConnection();
        return authentificationClientServices.isConnectionEtablished(credentials);
    }

    private static void communicate(Socket socket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        isRunning = true;
        System.out.println("Communicate");
        Thread sendingThread = new Thread(() -> {
            try {
                while (isRunning && !socket.isClosed()) {
                    sendMessage(oos);
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                handleError("Erreur dans sendingThread", e);
            } finally {
                stopCommunication(socket);
            }
        });

        Thread listeningThread = new Thread(() -> {
            try {
                while (isRunning && !socket.isClosed()) {
                    readMessage(socket, ois);
                }
            } catch (Exception e) {
                handleError("Erreur dans listeningThread", e);
            } finally {
                stopCommunication(socket);
            }
        });

        sendingThread.start();
        listeningThread.start();
    }

    private static void handleError(String messageError, Exception e) {
        System.err.println(messageError + ": " + e.getMessage());
        e.printStackTrace();
    }

    private static void stopCommunication(Socket socket) {
        isRunning = false;
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            handleError("Erreur lors de la fermeture du socket", e);
        }
    }


    private static void getClose(Socket socket) throws IOException {
        synchronized (socket) {
            socket.close();
        }
    }


    private static void readMessage(Socket socket,ObjectInputStream ois) throws IOException, ClassNotFoundException {
        System.out.println("Message lu");
        Message messageLu = (Message) ois.readObject();
        if(messageLu.getCode() != null) {
            if (messageLu.getCode() == 210) {
                getClose(socket);
            } else if (messageLu.getCode() == 105) {
                getClose(socket);
                return;
            }
        }

        if(messageLu.getCommand() != null){
            switch(messageLu.getCommand()) {
                case CommandServer.LIST-> {
                    int compteur = 1;
                    String[] usersConnectedList = messageLu.getContent().split("\\|");
                    System.out.println("Les utilisateurs connectes");
                    for (String userConnected : usersConnectedList) {
                        System.out.printf("%s - %s\n", compteur, userConnected);
                        compteur++;
                    }
                }
                case CommandServer.SENDTO -> {
                    System.out.println(messageLu.serializer());
                }
            }
        }else
            UserMethodeInterface.formatMessage(messageLu);
    }

    private static void sendMessage(ObjectOutputStream oos) throws IOException {
        System.out.println("Message envoye");
//        Message message = readInputUserAndConstructMessage();
        Message message = new Message("client","hello server");
        boolean isCorrect=false;

        if(message.getCommand() != null && message.getCommand() == CommandServer.SENDTO) {
            do {
                isCorrect = isMessageCmdCorrect(message);
                if(!isCorrect){
                    System.out.println("Erreur du formattage de la commande "+message.getCommand());
                    System.out.println(SynopsisCMD.getSysnopsisSendToCmd());
                    message = readInputUserAndConstructMessage();
                }
            }while(!isCorrect);
            synchronized (oos) {
                oos.reset();
                oos.writeObject(message);
                oos.flush();
            }
        }

    }

    private static String generate_uuid(String username){
        var uuid = UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8));
        return String.valueOf(uuid);
    }



    private static Message readInputUserAndConstructMessage(){
        List<String> commandUserList_str = List.of("/list","/quit","/sendto","/broadcast");
        String content="";
        Scanner sc = new Scanner(System.in);
        while(content.isBlank()) {
            System.out.println();
            System.out.print("Vous > ");
            content = sc.nextLine().trim();
        }
        String cmdFound = "";
        for(String cmd : commandUserList_str){
            int indexTarget = content.indexOf(cmd);
            if(indexTarget >= 0){
                cmdFound = content.substring(indexTarget,indexTarget+cmd.length());
                break;
            }
        }
        return cmdFound.isBlank()? new Message(generate_uuid(username),content): new Message(generate_uuid(username),content,getCommandeEnum(cmdFound));
    }

    private static CommandServer getCommandeEnum(String cmd_str){
        return switch (cmd_str) {
            case "/list" -> CommandServer.LIST;
            case "/quit" -> CommandServer.QUIT;
            case "/sendto" -> CommandServer.SENDTO;
            case "/broadcast" -> CommandServer.BROADCAST;
            default -> null;
        };
    }

    private static boolean isMessageCmdCorrect(Message message){
        boolean isCorrect = false;
        switch(message.getCommand()){
            case CommandServer.SENDTO -> {
                String[] message_split_str = message.getContent().split(" ",3);
                Arrays.stream(message_split_str).forEach(System.out::println);
                if(message_split_str.length==3){
                    isCorrect = checkChevronArgument(message_split_str[1]) && checkChevronArgument(message_split_str[2]);
                }
            }
        }
        return isCorrect;
    }

    private static boolean checkChevronArgument(String messageToCheck){
        return String.valueOf(messageToCheck.charAt(0)).equals("<")
                && String.valueOf(messageToCheck.charAt(messageToCheck.length()-1)).equals(">");
    }


}
