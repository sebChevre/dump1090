package ch.sebooom.adsb.utils;

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


    public static Logger getLogger(String logName, String className){


        Logger logger = Logger.getLogger(className);
        File file = new File("logs");
        FileHandler fh = null;
        ConsoleHandler ch = null;

        if (!file.exists()) {
            if (file.mkdir()) {
                logger.info("Directory logs created: " + file.getAbsolutePath());
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("_dd-MM-yyyy_HHmmss");
        try {
            fh = new FileHandler("logs/out_" + logName
                    + format.format(Calendar.getInstance().getTime()) + ".log");
            ch = new ConsoleHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new LogFormatter());
        ch.setFormatter(new LogFormatter());
        logger.addHandler(ch);
        logger.addHandler(fh);

        return logger;
    }
}
