package com.mrcodage;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        String username = "Wesner";

        var uuid = UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8));
        System.out.println(uuid);

        String chaine = "<destinatare>";


        System.out.println(String.valueOf(chaine.charAt(chaine.length()-1)).equals(">")?"Egale": "pas egale");

    }
}
