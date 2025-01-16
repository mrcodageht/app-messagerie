package com.mrcodage;

import com.mrcodage.model.Message;
import com.mrcodage.services.CommandHandler;
import com.mrcodage.services.CommandeServerServices;
import com.mrcodage.utilitaires.AuthentificationServerServicesIntance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class ServerTCP {
    static int PORT = 9360;
    private static volatile boolean isRunning = true;
    private static final Logger logger = LogManager.getLogger(ServerTCP.class);
    public static final CommandeServerServices commandServices = new CommandeServerServices();

    public static void main(String[] args) throws IOException {
        ServerManage serverManage = new ServerManage();
        serverManage.start(PORT);
    }

    private static void getErrorLog(Exception e) {
        logger.error("Une error s'est produite : ",e);
    }

    private static void processClient(Socket socketClient) throws IOException, ClassNotFoundException {
        logger.info("Connexion avec le client : {}", socketClient.getInetAddress());
        socketClient.setSoTimeout(5000);
        readClientInput(socketClient);
        sendMessage(new ObjectOutputStream(socketClient.getOutputStream()),new Message("server","Message recu"));
    }

    private static void closeSocket(Socket socket) {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            logger.error("Erreur lors de la fermeture du socket : {}", e.getMessage());
        }
    }


    private static void sendMessage(ObjectOutputStream oos, Message message) throws IOException{
        oos.reset();
        oos.writeObject(message);
        oos.flush();
    }

    private static void readClientInput(Socket socketClient) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(socketClient.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socketClient.getInputStream());
        try {
            Object obj = ois.readObject();

            if (obj == null) {
                System.err.println("L'object est null, abandon de la lecture");
                return;
            }

            if (obj instanceof Message message) {
                logger.debug("message non null");
                if (message.getCommand() != null) {
                    logger.debug("Le message est une commande du serveur : {}", message.getCommand());
                    CommandHandler handler = commandServices.getHandler(message.getCommand());
                    handler.execute(message,socketClient,ois,oos);
                }else{
                    logger.debug("Message recu : {}\n",message);
                    Message greetingMessage = new Message("serveur","hello client");
//                    sendMessage(oos,greetingMessage);
                }
            }else{
                System.err.println("Type d'objet inattendu recu");
            }
        } catch (ClassCastException e) {
            System.err.println("Erreur de type lors du casting : " + e.getMessage());
        } catch (EOFException e) {
            System.err.println("Fin de flux atteinte, connexion client fermée.");
        } catch (StreamCorruptedException e) {
            System.err.println("Flux corrompu : " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la lecture : " + e.getMessage());
        }
    }
}
