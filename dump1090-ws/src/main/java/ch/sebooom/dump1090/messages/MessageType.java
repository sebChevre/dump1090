package ch.sebooom.dump1090.messages;

import org.apache.commons.lang.Validate;

import java.util.Arrays;
import java.util.Optional;

/**
 * SBS1 Message type
 */
public enum MessageType {

    MSG1("Identification and Category"),
    MSG2("Surface Position Message"),
    MSG3("ES Airborne Position Message"),
    MSG4("ES Airborne Velocity Message"),
    MSG5("Surveillance Alt Message"),
    MSG6("Surveillance ID Message"),
    MSG7("Air To Air Message"),
    MSG8("All Call Reply"),
    SEL("SELECTION CHANGE MESSAGE"),
    ID("NEW ID MESSAGE"),
    AIR("NEW AIRCRAFT MESSAGE"),
    STA("STATUS CHANGE MESSAGE"),
    CLK("CLICK MESSAGE");

    public String description;

    MessageType(String description){
        this.description = description;
    }

    public static MessageType from(String type, String transType) {

        Validate.notNull(type);
        Validate.notNull(transType);

        String fullType = type + transType;


        Optional<MessageType> messageType = Arrays.asList(MessageType.values())
            .stream()
                .filter(msg -> msg.toString().equals(fullType))
            .findFirst();

        if(messageType.isPresent()){
            return messageType.get();
        }

        throw new NullPointerException();
    }
}
