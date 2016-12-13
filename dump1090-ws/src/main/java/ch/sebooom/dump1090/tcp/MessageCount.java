package ch.sebooom.dump1090.tcp;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class MessageCount {

    public String id;
    public AtomicInteger count;

    public MessageCount(String id,AtomicInteger count){
        this.id = id;
        this.count = count;
    }


}
