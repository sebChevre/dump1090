package ch.sebooom.dump1090;

import org.junit.Test;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class TokenSiteSplit {

    @Test
    public void test(){

        String t = "MSG,8,,,44CE66,,,,,,,,,,,,,,,,,";
        System.out.println(t.split(",",-1).length);
    }
}
