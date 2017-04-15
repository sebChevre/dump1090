package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.bus.RxBus;
import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
import ch.sebooom.dump1090.messages.sbs1.Message;

import java.io.BufferedReader;
import java.io.IOException;
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
                    JsonLog.log(
                        String.format("TCP Listenning started [%s:%d]",host,port),
                            EventType.TCP_LISTENNING_PROCESS_STARTED,JsonLog.EMPTY_CORRELATION_ID)
                );


                while(running){

                    String message = null;

                    while (!in.ready()) {}

                    message = in.readLine();

                    logger.info(JsonLog.log(
                            String.format("TCP String reveived [%s], trying to parse...",message),
                            EventType.TCP_MESSAGE_RECEIVED,
                            JsonLog.EMPTY_CORRELATION_ID)
                    );

                    //parse message
                    Message msg = Message.fromTCPString(message);

                    //si parse ok
                    if(null != msg){
                        logger.info(JsonLog.log(
                                String.format("TCP String parsed: %s",message),
                                EventType.TCP_MESSAGE_PARSED,
                                msg.getCorrelationId()));

                        logger.fine(JsonLog.log(
                                String.format("Send to bus: %S",msg),
                                EventType.INTERNAL_BUS_SENDING,
                                msg.getCorrelationId()));
                        bus.send(msg);
                    }


                }


            }catch (IOException ioex){
                ioex.printStackTrace();
                logger.severe(JsonLog.log(
                        String.format("Error during dump1090 Listenning: %S",ioex.getMessage()),
                        EventType.TCP_LISTENNING,
                        JsonLog.EMPTY_CORRELATION_ID)
                );
                logger.severe(JsonLog.log(
                        "System exiting now...",
                        EventType.SYSTEM_EXIT,
                        JsonLog.EMPTY_CORRELATION_ID));
                System.exit(1);
            }

    }



}
