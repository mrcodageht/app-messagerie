package com.mrcodage;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManage {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Lancement du serveur sur le port " + port);

        while (true) {
            // Accepter une connexion client
            Socket socketClient = serverSocket.accept();
            System.out.println("Nouvelle connexion client : " + socketClient.getInetAddress());

            // Démarrer un nouveau thread pour gérer le client
            new ClientHandler(socketClient).start();
        }
    }

    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    // Classe interne pour gérer chaque client
    private static class ClientHandler extends Thread {
        private Socket socketClient;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        public ClientHandler(Socket socketClient) {
            this.socketClient = socketClient;
        }

        @Override
        public void run() {
            try {
                // Configurer les flux de communication
                oos = new ObjectOutputStream(socketClient.getOutputStream());
                ois = new ObjectInputStream(socketClient.getInputStream());

                // Communication avec le client
                while (true) {
                    Message messageClient = (Message) ois.readObject();
                    System.out.println("Message reçu de " + messageClient.getSender() + ": " + messageClient.getContent());

                    // Répondre au client
                    if (messageClient.getContent().equalsIgnoreCase("hello server")) {
                        oos.writeObject(new Message("server", "Welcome to MrCodage Server"));
                    } else {
                        oos.writeObject(new Message("server", "Unrecognized message"));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur avec le client : " + e.getMessage());
            } finally {
                try {
                    if (ois != null) ois.close();
                    if (oos != null) oos.close();
                    if (socketClient != null) socketClient.close();
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
                }
            }
        }
    }
}
