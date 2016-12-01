package ch.sebooom.dump1090.tcp.messages;

/**
 * Created by seb on .

 ${VERSION}
 */
class Fields {


    private Field messageType = new Field("Message type", 0);
    private Field transmissionType = new Field("Transmission type", 1);
    private Field sessionID = new Field("Session ID", 2);
    private Field aircraftID = new Field("Aircraft ID", 3);
    private Field hexIdent = new Field("HexIdent", 4);
    private Field flightID = new Field("Fligh ID", 5);
    private Field dateMessageGen = new Field("Date message generated", 6);
    private Field timeMessageGen = new Field("Time message generted", 7);
    private Field dateMessageLog = new Field("Date message logged", 8);
    private Field timeMessageLog = new Field("Time message logged", 9);
    private Field callsign = new Field("Callsign", 10);
    private Field altitude = new Field("Altitude", 11);
    private Field groundSpeed = new Field("Groud speed", 12);
    private Field track = new Field("Track", 13);
    private Field latitude = new Field("Latitude", 14);
    private Field longitude = new Field("Longitude", 15);
    private Field verticalRate = new Field("Vertical rate", 16);
    private Field squawk = new Field("Squawk", 17);
    private Field alert = new Field("Alert", 18);
    private Field emergency = new Field("Emergency", 19);
    private Field sPI = new Field("SPI", 20);
    private Field isOnGround = new Field("is On Ground", 21);

    static Fields from(String[] fields) {

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

    String toJson() {
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

    private String formatFieldToJson(Field f) {
        if (null == f.getValue()) {
            return null;
        } else {
            return "'" + f.getValue() + "'";
        }
    }



}
