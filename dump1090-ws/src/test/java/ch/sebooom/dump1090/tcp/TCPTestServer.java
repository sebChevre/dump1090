package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.tcp.messages.SBS1MessagesSamples;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Serveur tcp test générant des messages SBS1 aléatoire
 *
 */
public class TCPTestServer {

    final static int port = 1234;
    final static int nbThread = 4;
    final static Logger logger = Logger.getLogger(TCPTestServer.class.getName());
    static boolean running = Boolean.TRUE;
    static Random random = new Random();

    public static void main(String args[]) throws IOException {


        ExecutorService executorService = Executors.newFixedThreadPool(nbThread);
        ServerSocket serverSocket = null;


        try {
            boolean running = true;
            serverSocket = new ServerSocket(port);
            logger.info("starting listenning");
            while(running){
                Socket clientSocket = serverSocket.accept();

                executorService.execute(() -> {
                    try {
                        logger.info("new client listenning [" + Thread.currentThread().getName() + "]");
                        new ClientTcpSocketHandler(clientSocket);
                    } catch (IOException e) {
                        logger.severe(e.getMessage());
                    }
                });

            }


        }
        catch(Exception e) {
            logger.severe("Application exit: " + e.getMessage());
            serverSocket.close();
        } finally {
            System.exit(1);
        }
    }

    private static void ClientSocketHandler(Socket clientSocket, PrintWriter out) throws InterruptedException, IOException {
        while (running) {

            out.print(SBS1MessagesSamples.getRandomMsg());
            System.out.println("Message sent");
            out.flush();

            randomSleep();
        }

        out.close();
        clientSocket.close();
    }

    private static void randomSleep() throws InterruptedException {
        int maxSleepDuration = 4;

        TimeUnit.SECONDS.sleep(random.nextInt(maxSleepDuration) + 1);//pas de 0
    }
}
