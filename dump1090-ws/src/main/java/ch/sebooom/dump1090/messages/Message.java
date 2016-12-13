package ch.sebooom.dump1090.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.Validate;

/**
 * High Level Message. Encapsulate SBS1 fields and type
 */
public class Message {

    private Fields messageFields;
    private MessageType type;
    private static Gson gson = new GsonBuilder().create();

    public static Message fromTCPString(String tcpString){
        Validate.notEmpty(tcpString); //null et pas vide
        return valideAndDecode(tcpString);
    }

    private static Message valideAndDecode(String tcpString){

        Message message = new Message();

        String [] fields = tcpString.split(",",-1);

        message.withType(MessageType.from(fields[0],fields[1]));

        message.withFields(Fields.from(fields));

        return message;
    }



    private Message withType(MessageType type){
        this.type = type;
        return this;
    }

    private Message withFields(Fields messageFields){
        this.messageFields = messageFields;
        return this;
    }

    public String toJson(){
        return messageFields.toJson();
    }

    public MessageType type() {
        return type;
    }
}
