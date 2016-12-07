package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.RxBus;
import rx.functions.Action0;
import rx.functions.Action1;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * TCP msg stats generation
 */
public class TCPStatsGenerator {


    private final static Logger logger = Logger.getLogger(TCPStatsGenerator.class.getName());
    private int port;
    private RxBus bus;

    private TCPStatsGenerator() {
    }

    public static TCPStatsGenerator newInstance(){
        return new TCPStatsGenerator();
    }

    public void start(){

        logger.info("TCP Stats started");

        Action1<Throwable> errrorHandler = throwable ->
                logger.severe("Error during stream processing for ch.sebooom.dump1090.tcptestserver.tcp stats: "
                + throwable.getMessage());

        Action0 completeHandler = () ->
                logger.severe("Stream complete. This wouldnt happend");

        bus.toObserverable()
                .buffer(10, TimeUnit.SECONDS)
                .map(TCPStats::from)
                .subscribe(next -> {

                    int count = next.getCount();

                    logger.fine(count + " messages received in last seconds");
                    logger.fine("Stats:" + next.toJson());

                }, errrorHandler, completeHandler);

        bus.toObserverable()
                .buffer(1, TimeUnit.SECONDS)
                .map(TCPStats::from)
                .subscribe(next -> {

                    int count = next.getCount();

                    logger.fine(count + " messages received in last seconds");
                    logger.fine("Stats:" + next.toJson());

                }, errrorHandler, completeHandler);
    }




    public TCPStatsGenerator withBus(RxBus bus) {
        this.bus = bus;
        return this;
    }
}
