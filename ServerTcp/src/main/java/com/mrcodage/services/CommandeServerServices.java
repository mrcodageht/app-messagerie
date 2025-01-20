package com.mrcodage.services;

import com.mrcodage.CommandServer;
import com.mrcodage.InvalidUserConnectionException;
import com.mrcodage.UserNotFoundException;
import com.mrcodage.model.Message;
import com.mrcodage.utilitaires.AuthentificationServerServicesIntance;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CommandeServerServices {
    private final Map<CommandServer, CommandHandler>  commandHandlers = new HashMap<>();

    public CommandeServerServices(){
        commandHandlers.put(CommandServer.CONNECT,new ConnectCommandHandler());
        commandHandlers.put(CommandServer.LIST, new ListCommandHandler());
        commandHandlers.put(CommandServer.SENDTO,new SendToCommandHandler());
        commandHandlers.put(CommandServer.BROADCAST, new BroadcastCommandHandler());
        commandHandlers.put(CommandServer.QUIT, new QuitCommandHandler());
        commandHandlers.put(CommandServer.DEBUG, new DebugCommandHandler());

    }

    public CommandHandler getHandler(CommandServer commandServer){
        return commandHandlers.get(commandServer);
    }

}
