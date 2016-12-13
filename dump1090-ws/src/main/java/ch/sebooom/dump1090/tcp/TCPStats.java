package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.messages.sbs1.Message;
import ch.sebooom.dump1090.messages.sbs1.MessageType;
import com.rethinkdb.model.MapObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TCPStats aggreagate object
 */
public class TCPStats {

    private HashMap<MessageType,AtomicInteger> messagesByType = new HashMap<>();

    /**
     * Static constructor
     * @param messages list of messages to agregate
     * @return a new instance
     */
    static TCPStats from(List<Message> messages) {
        return new TCPStats(messages);
    }

    private TCPStats(List<Message> messages){

        messages.forEach(message->{
            //add message type as map key if it didnt already exist
            messagesByType.putIfAbsent(message.type(), new AtomicInteger(0));
            //increment msg by type counter
            messagesByType.get(message.type()).incrementAndGet();

        });



    }

    public HashMap<MessageType, AtomicInteger> getMessagesByType() {
        return messagesByType;
    }

    public MapObject getMapObject(){
        MapObject mapObject = new MapObject();


        messagesByType.keySet().forEach(messageType -> {

            mapObject.with(messageType.toString(),messagesByType.get(messageType));
        });

        return mapObject;
    }

    /**
     * Return total messages count
     * @return the total number of messages
     */
    int getCount() {
        return messagesByType.values().stream()
                .mapToInt(AtomicInteger::intValue).sum();
    }

    @Override
    public String toString() {
        return "TCPStats{" +
                "messagesByType=" + messagesByType +
                '}';
    }

    /**
     * JSONify the instance
     * @return a json string of the instance
     */
    String toJson() {
        StringBuilder ret = new StringBuilder("{");

        /*ret.append("start:").append(startTime.toString()).append(",").append("stop:")
                .append(stopTime.toString()).append(",")
                .append("duration:" + (stopTime.getTime()-startTime.getTime()))
                .append(",");*/

        messagesByType.keySet().forEach(messageType ->
                ret
                .append(messageType.toString()).append(":")
                .append(messagesByType.get(messageType).get()).append(","));
                ret.append("}");

        return ret.toString();
    }
}
