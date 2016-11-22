package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.tcp.messages.SBS1MessagesSamples;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by seb on 22.11.16.
 */
public class TCPTestServer {

    static boolean running = Boolean.TRUE;

    public static void main(String args[]) {
        String data = "Test\n";
        try {
            ServerSocket srvr = new ServerSocket(1234);
            Socket skt = srvr.accept();
            PrintWriter out = new PrintWriter(skt.getOutputStream(), true);


            while(running){


                out.print(SBS1MessagesSamples.getRandomMsg());
                System.out.println("Message sent");
                out.flush();

                TimeUnit.MILLISECONDS.sleep(100);
            }

            out.close();
            skt.close();
            srvr.close();
        }
        catch(Exception e) {
           e.printStackTrace();
        }
    }
}
