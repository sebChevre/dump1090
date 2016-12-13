package ch.sebooom.dump1090.messages.sbs1;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public enum FieldType {

    MESSAGE_TYPE("Message type","messageType"),
    TRANSMISSION_TYPE("Transmission type","transmissionType"),
    SESSION_ID("Session ID","sessionId"),
    AIRCRAFT_ID("Aircraft ID","aircraftId"),
    HEX_IDENT("HexIdent","hexIdent"),
    FLIGHT_ID("Fligh ID","flightId"),
    DATE_MESSAGE_GEN("Date message generated","dateMessageGen"),
    TIME_MESSAGE_GEN("Time message generted","timeMessageGen"),
    DATE_MESSAGE_LOG("Date message logged","dateMessageLogged"),
    TIME_MESSAGE_LOG("Time message logged","timeMessageLogged"),
    CALLSIGN("Callsign","callSign"),
    ALTITUDE("Altitude","altitude"),
    GROUND_SPEDD("Groud speed","groudSpeed"),
    TRACK("Track","track"),
    LATITUDE("Latitude","latitude"),
    LONGITUDE("Longitude","longitude"),
    VERTICAL_RATE("Vertical rate","verticalRate"),
    SQUAWK("Squawk","squawk"),
    ALERT("Alert","alert"),
    EMERGENCY("Emergency","emergency"),
    SPI("SPI","spi"),
    IS_ON_GROUND("Is On Ground","isOnGround");

    private final String fieldName;
    private final String jsonFieldName;

    FieldType(String fieldName, String fieldNameTrimmed){
        this.fieldName = fieldName;
        this.jsonFieldName = fieldNameTrimmed;
    }

    public String fieldName(){
        return this.fieldName;
    }


    public String getJsonFieldName(){
        return jsonFieldName;
    }
}
