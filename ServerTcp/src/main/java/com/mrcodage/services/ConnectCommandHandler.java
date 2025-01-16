package com.mrcodage.services;

import com.mrcodage.InvalidUserConnectionException;
import com.mrcodage.UserNotFoundException;
import com.mrcodage.model.Message;
import com.mrcodage.utilitaires.AuthentificationServerServicesIntance;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class ConnectCommandHandler implements CommandHandler {
    @Override
    public void execute(Message message, Socket socketClient, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        try {
            // Vérification si l'utilisateur est valide
            boolean isUserValid = AuthentificationServerServicesIntance.getInstance().isUserToConnectValid(message.getUserToConnect());

            if (isUserValid) {
                if (!socketClient.isClosed() && socketClient.isConnected()) {
                    System.out.println("Utilisateur valide");
                    Message msg = new Message("server", "Connection approuvee", 200);
                    System.out.println(message.serializer());
                    writeObject(oos,msg);
                }
            }
        } catch (UserNotFoundException e) {
            System.err.println("Utilisateur non trouvé : " + e.getMessage());
            if (!socketClient.isClosed() && socketClient.isConnected()) {
                writeObject(oos,new Message("server", e.getMessage(), 210));
            }
        } catch (InvalidUserConnectionException e) {
            System.err.println("Utilisateur invalide : " + e.getMessage());
            if (!socketClient.isClosed() && socketClient.isConnected()) {
                writeObject(oos,new Message("server", "Identifiants de connexion invalide", 210));
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Erreur socket : " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Problème lors de l'exécution de la commande de connexion", e);
        } finally {
            // Ajout d'un message pour confirmer la fin de la méthode
            System.out.println("Fin de la méthode execute de ConnectCommandHandler");
        }
    }

    private void writeObject(ObjectOutputStream oos, Message message) throws IOException {
        oos.reset();
        oos.writeObject(message);
        oos.flush();
    }
}
