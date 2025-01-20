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
}
