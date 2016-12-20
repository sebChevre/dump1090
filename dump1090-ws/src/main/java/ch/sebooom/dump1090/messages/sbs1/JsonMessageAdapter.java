package ch.sebooom.dump1090.messages.sbs1;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class JsonMessageAdapter implements JsonSerializer<Message> {
    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext jsonSerializationContext) {
        
    	JsonObject obj = new JsonObject();
        
        //ZoneId zoneId = ZoneId.systemDefault(); 
        
        //long epoch = message.getLoggedDateTime().atZone(zoneId).toEpochSecond();
        
        obj.addProperty("timestamp", message.getLoggedTimeStamp());

        for(Field f : message.getFields()){
            obj.addProperty(f.fieldType().getJsonFieldName(),f.getValue());
        }

        return obj;
    }
}
