package ch.sebooom.dump1090.tcp.messages;

/**
 * Created by seb on 22.11.16.
 */
public class Fields {


    public Field messageType = new Field("Message type",0);
    public Field transmissionType = new Field("Transmission type",1);
    public Field sessionID	 = new Field("Session ID",2);
    public Field aircraftID = new Field("Aircraft ID",3);
    public Field hexIdent = new Field("HexIdent",4);
    public Field flightID	= new Field("Fligh ID",5);
    public Field dateMessageGen = new Field("Date message generated",6);
    public Field timeMessageGen = new Field("Time message generted",7);
    public Field dateMessageLog = new Field("Date message logged",8);
    public Field timeMessageLog = new Field("Time message logged",9);
    public Field callsign = new Field("Callsign",10);
    public Field altitude	= new Field("Altitude",11);
    public Field groundSpeed = new Field("Groud speed",12);
    public Field track	 = new Field("Track",13);
    public Field latitude = new Field("Latitude",14);
    public Field longitude = new Field("Longitude",15);
    public Field verticalRate = new Field("Vertical rate",16);
    public Field squawk = new Field("Squawk",17);
    public Field alert = new Field("Alert",18);
    public Field emergency = new Field("Emergency",19);
    public Field sPI = new Field("SPI",20);
    public Field isOnGround = new Field("is On Ground",21);

    public String toJson() {
        return "{" +
                "aircraftID:" + formatFieldToJson(aircraftID) +
                ",messageType:" + formatFieldToJson(messageType) +
                ",transmissionType:" + formatFieldToJson(transmissionType) +
                ",sessionID:" + formatFieldToJson(sessionID) +
                ",hexIdent:" + formatFieldToJson(hexIdent) +
                ",flightID:" + formatFieldToJson(flightID) +
                ",dateMessageGen:" + formatFieldToJson(dateMessageGen) +
                ",timeMessageGen:" + formatFieldToJson(timeMessageGen) +
                ",dateMessageLog:" + formatFieldToJson(dateMessageLog) +
                ",timeMessageLog:" + formatFieldToJson(timeMessageLog) +
                ",callsign:" + formatFieldToJson(callsign) +
                ",altitude:" + formatFieldToJson(altitude) +
                ",groundSpeed:" + formatFieldToJson(groundSpeed) +
                ",track:" + formatFieldToJson(track) +
                ",latitude:" + formatFieldToJson(latitude) +
                ",longitude:" + formatFieldToJson(longitude) +
                ",verticalRate:" + formatFieldToJson(verticalRate) +
                ",squawk:" + formatFieldToJson(squawk) +
                ",alert:" + formatFieldToJson(alert) +
                ",emergency:" + formatFieldToJson(emergency) +
                ",sPI:" + formatFieldToJson(sPI) +
                ",isOnGround:" + formatFieldToJson(isOnGround) +
                '}';
    }


    public String formatFieldToJson(Field f){
        if(null == f.getValue()){
            return null;
        }else{
            return "'" + f.getValue() + "'";
        }
    }
    public static Fields  from(String [] fields){

        Fields f = new Fields();

        f.messageType.withValue(fields[0]);
        f.transmissionType.withValue(fields[1]);
        f.sessionID.withValue(fields[2]);
        f.aircraftID.withValue(fields[3]);
        f.hexIdent.withValue(fields[4]);
        f.flightID.withValue(fields[5]);
        f.dateMessageGen.withValue(fields[6]);
        f.timeMessageGen.withValue(fields[7]);
        f.dateMessageLog.withValue(fields[8]);
        f.timeMessageLog.withValue(fields[9]);

        if(f.messageType.getValue().contains("MSG")
                || f.messageType.getValue().equals("SEL")
                || f.messageType.getValue().contains("ID")){
            f.callsign.withValue(fields[10]);
        }

        if(f.messageType.getValue().contains("MSG")){
            f.altitude.withValue(fields[11]);
            f.groundSpeed.withValue(fields[12]);
            f.track.withValue(fields[13]);
            f.latitude.withValue(fields[14]);
            f.longitude.withValue(fields[15]);
            f.verticalRate.withValue(fields[16]);
            f.squawk.withValue(fields[17]);
            f.alert.withValue(fields[18]);
            f.emergency.withValue(fields[19]);
            f.sPI.withValue(fields[20]);
            f.isOnGround.withValue(fields[21]);
        }
        return f;
    }



}
