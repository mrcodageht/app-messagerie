package com.mrcodage;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public interface UserManage {
    Map<String, Socket> clientsConnected = new HashMap<String, Socket>();
    /**
     * Cle : nom d'utilisateur
     * Valeur : uuid de l'utilisateur
     * */
    Map<String,String> userTracker = new HashMap<>();


}
