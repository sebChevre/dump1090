package ch.sebooom.dump1090.messages.sbs1;

import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.logging.Logger;

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

    private final static Logger logger = Logger.getLogger(Message.class.getName());

    private final static String TCP_TOKEN_SEPARATOR = ",";
    private final static int MINIMAL_TOKEN_SIZE = 10;

    static{
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new JsonMessageAdapter());
    }

    public static Message fromTCPString(String tcpString){

        if(valid(tcpString)){
            return parse(tcpString);
        }else{
            return null;
        }

    }

    private static boolean valid(String tcpStrings) {

        String invalidMessage = null;

        if(null == tcpStrings){
            logger.warning(JsonLog.log(
                    "Invalid message: null",
                    EventType.TCP_MESSAGE_PARSING,
                    JsonLog.EMPTY_CORRELATION_ID));
            return false;
        }

        if(tcpStrings.split(TCP_TOKEN_SEPARATOR,-1).length < MINIMAL_TOKEN_SIZE){
            logger.warning(JsonLog.log(
                    String.format("Invalid message, tokens size < 10: %s",tcpStrings),
                    EventType.TCP_MESSAGE_PARSING,
                    JsonLog.EMPTY_CORRELATION_ID));
            return false;
        }

        return true;
    }

    private Message(){

        this.loggedTimeStamp = new Date().getTime();
    }

    private static Message parse(String tcpString){

        Message message = new Message();

        logger.info(JsonLog.log(
                String.format("Parsing message: %s",tcpString ),
                EventType.TCP_MESSAGE_PARSING,
                Message.correlationIdFromTcpString(tcpString)));



        try{
            String [] fields = tcpString.split(TCP_TOKEN_SEPARATOR,-1);

            message
                    .withType(MessageType.from(fields[0],fields[1]))
                    .withFields(Fields.from(fields));

        }catch (FieldIndexNotFoundException ex){
            logger.warning(JsonLog.log(
                    String.format("Problem during generating message.TcpString: %s",tcpString),
                    EventType.TCP_MESSAGE_PARSING,
                    Message.correlationIdFromTcpString(tcpString)));
            logger.warning(JsonLog.log(
                    String.format("Message ignored from statistics"),
                    EventType.TCP_MESSAGE_PARSING,
                    Message.correlationIdFromTcpString(tcpString)));
            return null;
        }


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

        if(null != correlationId && !correlationId.isEmpty()){
            return correlationId;
        }

        return JsonLog.CORRELATION_ID_ERROR;
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

    public static String correlationIdFromTcpString(String tcpString){

        String cId = null;

        try{
            cId = tcpString.split(TCP_TOKEN_SEPARATOR,-1)[Fields.ICAO_IDENT];
        }catch(Exception ex){
            logger.warning(JsonLog.log(
                    String.format("Error during generating correlationId : %s",ex.getMessage()),
                    EventType.CORRELATION_ID_GENERATED,
                    JsonLog.CORRELATION_ID_ERROR
            ));

            return JsonLog.CORRELATION_ID_ERROR;
        }

        return cId;

    }

    public static String correlationIdFromSplittedTable(Field []splittedTable){

        String cId = null;

        try{
            cId = splittedTable[Fields.ICAO_IDENT].getValue();
        }catch(Exception ex){
            logger.warning(JsonLog.log(
                    String.format("Error during generating correlationId : %s",ex.getMessage()),
                    EventType.CORRELATION_ID_GENERATED,
                    JsonLog.CORRELATION_ID_ERROR
            ));

            return JsonLog.CORRELATION_ID_ERROR;
        }

        return cId;

    }


}
