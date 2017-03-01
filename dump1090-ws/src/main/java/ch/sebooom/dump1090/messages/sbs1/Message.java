package ch.sebooom.dump1090.messages.sbs1;

import com.google.gson.GsonBuilder;
import org.apache.commons.lang.Validate;

import java.util.Date;

/**
 * High Level Message. Encapsulate SBS1 fields and type
 *
 */
public class Message {


    private Field [] fields = new Field[22];    //tableau des champs tcp
    private MessageType type;                   //type de message
    private long loggedTimeStamp;               //timestamp message from tcp
    private String correlationId;

    private static GsonBuilder gsonBuilder;

    static{
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new JsonMessageAdapter());
    }

    public static Message fromTCPString(String tcpString){
        Validate.notEmpty(tcpString); //null et pas vide
        return valideAndDecode(tcpString);
    }

    private Message(){

        this.loggedTimeStamp = new Date().getTime();
    }

    private static Message valideAndDecode(String tcpString){

        Message message = new Message();

        String [] fields = tcpString.split(",",-1);

        message
            .withType(MessageType.from(fields[0],fields[1]))
            .withFields(Fields.from(fields));


        return message;
    }



    private Message withType(MessageType type){
        this.type = type;
        return this;
    }

    private Message withFields(Field [] messageFields){
        fields = messageFields;
        this.correlationId = fields[Fields.ICAO_IDENT].getValue();
        return this;
    }



    public String getCorrelationId () {
        return correlationId;
    }

    public Field[] getFields(){
        return fields;
    }

    public long getLoggedTimeStamp(){
        return loggedTimeStamp;
    }

    public String toJson(){
        return gsonBuilder.create().toJson(this);
    }

    public MessageType type() {
        return type;
    }

    public String getFieldAt(int position){
        return fields[position].getValue();
    }
}
