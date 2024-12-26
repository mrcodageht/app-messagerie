package com.mrcodage.model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;

public class Message implements Serializable {

    private String sender;
    private String content;
    private OffsetDateTime dateTime;
    private String idMessage;
    private int code;
    private String commandStr;


    public Message(String sender, String content){
        this.sender = sender;
        this.content = content;
        this.dateTime = OffsetDateTime.now();
        this.code = 0;
    }

    public Message(String sender, String content, int code) {
        this.sender = sender;
        this.content = content;
        this.dateTime = OffsetDateTime.now();
        this.idMessage = generateIdMessage(content.isEmpty()?"":content);
        this.code = code;
    }

    public Message(String sender, String content,String commandStr){
        this.sender=sender;
        this.content = content;
        this.commandStr = commandStr;
    }

    private String generateIdMessage(String content){
        return String.valueOf(UUID.nameUUIDFromBytes(content.getBytes(StandardCharsets.UTF_8)));
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommandStr() {
        return commandStr;
    }

    public void setCommandStr(String commandStr) {
        this.commandStr = commandStr;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return """
                ================================================================
                Sender :"""+" "+this.sender+"""
                \nContenu :"""+" "+this.content+"""
                \nDate :"""+" "+this.dateTime.format(ISO_LOCAL_DATE)+"""
                \n================================================================
                """;
    }
}
