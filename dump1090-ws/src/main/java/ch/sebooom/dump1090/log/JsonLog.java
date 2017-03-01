package ch.sebooom.dump1090.log;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
import com.google.gson.JsonObject;

/**
 * Created by sce on 23.02.2017.
 */
public class JsonLog {


    public final String correlationId;
    public final String msg;
    public final LogType msgType;
    public final EventType eventType;
    public final long durationTime;



    private JsonLog(String msg, LogType type, EventType eventType,long durationTime){
        this.msg = msg;
        this.msgType = type;
        this.eventType = eventType;
        this.correlationId = "";    //no correlation id for technical
        this.durationTime = durationTime;
    }

    private JsonLog(String msg, LogType type, String correlationId,EventType eventType){
        this.msg = msg;
        this.msgType = type;
        this.eventType = eventType;
        this.correlationId = correlationId;
        this.durationTime = 0;
    }

    public String toJson(){
        JsonObject obj = new JsonObject();
        obj.addProperty("msgType",msgType.toString());
        obj.addProperty("msg",msg);
        obj.addProperty("correlationId",correlationId);
        obj.addProperty("eventType",eventType.toString());
        obj.addProperty("durationTime",durationTime);
        return obj.toString();
    }

    public static String technical(String msg, EventType eventType,long durationTime){

        return new JsonLog(msg,LogType.TECHNICAL,eventType,durationTime).toJson();
    }

    public static String domain(String msg, String correlationId, EventType eventType){
        return new JsonLog(msg,LogType.DOMAIN,correlationId,eventType).toJson();
    }




}