package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.messages.sbs1.Fields;
import ch.sebooom.dump1090.messages.sbs1.ICAOIdent;
import ch.sebooom.dump1090.messages.sbs1.Message;
import ch.sebooom.dump1090.messages.sbs1.MessageType;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TCPStats aggreagate object
 */
public class TCPStats {

    private final long totalTime;
    private HashMap<MessageType,AtomicInteger> messagesByType = new HashMap<>();
    private HashMap<ICAOIdent,AtomicInteger> messagesByPlane = new HashMap<>();
    private long startTime;
    private long stopTime;

    /**
     * Static constructor
     * @param messages list of messages to agregate
     * @return a new instance
     */
    static TCPStats from(List<Message> messages) {
        return new TCPStats(messages);
    }



    private TCPStats(List<Message> messages){

        Collections.sort(messages, new Comparator<Message>(){
            public int compare(Message p1, Message p2){
                return ((Long)p1.getLoggedTimeStamp())
                        .compareTo(((Long)p2.getLoggedTimeStamp()));
            }
        });

        

        startTime = messages.get(0).getLoggedTimeStamp();
        stopTime = messages.get(messages.size()-1).getLoggedTimeStamp();
        totalTime = stopTime-startTime;
        
        messages.forEach(message->{
            //add message type as map key if it didnt already exist
            messagesByType.putIfAbsent(message.type(), new AtomicInteger(0));

            messagesByPlane.putIfAbsent(new ICAOIdent(message.getFieldAt(Fields.ICAO_IDENT)),new AtomicInteger(0));

            //increment msg by type counter
            messagesByType.get(message.type()).incrementAndGet();

            messagesByPlane.get(new ICAOIdent(message.getFieldAt(Fields.ICAO_IDENT))).incrementAndGet();

        });

    }



    public HashMap<MessageType, AtomicInteger> getMessagesByType() {
        return messagesByType;
    }

    public HashMap<ICAOIdent, AtomicInteger> getMessagesByPlane() {
        return messagesByPlane;
    }

   

    /**
     * Return total messages count
     * @return the total number of messages
     */
    public int getTotalCount() {
        return messagesByType.values().stream()
                .mapToInt(AtomicInteger::intValue).sum();
    }

    public long getTotalTime() {
		return totalTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getStopTime() {
		return stopTime;
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
    	
        JsonObject json = new JsonObject();
        
        messagesByType.keySet().forEach(messageType ->
        	json.addProperty(messageType.toString(), messagesByType.get(messageType).get())
        );
       
        
        return json.toString();
    }
}
