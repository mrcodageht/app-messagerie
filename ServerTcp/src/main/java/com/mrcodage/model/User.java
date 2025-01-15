package com.mrcodage.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.Date;

public class User {
    private String firstname;
    private String lastname;
    private Account account;

    public User(String firstname, String lastname, Account account) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public  User(){}

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", account=" + account.toString() +
                '}';
    }
}
