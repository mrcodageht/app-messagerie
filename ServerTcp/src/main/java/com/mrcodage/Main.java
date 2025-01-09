package com.mrcodage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws IOException {
        logger.fatal("Message fatal error");
        logger.error("Message d'error");
        logger.warn("Warning");
        logger.info("informations");
        logger.debug("Debugger");
    }
}
