package ch.sebooom.dump1090.utils;

import java.util.Date;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class Chrono {

    private long startTime;

    public static Chrono  start(){
        Chrono c = new Chrono();
        c.startTime = new Date().getTime();
        return c;
    }

    public long stop(){
        return new Date().getTime() - startTime;
    }
}
