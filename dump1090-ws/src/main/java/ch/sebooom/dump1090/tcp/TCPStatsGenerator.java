package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.bus.RxBus;
import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
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

        logger.info(JsonLog.technical("TCP Stats generator started",EventType.STATS_GENERATION,0));


        //error handler
        Action1<Throwable> errrorHandler = throwable ->

                logger.severe(JsonLog.technical(
                        String.format("Error during stream processing for statsGenerator stats: %s", throwable.getMessage())
                ,EventType.STATS_GENERATION,0));

        //complete handler
        Action0 completeHandler = () ->
                logger.severe(JsonLog.technical("Stream complete. This wouldnt happend",EventType.STATS_GENERATION,0));

        //Traietement des messages venat du bus (flux tcp)
        bus.toObserverable()
                .buffer(1, TimeUnit.MINUTES)            //toutes les minutes
                .map(TCPStats::from)                    //instanciation d'un objet TCPStats
                .subscribe(next -> {



                    int count = next.getTotalMsgsCount();   //total pour log
                   
                    logger.info(JsonLog.domain(String.format("%d messages received in last seconds",count),
                            String.valueOf(next.getStartTime()),
                            EventType.MESSAGES_STATS_AGGREGATION));
                    logger.fine(JsonLog.technical("Stats:" + next.toJson(),EventType.STATS_GENERATION,0));

                    tcpStatsService.saveStats(next);    //persistance db

                    logger.info(JsonLog.domain(
                            String.format("Stats generated: %s",next.toJson()),
                            String.valueOf(next.getStartTime()), EventType.TCP_MESSAGE_RECEIVED)
                    );



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
