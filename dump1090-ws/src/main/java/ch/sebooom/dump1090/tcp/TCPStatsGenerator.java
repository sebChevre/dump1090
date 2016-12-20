package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.RxBus;
import ch.sebooom.dump1090.service.TCPStatsService;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import rx.functions.Action0;
import rx.functions.Action1;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * TCP msg stats generation
 */
public class TCPStatsGenerator {


    private final static Logger logger = Logger.getLogger(TCPStatsGenerator.class.getName());
    private RxBus bus;
    private TCPStatsService tcpStatsService;
    
    
    private TCPStatsGenerator(TCPStatsService service) {
        tcpStatsService = service;
    }

    public static TCPStatsGenerator newInstance(TCPStatsService service){
        return new TCPStatsGenerator(service);
    }

    public void start(){

        logger.info("TCP Stats started");


        Action1<Throwable> errrorHandler = throwable ->
                logger.severe("Error during stream processing for ch.sebooom.dump1090.tcptestserver.tcp stats: "
                + throwable.getMessage());

       Action0 completeHandler = () ->
                logger.severe("Stream complete. This wouldnt happend");



        bus.toObserverable()
                .buffer(1, TimeUnit.MINUTES)
                .map(TCPStats::from)
                .subscribe(next -> {
                	
                	tcpStatsService.saveStats(next);

                    int count = next.getTotalCount();
                   
                    logger.info(count + " messages received in last seconds");
                    logger.info("Stats:" + next.toJson());

                }, errrorHandler, completeHandler);
    }




    public TCPStatsGenerator withBus(RxBus bus) {
        this.bus = bus;
        return this;
    }
}
