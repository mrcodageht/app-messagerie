package com.mrcodage.services;

import com.mrcodage.UserManage;
import com.mrcodage.UserNotFoundException;
import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class SendToCommandHandler implements CommandHandler{
    @Override
    public void execute(Message message, Socket socketClient, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        String[] infosCommand = message.getContent().split(" ",3);
        if(infosCommand.length == 3) {
            String userCleanSplit = infosCommand[1].substring(1,infosCommand[1].length()-1);
            String content = infosCommand[2].substring(1,infosCommand[2].length()-1);
            String userToChatId = UserManage.userTracker.get(userCleanSplit);
            if (vericateUser(userToChatId)) {
                writeObject(oos, new Message("server", "Demande de tchat avec id : " + userToChatId));
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
                writeObject(oos,messageTosend);
                messageTosend.setSender(uuidSender);
//                logger.info(messageTosend);
            } else {
                writeObject(oos, new Message("server", "Utilisateur non trouve",410));
            }
        }else{
            writeObject(oos, new Message("server", "Format de la commande incorrect : /sendto <nom d'utilisateur de la personne> <message a envoye>"));
        }
    }

    private void writeObject(ObjectOutputStream oos, Message message) throws IOException {
        oos.reset();
        oos.writeObject(message);
        oos.flush();
    }

    private boolean vericateUser(String idClient){
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
