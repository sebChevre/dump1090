package ch.sebooom.dump1090.log;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.logging.LogRecord;

/**
 * Created by sce on 23.02.2017.
 */
public class JsonLogAdapter implements JsonSerializer<LogRecord>{

    GsonBuilder gsonBuilder = new GsonBuilder();

    @Override
    public JsonElement serialize(LogRecord logRecord, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("level",logRecord.getLevel().toString());

        JsonLog log = gsonBuilder.create().fromJson(logRecord.getMessage(), JsonLog.class);

        obj.addProperty("eventType",log.eventType.toString());
        obj.addProperty("logTime",log.logTime);

        obj.addProperty("correlationId",log.correlationId);
        obj.addProperty("msg",log.msg);
        obj.addProperty("msgType",log.msgType.toString());
        obj.addProperty("name",logRecord.getLoggerName());
        obj.addProperty("time",logRecord.getMillis());
        obj.addProperty("sourceClass",logRecord.getSourceClassName());
        obj.addProperty("threadId",logRecord.getThreadID());
        obj.addProperty("threadName",log.threadName);
        return obj;
    }
}