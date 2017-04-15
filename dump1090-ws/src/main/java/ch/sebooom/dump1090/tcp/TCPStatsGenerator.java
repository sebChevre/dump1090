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

        logger.info(JsonLog.log(
                "TCP Stats generator started",
                EventType.TCP_AGREGATOR_PROCESS_STARTED,
                JsonLog.EMPTY_CORRELATION_ID));


        //error handler
        Action1<Throwable> errrorHandler = throwable ->

                logger.severe(JsonLog.log(
                        String.format("Error during stream processing for statsGenerator stats: %s", throwable.getMessage()),
                        EventType.MESSAGES_STATS_AGGREGATIONING,
                        JsonLog.EMPTY_CORRELATION_ID)
                );

        //complete handler
        Action0 completeHandler = () ->
                logger.severe(JsonLog.log(
                        "Stream complete. This wouldnt happend",
                        EventType.MESSAGES_STATS_AGGREGATIONING,
                        JsonLog.EMPTY_CORRELATION_ID));

        //Traietement des messages venat du bus (flux tcp)
        bus.toObserverable()
                .buffer(1, TimeUnit.MINUTES)            //toutes les minutes
                .map(TCPStats::from)                    //instanciation d'un objet TCPStats
                .subscribe(next -> {
                    int count = next.getTotalMsgsCount();   //total pour log


                    logger.info(JsonLog.log(
                            String.format("%d messages received in last seconds",count),
                            EventType.MESSAGE_STATS_AGGREGATED,
                            String.valueOf(next.getStartTime())));//correlation id startime
                    logger.info(JsonLog.log(
                            String.format("Stats generated: %s", next.toJson()),
                            EventType.MESSAGE_STATS_AGGREGATED,
                            String.valueOf(next.getStartTime()))
                    );

                    logger.info(JsonLog.log(
                            String.format("Stats saving: %s", next.toJson()),
                            EventType.MESSAGE_STATS_SAVING,
                            String.valueOf(next.getStartTime()))
                    );

                    tcpStatsService.saveStats(next);    //persistance db

                    logger.info(JsonLog.log(
                            String.format("Stats saving: %s", next.toJson()),
                            EventType.MESSAGE_STATS_SAVED,
                            String.valueOf(next.getStartTime()))
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
