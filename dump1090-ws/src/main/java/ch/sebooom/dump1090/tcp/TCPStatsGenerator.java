package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.RxBus;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by seb on 22.11.16.
 */
public class TCPStatsGenerator {


    private int port;
    private RxBus bus;

    final static Logger logger = Logger.getLogger(TCPStatsGenerator.class.getName());
    public static TCPStatsGenerator newInstance(){
        return new TCPStatsGenerator();
    }

    private TCPStatsGenerator(){}

    public void start(){

        logger.info("TCP Stats started");

        bus.toObserverable()
                .buffer(1, TimeUnit.SECONDS)
                .map(messages -> {
                    return TCPStats.from(messages);
                })
                .subscribe(next -> {

                    int count = next.getCount();

                    logger.fine(count + " messages received in last seconds");
                    logger.fine("Stats:" + next.toJson());

                },error -> {
                    logger.severe("Error during stream processing for tcp stats: "
                            + error.getMessage());
                },()->{
                    logger.severe("Stream complete for tcp stats, that's a problem");
                });
    }




    public TCPStatsGenerator withBus(RxBus bus) {
        this.bus = bus;
        return this;
    }
}
