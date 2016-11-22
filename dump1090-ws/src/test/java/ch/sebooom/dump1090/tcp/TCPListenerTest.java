package ch.sebooom.dump1090.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by seb on 22.11.16.
 */
public class TCPListenerTest {


    static boolean r = true;
    public static void main(String[] args) throws InterruptedException {
        //TCPTestServer.main(null);
        try {
            Socket skt = new Socket("localhost", 1234);
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(skt.getInputStream()));

            while(r){
                System.out.println("running");
                while (!in.ready()) {}

               // bus.send(in.readLine());
                //System.out.print("'\n");
                System.out.println("send to bus: ");
            }

            in.close();
        }
        catch(Exception e) {
            System.out.print("Whoops! It didn't work!\n");
        }
    }



}