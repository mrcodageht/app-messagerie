package com.mrcodage;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public interface UserManage {
    Map<String, Socket> clientsConnected = new HashMap<String, Socket>();
    Map<String,String> userTracker = new HashMap<>();
}
