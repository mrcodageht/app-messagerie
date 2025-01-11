package com.mrcodage;

import com.mrcodage.data.FileManagement;
import com.mrcodage.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Map;

public class ServerTCP {
    static int PORT = 9360;
    private static final Logger logger = LogManager.getLogger(ServerTCP.class);
    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            logger.debug("----------------------");
            logger.warn("Lancement du serveur");
            logger.debug("----------------------");
            new Thread(new Traitement()).start();
            while(true){
                Socket socketClient = serverSocket.accept();
                Runnable socketThread = ()->{
                    try {
                        processThread(socketClient);
                    } catch (IOException | ClassNotFoundException e) {
                        logger.error("Une erreur s'est produite : {}",String.valueOf(e));
                    }
                };
                new Thread(socketThread).start();
            }
        } catch (Exception e) {
            logger.error("Une erreur s'est produite : {}",String.valueOf(e));
        }
    }

    private static void processThread(Socket socketClient) throws IOException, ClassNotFoundException {
        logger.debug("-------------------------------------------------");
        logger.info("Connexion avec : {}",socketClient.getInetAddress());
        logger.debug("-------------------------------------------------");
        requestConnectionClient(socketClient);
        while(socketClient.isConnected()) {
            readClientInput(socketClient);
            sendResponseToClient(socketClient);
        }
    }

    private static void sendResponseToClient(Socket socketClient) throws IOException {
        Message serverMessage = new Message("server","Message bien recu");
        sendMessage(socketClient,serverMessage);
    }

    private static void sendMessage(Socket socketClient, Message message) throws IOException{
        var out = new ObjectOutputStream(socketClient.getOutputStream());
        out.writeObject(message);
    }

    private static void sendResponseRequestConnection(Socket socketClient, String message,int code) throws IOException{
        Message responseConnection = new Message("server",message,code);
        sendMessage(socketClient,responseConnection);
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
                    sendMessage(socketClient, messageDeconnection);
                    String uuid = "";
                    for(Map.Entry<String, Socket> entry:UserManage.clientsConnected.entrySet()){
                        if(entry.getValue()==socketClient){
                            uuid=entry.getKey();
                            break;
                        }
                    }
                    logger.warn("Deconnection du client avec le uuid : {}",uuid);
                    socketClient.close();
                }
            }
        }else
            logger.info(message.serializer());
    }

    private static Message clientInput(Socket socketClient)throws IOException, ClassNotFoundException{
        var in = new ObjectInputStream(socketClient.getInputStream());
       return (Message) in.readObject();
    }

    private static void commandUser(CommandServer cmd, Socket socketClient,Message message) throws IOException, ClassNotFoundException {
        logger.info("commande de l'utilisateur : {}",cmd);
        logger.info(message.serializer());
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
                sendMessage(socketClient, msg);
            }
            case CommandServer.SENDTO->{
                String[] infosCommand = message.getContent().split(" ",3);
                if(infosCommand.length == 3) {
                    String userCleanSplit = infosCommand[1].substring(1,infosCommand[1].length()-1);
                    String content = infosCommand[2].substring(1,infosCommand[2].length()-1);
                    String userToChatId = UserManage.userTracker.get(userCleanSplit);
                    if (vericateUser(userToChatId)) {
                        sendMessage(socketClient, new Message("server", "Demande de tchat avec id : " + userToChatId));
                        Socket socketToSendMessage = UserManage.clientsConnected.get(userToChatId);
                        if(socketToSendMessage == null){
                            throw new UserNotFoundException(userToChatId);
                        }
                        String uuidSender = UserManage.clientsConnected.entrySet().stream()
                                .filter((us)->us.getValue()==socketClient)
                                .findFirst().get().getKey();
                        String usernameSender = UserManage.userTracker.entrySet().stream()
                                .filter(us->us.getValue().equals(uuidSender))
                                .findFirst().get().getKey();
                        Message messageTosend = new Message(usernameSender,content);
                        sendMessage(socketToSendMessage,messageTosend);
                        messageTosend.setSender(uuidSender);
                        logger.info(messageTosend);
                    } else {
                        sendMessage(socketClient, new Message("server", "Utilisateur non trouve",410));
                    }
                }else{
                    sendMessage(socketClient, new Message("server", "Format de la commande incorrect : /sendto <nom d'utilisateur de la personne> <message a envoye>"));
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
            logger.debug("--------------------------------------------------------------");
            logger.info(message.serializer());
            logger.debug("--------------------------------------------------------------");
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


}
