package com.mrcodage.model;

import com.mrcodage.CommandServer;
import com.mrcodage.utilitaires.Serialization;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;

public class Message implements Serializable, Serialization {
    @Serial
    private static final long serialVersionUID = 1L;
    private String sender;
    private String content;
    private OffsetDateTime dateTime;
    private String idMessage;
    private Integer code;
    private CommandServer cmd;
    private UserToConnect userToConnect;


    public Message(String sender, String content){
        this.sender = sender;
        this.content = content;
        this.dateTime = OffsetDateTime.now();
        this.idMessage = generateIdMessage(content.isEmpty()?"":content);
        this.code = 0;
    }

    public Message(String sender, String content, Integer code) {
        this.sender = sender;
        this.content = content;
        this.dateTime = OffsetDateTime.now();
        this.idMessage = generateIdMessage(content.isEmpty()?this.sender+this.dateTime:content);
        this.code = code==null?0:code;
    }

    public Message(String sender, String content, CommandServer cmd){
        this.sender=sender;
        this.content = content;
        this.idMessage = generateIdMessage(content.isEmpty()?"":content);
        this.dateTime = OffsetDateTime.now();
        this.cmd = cmd;
    }

    public Message(String sender,UserToConnect userToConnect,CommandServer cmd){
        this.sender = sender;
        this.userToConnect = userToConnect;
        this.dateTime = OffsetDateTime.now();
        this.cmd = cmd;
    }

    private String generateIdMessage(String content){
        String chaine = content+OffsetDateTime.now();
        return String.valueOf(UUID.nameUUIDFromBytes(chaine.getBytes(StandardCharsets.UTF_8)));
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

    public CommandServer getCommand() {
        return cmd;
    }

    public void setCommand(CommandServer cmd) {
        this.cmd = cmd;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    @Override
    public String serializer() {

        StringBuilder sb = new StringBuilder();
        sb.append("idMessage : ");
        sb.append(this.idMessage == null ? "" : this.idMessage);
        sb.append(" || idSender : ");
        sb.append(this.sender);
        sb.append(" ");
        sb.append(this.cmd == null ? "" : "|| cmd : "+this.cmd);
        sb.append(" ");
        sb.append(this.code == null ? "" : "|| code : "+this.code);
        sb.append(" || date : ");
        sb.append(this.dateTime);
        return sb.toString();
    }

    public UserToConnect getUserToConnect() {
        return userToConnect;
    }


    //    @Override
//    public String toString() {
//        return "Message{" +
//                "sender='" + sender + '\'' +
//                ", content='" + content + '\'' +
//                ", dateTime=" + dateTime +
//                ", idMessage='" + idMessage + '\'' +
//                ", code=" + code +
//                ", cmd=" + cmd +
//                '}';
//    }
}
