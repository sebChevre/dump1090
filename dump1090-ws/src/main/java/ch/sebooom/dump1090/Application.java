package ch.sebooom.dump1090;


import ch.sebooom.dump1090.http.Server;
import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.repository.impl.TCPStatsRethinkDBRepository;
import ch.sebooom.dump1090.service.TCPStatsService;
import ch.sebooom.dump1090.service.impl.TCPStatsServiceImpl;
import ch.sebooom.dump1090.tcp.TCPListener;
import ch.sebooom.dump1090.tcp.TCPStatsGenerator;
import org.apache.commons.cli.MissingArgumentException;

import java.io.FileInputStream;
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
    private static Properties properties = new Properties();
    

    public static void main(String[] args) {

            validProperties();

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

        String rethinkDBHost = properties.getProperty("rethinkdb.host");
        int port = Integer.parseInt(properties.getProperty("rethinkdb.port"));
        String db = properties.getProperty("rethinkdb.db");

        return new TCPStatsRethinkDBRepository(rethinkDBHost, port, db);
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
        int port = Integer.parseInt(properties.getProperty("server.port"));

        logger.info("Starting server on port: " + port);

        Executors.newSingleThreadExecutor().execute(() -> Server.newInstance(service)
                .withPort(port)
                .withBus(bus).start());

    }

    /**
     * Démarrage du thread d'écoute et de traitement des trames tcp
     */
    private static void startTCPListenning() {

        String tcpHost = properties.getProperty("dump1090.tcp.host");
        int tcpPort = Integer.parseInt(properties.getProperty("dump1090.tcp.port"));

        logger.info("Starting listenning dump1090tcp server ["
                + tcpHost + ":" + tcpPort + "]");

        Executors.newSingleThreadExecutor().execute(() ->
                new TCPListener(tcpPort, tcpHost, bus).start());

    }


    /**
     * Validation des propriétés
     * Si probléme, fin de l'application
     */
    private static void validProperties() {

        try {
            properties.load(new FileInputStream("./config/application.properties"));
            checkMandatoryProperties();
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
    private static void checkMandatoryProperties() throws MissingArgumentException {
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

        if(propertiesMissing){
            logger.severe("Application will exit now!");
            System.exit(1);
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

        if(propertiesMissing){
            logger.severe("Application will exit now!");
            throw new MissingArgumentException("At least one properties missing, check log");
        }
        
        
    }

}
