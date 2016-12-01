package ch.sebooom.dump1090.tcp.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.Validate;

/**
 * Created by seb on 22.11.16.
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

    public Message withType(MessageType type){
        this.type = type;
        return this;
    }

    public Message withFields(Fields messageFields){
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
