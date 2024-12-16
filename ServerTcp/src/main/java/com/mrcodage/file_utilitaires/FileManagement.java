package com.mrcodage.file_utilitaires;

import java.io.*;

public abstract class FileManagement {

    static final String _ROOT_ = System.getProperty("user.dir");

    public static void writeOnFile(File file, String data) throws IOException {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            out.write(data);
            out.newLine();
            System.out.println("Data sauvegarder dans le fichier");
        } catch (IOException e) {
            System.out.println("Un probleme est survenue");
            e.printStackTrace();
        }

    }

    public static void readOnFile(File file){

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("Termine la lecture");
        } catch (Exception e) {
            System.out.println("Un probleme est survenu lors de l'operation");
            e.printStackTrace();
        }

    }

    public static boolean findDataOnFile(File file, String dataToFind){
        boolean isFind = false;

        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = read.readLine()) != null) {
                isFind = (line.equals(dataToFind));
                if (isFind)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isFind;
    }

    public static boolean verifyIsFileExists(String fileName){
        final String pathNameFile=_ROOT_+ File.separator+"ServerTcp"+File.separator+fileName;
        boolean isExist=false;

        File file = new File(pathNameFile);
        if(!file.exists()){
            try {
                isExist=file.createNewFile();
                System.out.println("Le fichier a ete creer");
            } catch (IOException e) {
                System.out.println("Erreur lors de la creation du fichier");
                e.printStackTrace();
            }
        }else {
            System.out.println("Le fichier existe deja");
        }
        return isExist;
    }

    public static File getFile(String fileName){
        final String pathNameFile=_ROOT_+ File.separator+"ServerTcp"+File.separator+fileName;

        return new File(pathNameFile);
    }
}
