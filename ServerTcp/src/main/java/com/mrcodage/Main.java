package com.mrcodage;

import com.mrcodage.file_utilitaires.FileManagement;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Main {
    static final String _ROOT_ = System.getProperty("user.dir");
    public static void main(String[] args) throws IOException {
        final String pathNameFile=_ROOT_+ File.separator+"ServerTcp"+File.separator+"fichierText.txt";

        File file = new File(pathNameFile);
        if(!file.exists()){
            try {
                boolean isCreate=file.createNewFile();
                System.out.println("Le fichier a ete creer");
            } catch (IOException e) {
                System.out.println("Erreur lors de la creation du fichier");
                e.printStackTrace();
            }
        }else {
            System.out.println("Le fichier existe deja");
        }
//        FileManagement.writeOnFile(file, String.valueOf(UUID.nameUUIDFromBytes("Patrice".getBytes(StandardCharsets.UTF_8))));
//        FileManagement.readOnFile(file);
        if(FileManagement.findDataOnFile(file,String.valueOf(UUID.nameUUIDFromBytes("Bob".getBytes(StandardCharsets.UTF_8))))){
            System.out.println("C'est trouve");
        }else{
            System.out.println("Non trouve");
        }
    }


}
