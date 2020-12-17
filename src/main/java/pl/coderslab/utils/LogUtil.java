package pl.coderslab.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogUtil {

    //instancja loggera
    private static Logger instance;

    //metoda upewniająca się, że logger jest stworzony tylko raz
    public static Logger getLogger() throws IOException {
        if (instance == null) {
            synchronized (LogUtil.class) {
                if (instance == null) {
                    instance = getInstance();
                }
            }
        }
        return instance;
    }

    //metoda tworząca nowego loggera
    private static Logger getInstance() throws IOException {
        File outputFile;
        FileHandler fileHandler;


        outputFile = new File("log.txt");
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }

        fileHandler = new FileHandler("log.txt",true);

        Logger logger = Logger.getLogger("logger");
        logger.addHandler(fileHandler);

        logger.info("Created Logger");

        return logger;
    }
}
