package ch.sebooom.dump1090.main;


import ch.sebooom.dump1090.bus.RxBus;
import ch.sebooom.dump1090.http.Server;
import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.repository.impl.TCPStatsMongoDBRepository;
import ch.sebooom.dump1090.repository.impl.TCPStatsRethinkDBRepository;
import ch.sebooom.dump1090.service.TCPStatsService;
import ch.sebooom.dump1090.service.impl.TCPStatsServiceImpl;
import ch.sebooom.dump1090.tcp.TCPListener;
import ch.sebooom.dump1090.tcp.TCPStatsGenerator;
import ch.sebooom.dump1090.utils.Chrono;
import ch.sebooom.dump1090.utils.Dump1090Properties;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Démarre les différents composants de l'application:
 *
 *
 */
class Application {

    private final static Logger logger = Logger.getLogger(Application.class.getName());

    //Bus interne d'échange de données
    private static RxBus bus = new RxBus();
    private static Dump1090Properties properties;
    

    public static void main(String[] args) {

            getProperties();

        	TCPStatsService service = initService();
              
            startTCPListenning();

            startTCPStatsGenerator(service);

            startWebServer(service);

    }

    /**
     * Initilaisation du service
     * @return une instance du service
     */
    private static TCPStatsService initService(){

        return new TCPStatsServiceImpl(initRepository());
    }

    /**
     * Initiliaisation du repository
     * @return une instance du repository
     */
    private static TCPStatsRepository initRepository(){

        Properties props = properties.getProperties();
        String dbType = props.getProperty("dump1090.dbtype");
        TCPStatsRepository impl = null;

        Chrono repo = Chrono.start();
        logger.info(JsonLog.log("Init repository, dbType: " + dbType,
                EventType.REPOSITORY,
                String.valueOf(0))
        );


        switch (dbType){

            case "mongodb":
                String mongoDBHost = props.getProperty("mongodb.host");
                int mongoPort = Integer.parseInt(props.getProperty("mongodb.port"));
                String mongoDb = props.getProperty("mongodb.db");
                String collection = props.getProperty("mongodb.collection");
                impl = new TCPStatsMongoDBRepository(mongoDBHost, mongoPort, mongoDb, collection);
                logger.info(JsonLog.log(
                        "Repository initialized, mongodb",
                        EventType.REPOSITORY,
                        String.valueOf(repo.stop())))
                ;
            break;

            case "rethinkdb":
                String rethinkDBHost = props.getProperty("rethinkdb.host");
                int rethinkDBport = Integer.parseInt(props.getProperty("rethinkdb.port"));
                String rethinkDBdb = props.getProperty("rethinkdb.db");
                String rethinkDBtable = props.getProperty("rethinkdb.table");
                impl = new TCPStatsRethinkDBRepository(rethinkDBHost, rethinkDBport, rethinkDBdb, rethinkDBtable);
                logger.info(JsonLog.log(
                        "Repository initialized, rethinkdb",
                        EventType.REPOSITORY,
                        String.valueOf(repo.stop()))
                );
            break;
        }

        return impl;
    }

    /**
     * Démarage du service de génération de statistiques
     * @param service l'instance du service des stats
     */
    private static void startTCPStatsGenerator(TCPStatsService service) {

        logger.info(JsonLog.log(
                "Starting tcpgenerator ...",EventType.STATS_GENERATION,
                String.valueOf(0))
        );

        Executors.newSingleThreadExecutor().execute(() -> 
        	TCPStatsGenerator.newInstance(service)
                .withBus(bus).start());

    }

    /**
     * Démarrage du serveur http
     * @param service le service injecté
     */
    private static void startWebServer(TCPStatsService service) {
        int port = Integer.parseInt(properties.getProperties().getProperty("server.port"));

        logger.info(JsonLog.log(
                "Starting server on port: " + port,
                EventType.WEB_SERVER,
                String.valueOf(0))
        );

        Executors.newSingleThreadExecutor().execute(() -> Server.newInstance(service)
                .withPort(port)
                .withBus(bus).start());

    }

    /**
     * Démarrage du thread d'écoute et de traitement des trames tcp
     */
    private static void startTCPListenning() {

        String tcpHost = properties.getProperties().getProperty("dump1090.tcp.host");
        int tcpPort = Integer.parseInt(properties.getProperties().getProperty("dump1090.tcp.port"));

        logger.info(JsonLog.log(
                "Starting listenning dump1090tcp server ["
                + tcpHost + ":" + tcpPort + "]",
                EventType.TCP_LISTENNING,
                String.valueOf(0))
        );

        Executors.newSingleThreadExecutor().execute(() ->
                new TCPListener(tcpPort, tcpHost, bus).start());



    }


    /**
     * Récupération des propriétés
     * Si problème, fin de l'application
     */
    private static void getProperties() {

        logger.info(JsonLog.log("Parsing properties app...",
                EventType.PROPERTIES,
                String.valueOf(0)));

        try {
            properties = Dump1090Properties.get();
            logger.info(JsonLog.log("Properties app succesfully parsed",
                    EventType.PROPERTIES,
                    String.valueOf(0)));

        } catch (Exception e) {
            logger.severe(JsonLog.log(
                    "Properties files problem: " + e.getMessage(),
                    EventType.PROPERTIES,
                    String.valueOf(0)));
            logger.severe(JsonLog.log(
                    "Application will exit now!",
                    EventType.PROPERTIES,
                    String.valueOf(0)));
            System.exit(1);
        }

    }

}
