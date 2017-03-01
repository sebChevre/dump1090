package ch.sebooom.dump1090.utils;

import org.junit.Test;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class ChronoTest {

    @Test
    public void test() throws InterruptedException {

        Chrono d = Chrono.start();

        Thread.sleep(1000);

        System.out.println(d.stop());
    }
}