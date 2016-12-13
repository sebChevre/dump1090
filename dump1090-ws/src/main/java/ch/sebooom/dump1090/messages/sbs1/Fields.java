package ch.sebooom.dump1090.messages.sbs1;

/**
 * Created by seb on .

 ${VERSION}
 */
class Fields {




    static Field[] from(String[] fields) {

        Field [] fieldsFromTcp = new Field[22];


        fieldsFromTcp[0] = new Field(FieldType.MESSAGE_TYPE,fields[0]);
        fieldsFromTcp[1] = new Field(FieldType.TRANSMISSION_TYPE,fields[1]);
        fieldsFromTcp[2] = new Field(FieldType.SESSION_ID,fields[2]);
        fieldsFromTcp[3] = new Field(FieldType.AIRCRAFT_ID,fields[3]);
        fieldsFromTcp[4] = new Field(FieldType.HEX_IDENT,fields[4]);
        fieldsFromTcp[5] = new Field(FieldType.FLIGHT_ID,fields[5]);
        fieldsFromTcp[6] = new Field(FieldType.DATE_MESSAGE_GEN,fields[6]);
        fieldsFromTcp[7] = new Field(FieldType.TIME_MESSAGE_GEN,fields[7]);
        fieldsFromTcp[8] = new Field(FieldType.DATE_MESSAGE_LOG,fields[8]);
        fieldsFromTcp[9] = new Field(FieldType.TIME_MESSAGE_LOG,fields[9]);

        //callsing seulement avec MSG, SEL et ID
        if(fields[0].contains("MSG")
                || fields[0].equals("SEL")
                || fields[0].contains("ID")){
            fieldsFromTcp[10] = new Field(FieldType.CALLSIGN,fields[10]);
        }else{
            fieldsFromTcp[10] = new Field(FieldType.CALLSIGN,"");
        }

        //Seulement avec MSG
        if(fields[0].contains("MSG")){
            fieldsFromTcp[11] = new Field(FieldType.ALTITUDE,fields[11]);
            fieldsFromTcp[12] = new Field(FieldType.GROUND_SPEDD,fields[12]);
            fieldsFromTcp[13] = new Field(FieldType.TRACK,fields[13]);
            fieldsFromTcp[14] = new Field(FieldType.LATITUDE,fields[14]);
            fieldsFromTcp[15] = new Field(FieldType.LONGITUDE,fields[15]);
            fieldsFromTcp[16] = new Field(FieldType.VERTICAL_RATE,fields[16]);
            fieldsFromTcp[17] = new Field(FieldType.SQUAWK,fields[17]);
            fieldsFromTcp[18] = new Field(FieldType.ALERT,fields[18]);
            fieldsFromTcp[19] = new Field(FieldType.EMERGENCY,fields[19]);
            fieldsFromTcp[20] = new Field(FieldType.SPI,fields[20]);
            fieldsFromTcp[21] = new Field(FieldType.IS_ON_GROUND,fields[21]);
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
