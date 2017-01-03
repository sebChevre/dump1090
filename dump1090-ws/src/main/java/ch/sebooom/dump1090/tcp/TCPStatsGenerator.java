package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.RxBus;
import ch.sebooom.dump1090.service.TCPStatsService;
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


    /**
     * Constructeur privé appelé par la factory static
     * @param service le service des stats
     */
    private TCPStatsGenerator(TCPStatsService service) {
        tcpStatsService = service;
    }

    public static TCPStatsGenerator newInstance(TCPStatsService service){
        return new TCPStatsGenerator(service);
    }

    public void start(){

        logger.info("TCP Stats started");


        //error handler
        Action1<Throwable> errrorHandler = throwable ->

                logger.severe("Error during stream processing for ch.sebooom.dump1090.tcptestserver.tcp stats: "
                + throwable.getMessage());

        //complete handler
        Action0 completeHandler = () ->
                logger.severe("Stream complete. This wouldnt happend");

        //Traietement des messages venat du bus (flux tcp)
        bus.toObserverable()
                .buffer(10, TimeUnit.SECONDS)            //toutes les minutes
                .map(TCPStats::from)                    //instanciation d'un objet TCPStats
                .subscribe(next -> {


                	tcpStatsService.saveStats(next);    //persistance db

                    int count = next.getTotalCount();   //total pour log
                   
                    logger.info(count + " messages received in last seconds");
                    logger.info("Stats:" + next.toJson());

                }, errrorHandler, completeHandler);
    }


    /**
     * Setter permettant d'injecter le bus
     * @param bus le bus de données interne
     * @return l'instance courante
     */
    public TCPStatsGenerator withBus(RxBus bus) {
        this.bus = bus;
        return this;
    }
}
