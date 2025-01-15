package com.mrcodage.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Account{
    private String username;
    private String salt;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date created_at;

    public Account(String username, String salt, String password) {
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.created_at = new Date();
    }

    public Account() {
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", salt='" + salt + '\'' +
                ", password='" + password + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
