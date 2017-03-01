package ch.sebooom.dump1090.log;

import com.google.gson.GsonBuilder;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

;

/**
 * Created by sce on 23.02.2017.
 */
public class JsonLogFormatter extends Formatter{

    GsonBuilder gsonBuilder = new GsonBuilder();



    @Override
    public String format(LogRecord record) {

        gsonBuilder.registerTypeAdapter(LogRecord.class, new JsonLogAdapter());//.setPrettyPrinting();

        return new StringBuilder(gsonBuilder.create().toJson(record)).append("\n").toString();
    }
}