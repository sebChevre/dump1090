package ch.sebooom.dump1090;


import ch.sebooom.dump1090.http.Server;
import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.repository.impl.TCPStatsMongoDBRepository;
import ch.sebooom.dump1090.repository.impl.TCPStatsRethinkDBRepository;
import ch.sebooom.dump1090.service.TCPStatsService;
import ch.sebooom.dump1090.service.impl.TCPStatsServiceImpl;
import ch.sebooom.dump1090.tcp.TCPListener;
import ch.sebooom.dump1090.tcp.TCPStatsGenerator;
import ch.sebooom.dump1090.utils.Dump1090Properties;
import org.apache.commons.cli.MissingArgumentException;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Point d'entrée de l'application
 *
 */
class Application {

    private final static Logger logger = Logger.getLogger(Application.class.getName());
    //Bus interne d'échange de données
    private static RxBus bus = new RxBus();
    private static Dump1090Properties properties;
    

    public static void main(String[] args) {

            parsePorperties();

        	TCPStatsService service = initService();
              
            startTCPListenning();

            startTCPStats(service);

            startServer(service);

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

        switch (dbType){

            case "mongodb":
                String mongoDBHost = props.getProperty("mongodb.host");
                int mongoPort = Integer.parseInt(props.getProperty("mongodb.port"));
                String mongoDb = props.getProperty("mongodb.db");
                String collection = props.getProperty("mongodb.collection");
                impl = new TCPStatsMongoDBRepository(mongoDBHost, mongoPort, mongoDb, collection);
            break;

            case "rethinkdb":
                String rethinkDBHost = props.getProperty("rethinkdb.host");
                int rethinkDBport = Integer.parseInt(props.getProperty("rethinkdb.port"));
                String rethinkDBdb = props.getProperty("rethinkdb.db");
                String rethinkDBtable = props.getProperty("rethinkdb.table");
                impl = new TCPStatsRethinkDBRepository(rethinkDBHost, rethinkDBport, rethinkDBdb, rethinkDBtable);
            break;
        }

        return impl;
    }

    /**
     * Démarage du service de statistique
     * @param service l'instance du service des stats
     */
    private static void startTCPStats(TCPStatsService service) {

        logger.info("Starting ch.sebooom.dump1090.tcptestserver.tcp stats...");

        Executors.newSingleThreadExecutor().execute(() -> 
        	TCPStatsGenerator.newInstance(service)
                .withBus(bus).start());

    }

    /**
     * Démarrage du serveur http
     * @param service
     */
    private static void startServer(TCPStatsService service) {
        int port = Integer.parseInt(properties.getProperties().getProperty("server.port"));

        logger.info("Starting server on port: " + port);

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

        logger.info("Starting listenning dump1090tcp server ["
                + tcpHost + ":" + tcpPort + "]");

        Executors.newSingleThreadExecutor().execute(() ->
                new TCPListener(tcpPort, tcpHost, bus).start());

    }


    /**
     * Validation des propriétés
     * Si probléme, fin de l'application
     */
    private static void parsePorperties() {

        try {
            properties = Dump1090Properties.get();

        } catch (Exception e) {
            logger.severe("Properties files problem: " + e.getMessage());
            logger.severe("Application will exit now!");
            System.exit(1);
        }

    }

    /**
     * Validation des propriétés applicative
     * @throws MissingArgumentException levé si au moins une propriété manque
     */
   /* private static void checkMandatoryProperties() throws MissingArgumentException {
        boolean propertiesMissing = false;

        if(properties.get("server.port") == null){
            logger.severe("Property [server.port] not found.");
            propertiesMissing = true;
        }

        if(properties.get("dump1090.tcp.port") == null){
            logger.severe("Property [dump1090.tcp.port] not found.");
            propertiesMissing = true;
        }

        if(properties.get("dump1090.tcp.host") == null){
            logger.severe("Property [dump1090.tcp.host] not found.");
            propertiesMissing = true;
        }

        if(properties.get("dump1090.dbtype") == null){
            logger.severe("Property [dump1090.dbtype] not found.");
            propertiesMissing = true;
        }


        if(properties.get("rethinkdb.host") == null){
            logger.severe("Property [rethinkdb.host] not found.");
            propertiesMissing = true;
        }
        
        if(properties.get("rethinkdb.port") == null){
            logger.severe("Property [rethinkdb.port] not found.");
            propertiesMissing = true;
        }
        
        if(properties.get("rethinkdb.db") == null){
            logger.severe("Property [rethinkdb.db] not found.");
            propertiesMissing = true;
        }

        if(properties.get("rethinkdb.table") == null){
            logger.severe("Property [rethinkdb.table] not found.");
            propertiesMissing = true;
        }

        if(propertiesMissing){
            logger.severe("At least one mandatory properties missing");
            throw new MissingArgumentException("At least one properties missing, check log");
        }
        
        
    }*/

}
