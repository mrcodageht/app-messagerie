package com.mrcodage.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class FileManagement {

    static final String _ROOT_ = System.getProperty("user.dir");
    private static final Logger logger = LogManager.getLogger(FileManagement.class);
    private File file;

    public FileManagement(String fileName){

    }

    public static void writeOnFile(File file, String data) throws IOException {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            out.write(data);
            out.newLine();
            logger.info("Donnee sauvegarder dans le fichier : {}",data);
        } catch (IOException e) {
            getLoggerError(e);
        }

    }

    private static void getLoggerError(Exception e) {
        logger.error("Un probleme est survenu : {}", e.getMessage());
    }

    public static void readOnFile(File file){

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            logger.debug("Lecture terminee");
        } catch (Exception e) {
            getLoggerError(e);
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
            getLoggerError(e);
        }

        return isFind;
    }

    public static void verifyIsFileExists(String fileName){
        final String pathNameFile=_ROOT_+ File.separator+"ServerTcp"+File.separator+fileName;
        boolean isExist=false;

        File file = new File(pathNameFile);
        if(!file.exists()){
            try {
                isExist=file.createNewFile();
                logger.warn("Le fichier '{}' a ete cree",file.getName());
            } catch (IOException e) {
                logger.error("Un probleme est survenu lors de la creation du fichier : {}",e.getMessage());

            }
        }
    }

    public static File getFile(String fileName){
        final String pathNameFile=_ROOT_+ File.separator+"ServerTcp"+File.separator+fileName;
        return new File(pathNameFile);
    }
}
