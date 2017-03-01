package ch.sebooom.dump1090.log;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public enum EventType {

    TCP_MESSAGE_RECEIVED(LogType.DOMAIN),
    MESSAGES_STATS_AGGREGATION(LogType.DOMAIN),
    PROPERTIES(LogType.TECHNICAL),
    REPOSITORY(LogType.TECHNICAL),
    TCP_LISTENNING(LogType.TECHNICAL),
    STATS_GENERATION(LogType.TECHNICAL),
    WEB_SERVER(LogType.TECHNICAL),

    WEBSOCKET_CONNECTION(LogType.TECHNICAL),
    INTERNAL_BUS(LogType.TECHNICAL),
    WEBSOCKET_SENDING(LogType.TECHNICAL),
    WEBSOCKET_DISCONNECT(LogType.TECHNICAL), REST_GET_TEST(LogType.TECHNICAL), REST_GET(LogType.TECHNICAL), STATS_SAVING(LogType.TECHNICAL), STATS_FIND_LAST(LogType.TECHNICAL), STATS_FIND_PERIOD(LogType.TECHNICAL);


    LogType logType;

    EventType(LogType type){
        this.logType = type;
    }

    public LogType logType(){
        return logType;
    }
}
