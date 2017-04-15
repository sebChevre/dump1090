package ch.sebooom.dump1090.messages.sbs1;

import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;

import java.util.logging.Logger;

/**
 * Created by seb on .

 ${VERSION}
 */
public class Fields {


    public static final int ICAO_IDENT = 4;
    private final static Logger logger = Logger.getLogger(Fields.class.getName());

    static Field[] from(String[] fields) throws FieldIndexNotFoundException {

        Field [] fieldsFromTcp = null;

        try{
            fieldsFromTcp = new Field[22];

            fieldsFromTcp[0] = new Field(FieldType.MESSAGE_TYPE,(null!=fields[0])?fields[0]:"");
            fieldsFromTcp[1] = new Field(FieldType.TRANSMISSION_TYPE,(null!=fields[1])?fields[1]:"");
            fieldsFromTcp[2] = new Field(FieldType.SESSION_ID,(null!=fields[2])?fields[2]:"");
            fieldsFromTcp[3] = new Field(FieldType.AIRCRAFT_ID,(null!=fields[3])?fields[3]:"");
            fieldsFromTcp[4] = new Field(FieldType.HEX_IDENT,(null!=fields[4])?fields[4]:"");
            fieldsFromTcp[5] = new Field(FieldType.FLIGHT_ID,(null!=fields[5])?fields[5]:"");
            fieldsFromTcp[6] = new Field(FieldType.DATE_MESSAGE_GEN,(null!=fields[6])?fields[6]:"");
            fieldsFromTcp[7] = new Field(FieldType.TIME_MESSAGE_GEN,(null!=fields[7])?fields[7]:"");
            fieldsFromTcp[8] = new Field(FieldType.DATE_MESSAGE_LOG,(null!=fields[8])?fields[8]:"");
            fieldsFromTcp[9] = new Field(FieldType.TIME_MESSAGE_LOG,(null!=fields[9])?fields[9]:"");

            //callsing seulement avec MSG, SEL et ID
            if(fields[0].contains("MSG")
                    || fields[0].equals("SEL")
                    || fields[0].contains("ID")){
                fieldsFromTcp[10] = new Field(FieldType.CALLSIGN,(null!=fields[10])?fields[10]:"");
            }else{
                fieldsFromTcp[10] = new Field(FieldType.CALLSIGN,"");
            }

            //Seulement avec MSG
            if(fields[0].contains("MSG")){
                fieldsFromTcp[11] = new Field(FieldType.ALTITUDE,(null!=fields[11])?fields[11]:"");
                fieldsFromTcp[12] = new Field(FieldType.GROUND_SPEDD,(null!=fields[12])?fields[12]:"");
                fieldsFromTcp[13] = new Field(FieldType.TRACK,(null!=fields[13])?fields[13]:"");
                fieldsFromTcp[14] = new Field(FieldType.LATITUDE,(null!=fields[14])?fields[14]:"");
                fieldsFromTcp[15] = new Field(FieldType.LONGITUDE,(null!=fields[15])?fields[15]:"");
                fieldsFromTcp[16] = new Field(FieldType.VERTICAL_RATE,(null!=fields[16])?fields[16]:"");
                fieldsFromTcp[17] = new Field(FieldType.SQUAWK,(null!=fields[17])?fields[17]:"");
                fieldsFromTcp[18] = new Field(FieldType.ALERT,(null!=fields[18])?fields[18]:"");
                fieldsFromTcp[19] = new Field(FieldType.EMERGENCY,(null!=fields[19])?fields[19]:"");
                fieldsFromTcp[20] = new Field(FieldType.SPI,(null!=fields[20])?fields[20]:"");
                fieldsFromTcp[21] = new Field(FieldType.IS_ON_GROUND,(null!=fields[21])?fields[21]:"");
            }else{
                fieldsFromTcp[11] = new Field(FieldType.ALTITUDE,"");
                fieldsFromTcp[12] = new Field(FieldType.GROUND_SPEDD,"");
                fieldsFromTcp[13] = new Field(FieldType.TRACK,"");
                fieldsFromTcp[14] = new Field(FieldType.LATITUDE,"");
                fieldsFromTcp[15] = new Field(FieldType.LONGITUDE,"");
                fieldsFromTcp[16] = new Field(FieldType.VERTICAL_RATE,"");
                fieldsFromTcp[17] = new Field(FieldType.SQUAWK,"");
                fieldsFromTcp[18] = new Field(FieldType.ALERT,"");
                fieldsFromTcp[19] = new Field(FieldType.EMERGENCY,"");
                fieldsFromTcp[20] = new Field(FieldType.SPI,"");
                fieldsFromTcp[21] = new Field(FieldType.IS_ON_GROUND,"");
            }
        }catch (ArrayIndexOutOfBoundsException ex){
            logger.warning(JsonLog.log(
                    String.format("Exception during field <-> tcpString mapping, index: %s",ex.getMessage()),
                    EventType.TCP_MESSAGE_PARSING,
                    Message.correlationIdFromSplittedTable(fieldsFromTcp)));
            throw new FieldIndexNotFoundException(
                    String.format("Problem with field: %s",ex.getMessage()));
        }


        return fieldsFromTcp;
    }



    private String formatFieldToJson(Field f) {
        if (null == f.getValue()) {
            return null;
        } else {
            return "'" + f.getValue() + "'";
        }
    }



}
