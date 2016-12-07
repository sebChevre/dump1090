package ch.sebooom.dump1090.tcptestserver.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Serveur ch.sebooom.dump1090.tcptestserver.tcp test générant des messages SBS1 aléatoire
 *
 */
public class TCPTestServer {

    private final static int port = 1234;
    private final static int nbThread = 4;
    private final static Logger logger = Logger.getLogger(TCPTestServer.class.getName());
    private static Random random = new Random();

    public static void main(String args[]) throws IOException {


        ExecutorService executorService = Executors.newFixedThreadPool(nbThread);
        ServerSocket serverSocket = null;


        try {

            serverSocket = new ServerSocket(port);

            while(true){
                Socket clientSocket = serverSocket.accept();

                executorService.execute(() -> {
                    try {
                        logger.info("new client listenning [" + Thread.currentThread().getName() + "]");
                        new ClientTcpSocketHandler(clientSocket).listen();
                    } catch (IOException e) {
                        logger.severe(e.getMessage());
                    }
                });

            }

        }
        catch(Exception e) {
            logger.severe("Application exit: " + e.getMessage());
            serverSocket.close();
            System.exit(1);
        }
    }


    private static void randomSleep() throws InterruptedException {
        int maxSleepDuration = 4;

        TimeUnit.SECONDS.sleep(random.nextInt(maxSleepDuration) + 1);//pas de 0
    }
}
