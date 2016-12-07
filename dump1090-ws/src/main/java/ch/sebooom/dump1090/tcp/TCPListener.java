package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.RxBus;
import ch.sebooom.dump1090.messages.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * TCP Listener class
 */
public class TCPListener {

    private final static Logger logger = Logger.getLogger(TCPListener.class.getName());
    private int port;
    private String host;
    private RxBus bus;

    public TCPListener(int port, String host ,RxBus bus){
        this.bus = bus;
        this.port = port;
        this.host = host;
    }


    public void start(){



            try {
                Socket skt = new Socket(host, port);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(skt.getInputStream()));

                logger.info("TCP Listenning started [" + host +":" + port +"]");


                while(true){

                    while (!in.ready()) {}

                    String message = in.readLine();
                    Message msg = Message.fromTCPString(message);
                    logger.fine("Received from dump1090: " + message);
                    logger.fine("Send to bus: " + msg);
                    bus.send(msg);
                }


            }
            catch(Exception e) {
                logger.severe("Error during ch.sebooom.dump1090.tcptestserver.tcp dump1090 Listenning: " + e.getMessage());
                logger.severe("System exiting now...");
                System.exit(1);
            }




    }


}
