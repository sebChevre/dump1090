package ch.sebooom.dump1090.tcp.messages;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class MessageTypeTest {

    @Test
    public void testSimpleCase(){

        MessageType t = MessageType.from("MSG","1");
        assertTrue(t.equals(MessageType.MSG1));

        t = MessageType.from("CLK","");
        assertTrue(t.equals(MessageType.CLK));

        t = MessageType.from("ID","");
        assertTrue(t.equals(MessageType.ID));

    }

    @Test
    public void testSampleDataSet(){

        int count = SBS1MessagesSamples.messages.size();


        long msgCount = SBS1MessagesSamples.messages.stream()
                .map(Message::fromTCPString).count();

        assertTrue(msgCount == count);

        SBS1MessagesSamples.messages.stream()
                .map(msg -> Message.fromTCPString(msg))
                .forEach(message -> System.out.println(message.toJson()));
    }

}