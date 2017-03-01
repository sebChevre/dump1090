package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.bus.RxBus;
import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
import ch.sebooom.dump1090.messages.sbs1.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * TCP Listener class
 * Classe écoutant les trames tcp et publiant le résultat sur le bus
 */
public class TCPListener {

    private final static Logger logger = Logger.getLogger(TCPListener.class.getName());
    private int port;
    private String host;
    private RxBus bus;
    private Boolean running = Boolean.TRUE;

    public TCPListener(int port, String host ,RxBus bus){
        this.bus = bus;
        this.port = port;
        this.host = host;
    }


    /**
     * Démarrage de la boucle d'écoute, et publication sur le bus
     */
    public void start(){

            try {
                Socket skt = new Socket(host, port);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(skt.getInputStream()));

                logger.info(
                    JsonLog.technical(
                        String.format("TCP Listenning started [%s:%d]",host,port),
                            EventType.TCP_LISTENNING,0));


                while(running){

                    while (!in.ready()) {}

                    String message = in.readLine();
                    Message msg = Message.fromTCPString(message);
                    logger.info(JsonLog.domain(
                            String.format("Received from dump1090: %s",message),
                            msg.getCorrelationId(), EventType.TCP_MESSAGE_RECEIVED));
                    logger.fine(JsonLog.technical(
                            String.format("Send to bus: %S",msg),
                            EventType.TCP_LISTENNING,0));
                    bus.send(msg);
                }


            }
            catch(Exception e) {
                e.printStackTrace();
                logger.severe(JsonLog.technical(
                        String.format("Error during ch.sebooom.dump1090.tcptestserver.tcp dump1090 Listenning: %S",e.getMessage()),
                        EventType.TCP_LISTENNING,0));
                logger.severe(JsonLog.technical(
                        "System exiting now...",EventType.TCP_LISTENNING,0));
                System.exit(1);
            }

    }


}
