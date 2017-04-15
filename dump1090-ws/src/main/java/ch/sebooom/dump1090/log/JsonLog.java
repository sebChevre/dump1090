package ch.sebooom.dump1090.log;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */

import com.google.gson.JsonObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sce on 23.02.2017.
 */
public class JsonLog {


    public final String correlationId;
    public final String msg;
    public final LogType msgType;
    public final EventType eventType;
    public final String logTime;
    public final String threadName;
    public static final String EMPTY_CORRELATION_ID = "EMPTY";
    public static final String CORRELATION_ID_ERROR = "ERROR";
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";



    private JsonLog(String msg, LogType type, EventType eventType,String correlationId){
        this.msg = msg;
        this.msgType = type;
        this.eventType = eventType;
        this.correlationId = correlationId;
        this.logTime = logDate();
        this.threadName = Thread.currentThread().getName();
    }


    private String logDate(){
        Format formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(new Date());
    }

    public String toJson(){
        JsonObject obj = new JsonObject();
        obj.addProperty("msgType",msgType.toString());
        obj.addProperty("msg",msg);
        obj.addProperty("correlationId",correlationId);
        obj.addProperty("eventType",eventType.toString());
        obj.addProperty("logTime",logTime);
        obj.addProperty("threadName",threadName);
        return obj.toString();
    }

    public static String log(String msg, EventType eventType,String correlationId){
        return new JsonLog(msg,eventType.logType(),eventType,correlationId).toJson();
    }

}