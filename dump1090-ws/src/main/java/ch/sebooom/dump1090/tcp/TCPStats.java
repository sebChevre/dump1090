package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.tcp.messages.Message;
import ch.sebooom.dump1090.tcp.messages.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seb on 26.11.16.
 */
public class TCPStats {

    private HashMap<MessageType,AtomicInteger> messagesByType = new HashMap<>();


    public static TCPStats from(List<Message> messages){
        return new TCPStats(messages);
    }

    public int getCount () {
        return messagesByType.values().stream()
                .mapToInt(i -> i.intValue()).sum();
    }
    private TCPStats(List<Message> messages){

        messages.forEach(message->{

            messagesByType.putIfAbsent(message.type(), new AtomicInteger(0));
            messagesByType.get(message.type()).incrementAndGet();

        });

    }

    @Override
    public String toString() {
        return "TCPStats{" +
                "messagesByType=" + messagesByType +
                '}';
    }

    public String toJson(){
        StringBuilder ret = new StringBuilder("{");

        messagesByType.keySet().forEach(messageType->{

            ret.append(messageType.toString()).append(":").append(messagesByType.get(messageType).get()).append(",");
        });

        ret.append("}");
        return ret.toString();
    }
}
