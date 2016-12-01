package ch.sebooom.dump1090.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by seb on 13.11.16.
 */
public class Utils {

    private static Logger logger;

    public static void sleep (long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException("Utils.sleep throw exception",e);
        }
    }


    public static Logger getFileLogger(String fileNamePrefix, String className, boolean enableConsoleLog){

        Logger logger = Logger.getLogger(className);

        addFileHandler(logger,fileNamePrefix);

        if(enableConsoleLog){
            addConsoleHandler(logger);
        }

        return logger;
    }

    public static Logger getConsoleLogger(String className){

        Logger logger = Logger.getLogger(className);

        addConsoleHandler(logger);

        return logger;
    }

    private static SimpleDateFormat getDateFormater(){
        return new SimpleDateFormat("_dd-MM-yyyy_HHmmss");
    }


    private static void addConsoleHandler(Logger logger){
        ConsoleHandler ch = null;

        SimpleDateFormat format = getDateFormater();
        try {
            ch = new ConsoleHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ch.setFormatter(new LogFormatter());
        logger.addHandler(ch);
    }

    private static void addFileHandler(Logger logger, String logFilePrefix){
        File file = new File("logs");
        FileHandler fh = null;


        if (!file.exists()) {
            if (file.mkdir()) {
                logger.info("Directory logs created: " + file.getAbsolutePath());
            }
        }

        SimpleDateFormat format = getDateFormater();
        try {
            fh = new FileHandler("logs/" + logFilePrefix
                    + format.format(Calendar.getInstance().getTime()) + ".log");

        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new LogFormatter());

        logger.addHandler(fh);

    }





}
