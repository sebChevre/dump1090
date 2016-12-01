package ch.sebooom.dump1090;

import ch.sebooom.dump1090.tcp.TCPListener;
import ch.sebooom.dump1090.tcp.TCPStatsGenerator;
import org.apache.commons.cli.*;

import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Point d'entrée de l'application
 *
 * Paramètres: -rp (dump109 tcp port, défaut:30003),
 *             -rh (hote dump1090, défaut:localhost)
 *             -sp (port serveur ws, défaut:9999)
 * Exemple: -sp 9898 -rh 192.168.1.109 -rp 30003
 */
class Application {

    private final static Logger logger = Logger.getLogger(Application.class.getName());
    private final static int DEFAULT_REMOTE_PORT = 1234;
    private final static String DEFAULT_REMOTE_HOST = "localhost";
    private final static int DEFAULT_SERVER_PORT = 9999;
    private static int remotePort = DEFAULT_REMOTE_PORT;
    private static int serverPort = DEFAULT_SERVER_PORT;
    private static String remoteHost = DEFAULT_REMOTE_HOST;
    private static RxBus bus = new RxBus();
    //private static ExecutorService service = Executors.newSingleThreadExecutor();


    public static void main(String[] args) {

        try {
            parseArgs(args);

            startServer();

            startTCPStats();

            startTCPListenning();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void startTCPStats() {

        logger.info("Starting tcp stats...");


        Executors.newSingleThreadExecutor().execute(() -> TCPStatsGenerator.newInstance()
                .withBus(bus).start());

    }

    private static void startServer() {
        logger.info("Starting server...");

        Executors.newSingleThreadExecutor().execute(() -> Server.newInstance()
                .withPort(serverPort)
                .withBus(bus).start());

    }

    private static void startTCPListenning() {
        logger.info("Starting tcp listenning...");

        Executors.newSingleThreadExecutor().execute(() -> new TCPListener(remotePort, remoteHost, bus).start());

    }

    private static void parseArgs(String[] args) throws ParseException {
        logger.info("Parsing arguments...");



        Options options  = new Options();
        options.addOption("rp",true,"Port distant tcp du serveur dump1090");
        options.addOption("rh",true,"Hote distant tcp du serveur dump1090");
        options.addOption("sp",true,"Port serveur ws");

        CommandLineParser parser = new PosixParser();

        try{
            CommandLine cmd = parser.parse( options, args);



            if(cmd.getOptions().length == 0){
                logger.config("No arguments defined, default values aplied: remoteHost: "
                        +remoteHost + ":" + remotePort + ", serverPort:" + serverPort);

            }else{
                remotePort = (cmd.hasOption("rp"))?Integer.parseInt(cmd.getOptionValue("rp")):DEFAULT_REMOTE_PORT;
                serverPort = (cmd.hasOption("sp"))?Integer.parseInt(cmd.getOptionValue("sp")):DEFAULT_SERVER_PORT;
                remoteHost = (cmd.hasOption("rh"))?cmd.getOptionValue("rh"):DEFAULT_REMOTE_HOST;
                logger.config("Arguments parsed, remoteHost: " +getArgumentsConfigAsStr() );

            }

        }catch (UnrecognizedOptionException e){
            logger.warning(e.getMessage());
            logger.warning("default value will be applicated: remoteHost: "+
                    getArgumentsConfigAsStr());
        }




    }

    private static String getArgumentsConfigAsStr(){
        return remoteHost + ":" + remotePort + ", serverPort:" + serverPort;
    }

}
