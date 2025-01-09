package com.mrcodage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Traitement implements Runnable{
    private static final Logger logger = LogManager.getLogger(Traitement.class);
    private static boolean firstCheck = true;
    private static Long numberUserConnected=0L;
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(5000);
                checkIsUserConnected();
            } catch (InterruptedException e) {
                logger.fatal(e.getMessage());
            }
        }
    }

    private static void checkIsUserConnected(){
        if(firstCheck || numberUserConnected != (long) UserManage.clientsConnected.size()) {
            logger.info("Nombre clients connectes : {}", (long) UserManage.clientsConnected.size());
            numberUserConnected = (long) UserManage.clientsConnected.size();
            firstCheck=false;
        }
        if(UserManage.clientsConnected.isEmpty()) {
            return;
        }
        Lock verrou = new ReentrantLock();
        verrou.lock();
        try {
            System.out.println("Verification socket en cours...");
            for (Map.Entry<String, Socket> map : UserManage.clientsConnected.entrySet()) {
                Socket socketClient = map.getValue();
                String idUser = map.getKey();

                if (socketClient.isClosed() || !socketClient.isConnected()) {
                    logger.info("Utilisateur deconnecte uuid : {}",idUser);
                    UserManage.clientsConnected.remove(idUser);
                    UserManage.userTracker.entrySet().removeIf(user->user.getValue().equals(idUser));
                }
            }
        }finally {
            verrou.unlock();
        }
    }
}
