package com.mrcodage;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        var uuid = UUID.randomUUID();
        System.out.println(uuid);

        var uuidSplit = String.valueOf(uuid).split("-");
        for(String id : uuidSplit){
            System.out.println(id);
        }
    }
}
