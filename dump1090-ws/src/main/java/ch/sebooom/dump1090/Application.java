package ch.sebooom.dump1090;

import ch.sebooom.dump1090.tcp.TCPListener;
import org.apache.commons.cli.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Point d'entrée de l'application
 *
 * Paramètres: -rp (dump109 tcp port, défaut:30003),
 *             -rh (hote dump1090, défaut:localhost)
 *             -sp (port serveur ws, défaut:9999)
 */
public class Application {

    final static Logger logger = Logger.getLogger(Application.class.getName());
    private final static int DEFAULT_REMOTE_PORT = 1234;
    private final static String DEFAULT_REMOTE_HOST = "localhost";
    private final static int DEFAULT_SERVER_PORT = 9999;
    private static int remotePort = DEFAULT_REMOTE_PORT;
    private static int serverPort = DEFAULT_SERVER_PORT;
    private static String remoteHost = DEFAULT_REMOTE_HOST;
    private static RxBus bus = new RxBus();


    public static void main(String[] args) {

        try {
            parseArgs(args);

            startServer();

            startTCPListenning();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void startServer() {


        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(() -> {
            Server.newInstance()
                    .withPort(serverPort)
                    .withBus(bus).start();

        });

    }

    private static void startTCPListenning() {

        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(() -> {
            new TCPListener(remotePort,remoteHost,bus).start();
        });

    }

    private static void parseArgs(String[] args) throws ParseException {

        Options options  = new Options();
        options.addOption("rp",true,"Port distant tcp du serveur dump1090");
        options.addOption("rh",true,"Hote distant tcp du serveur dump1090");
        options.addOption("sp",true,"Port serveur ws");

        CommandLineParser parser = new PosixParser();

        try{
            CommandLine cmd = parser.parse( options, args);

            if(cmd.getOptions().length == 0){
                logger.info("No arguments defined, default values aplied: remoteHost: "
                        +remoteHost + ":" + remotePort + ", serverPort:" + serverPort);

            }else{
                remotePort = (cmd.hasOption("rp"))?Integer.parseInt(cmd.getOptionValue("rp")):DEFAULT_REMOTE_PORT;
                serverPort = (cmd.hasOption("sp"))?Integer.parseInt(cmd.getOptionValue("sp")):DEFAULT_SERVER_PORT;
                remoteHost = (cmd.hasOption("rh"))?cmd.getOptionValue("rh"):DEFAULT_REMOTE_HOST;
                logger.info("Arguments parsed, remoteHost: " +remoteHost + ":" + remotePort + ", serverPort:" + serverPort);

            }


        }catch (UnrecognizedOptionException e){
            logger.info(e.getMessage());
            logger.info("default value will be applicated: remoteHost: "
                    +remoteHost + ":" + remotePort + ", serverPort:" + serverPort);
        }




    }

}
