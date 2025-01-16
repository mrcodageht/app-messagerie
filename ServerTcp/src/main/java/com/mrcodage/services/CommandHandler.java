package com.mrcodage.services;

import com.mrcodage.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public interface CommandHandler {
    void execute(Message message, Socket socketClient, ObjectInputStream ois, ObjectOutputStream oos) throws IOException;
}
