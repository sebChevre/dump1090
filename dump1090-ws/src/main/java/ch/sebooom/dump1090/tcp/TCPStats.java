package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.tcp.messages.Message;
import ch.sebooom.dump1090.tcp.messages.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seb on 26.11.16.
 */
class TCPStats {

    private HashMap<MessageType,AtomicInteger> messagesByType = new HashMap<>();


    private TCPStats(List<Message> messages){

        messages.forEach(message->{

            messagesByType.putIfAbsent(message.type(), new AtomicInteger(0));
            messagesByType.get(message.type()).incrementAndGet();

        });

    }

    static TCPStats from(List<Message> messages) {
        return new TCPStats(messages);
    }

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

    String toJson() {
        StringBuilder ret = new StringBuilder("{");

        messagesByType.keySet().forEach(messageType -> ret.append(messageType.toString()).append(":").append(messagesByType.get(messageType).get()).append(","));

        ret.append("}");
        return ret.toString();
    }
}
