package ch.sebooom.dump1090;


import ch.sebooom.dump1090.tcp.TCPListener;
import ch.sebooom.dump1090.tcp.TCPStatsGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Point d'entrée de l'application
 *
 * Paramètres: -rp (dump109 ch.sebooom.dump1090.tcptestserver.tcp port, défaut:30003),
 *             -rh (hote dump1090, défaut:localhost)
 *             -sp (port serveur ws, défaut:9999)
 * Exemple: -sp 9898 -rh 192.168.1.109 -rp 30003
 */
class Application {

    private final static Logger logger = Logger.getLogger(Application.class.getName());

    private static RxBus bus = new RxBus();
    private static Properties properties = new Properties();


    public static void main(String[] args) {

            readProperties();

            startTCPListenning();

            startTCPStats();

            startServer();

    }

    private static void startTCPStats() {

        logger.info("Starting ch.sebooom.dump1090.tcptestserver.tcp stats...");


        Executors.newSingleThreadExecutor().execute(() -> TCPStatsGenerator.newInstance()
                .withBus(bus).start());

    }

    private static void startServer() {
        int port = Integer.parseInt(properties.getProperty("server.port"));

        logger.info("Starting server on port: " + port);

        Executors.newSingleThreadExecutor().execute(() -> Server.newInstance()
                .withPort(port)
                .withBus(bus).start());

    }

    private static void startTCPListenning() {
        String tcpHost = properties.getProperty("dump1090.tcp.host");
        int tcpPort = Integer.parseInt(properties.getProperty("dump1090.tcp.port"));

        logger.info("Starting listenning dump1090tcp server ["
                + tcpHost + ":" + tcpPort + "]");

        Executors.newSingleThreadExecutor().execute(() ->
                new TCPListener(tcpPort, tcpHost, bus).start());

    }


    private static void readProperties () {

        try {
            properties.load(new FileInputStream("./config/application.properties"));
            checkMandatoryProperties();
        } catch (IOException e) {
            logger.severe("Properties files problem: " + e.getMessage());
            logger.severe("Application will exit now!");
        }

    }

    private static void checkMandatoryProperties() {
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
    }

}
