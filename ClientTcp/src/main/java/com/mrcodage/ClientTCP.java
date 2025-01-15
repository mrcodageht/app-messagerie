package com.mrcodage;

import com.mrcodage.model.Message;
import com.mrcodage.model.UserToConnect;
import com.mrcodage.utilitaires.SynopsisCMD;
import com.mrcodage.utilitaires.UserMethodeInterface;

import javax.crypto.spec.PSource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.*;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class ClientTCP {
    static boolean FIRST_CONNECTION = true;
    static String username = "";

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

        HashMap<String,String> credentials = UserMethodeInterface.getUserIdentifiantConnection();
        String username = "";
        String password = "";

        for(Map.Entry<String,String> credential : credentials.entrySet()){
            username = credential.getKey();
            password = credential.getValue();
        }
        UserToConnect userToConnect = new UserToConnect(username,password);
        System.out.println("Parametres de connexion");
        System.out.println("username : "+username);
        System.out.println("Password : "+password);


        String clientId = generate_uuid("client");
        Message connectionMessage = new Message(username,userToConnect,CommandServer.CONNECT);
        out.writeObject(connectionMessage);

//        Lecture de la response du serveur
        var in = new ObjectInputStream(socket.getInputStream());
        Message messageLu = (Message) in.readObject();
        formatMessage(messageLu);
        System.exit(0);
        if(messageLu.getCode()==210){
            return false;
        } else if (messageLu.getCode() == 200) {
            return true;
        }
        return true;
    }

    private static void communicate(Socket socket) throws IOException,ClassNotFoundException{

            Runnable sendingThread = () -> {
                while (!socket.isClosed() || socket.isConnected()) {
                    try {
                        if (!socket.isClosed()) {
                            sendMessage(socket);
                            Thread.sleep(5000);
                        }
                    } catch (SocketException e) {
                        System.out.println("Hors ligne a " + OffsetDateTime.now().format(ISO_LOCAL_DATE));
                        break;
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            Runnable listenningThread = () -> {
                while (!socket.isClosed() || socket.isConnected()) {
                    try {
                        readMessage(socket);
                    } catch (SocketException e) {
                        System.out.println("Hors ligne a " + OffsetDateTime.now().format(ISO_LOCAL_DATE));
                        break;
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            new Thread(sendingThread).start();
            new Thread(listenningThread).start();
    }

    private static void readMessage(Socket socket) throws IOException, ClassNotFoundException {
        var in = new ObjectInputStream(socket.getInputStream());
        Message messageLu = (Message) in.readObject();
        if(messageLu.getCode() != null) {
            if (messageLu.getCode() == 210) {
                socket.close();
            } else if (messageLu.getCode() == 105) {
                socket.close();
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
            formatMessage(messageLu);
    }

    private static void sendMessage(Socket socket) throws IOException {
        var out = new ObjectOutputStream(socket.getOutputStream());
        Message message = readInputUserAndConstructMessage();
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
        }
        out.writeObject(message);
    }

    private static String generate_uuid(String username){
        var uuid = UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8));
        return String.valueOf(uuid);
    }

    private static void formatMessage(Message messageToFormat){
        System.out.printf("\n\t\t[%s] : %s : [%s]\n",messageToFormat.getSender(),messageToFormat.getContent(),messageToFormat.getDateTime().format(ISO_LOCAL_DATE));
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
