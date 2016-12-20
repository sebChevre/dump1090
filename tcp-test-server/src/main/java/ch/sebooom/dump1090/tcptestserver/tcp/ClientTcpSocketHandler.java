package ch.sebooom.dump1090.tcptestserver.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by seb on 01.12.16.
 */
class ClientTcpSocketHandler {

    private final static Logger logger = Logger.getLogger(ClientTcpSocketHandler.class.getName());
    private static Random random = new Random();
    private PrintWriter out;
    private Socket clientSocket;

    public ClientTcpSocketHandler(Socket clientSocket) throws IOException {

        try {
            this.clientSocket = clientSocket;
            out = new PrintWriter(this.clientSocket.getOutputStream(), true);

        } catch (IOException e) {
            logger.severe("Exception during tcp client handler :" + e.getMessage());
            logger.severe("Client will close now");

            out.close();
            clientSocket.close();

        }

    }

    /**
     * Mise en attente du thread
     * Entre 100 et 1000ms
     * @throws InterruptedException
     */
    private static void randomSleep() {
        int maxSleepDurationMs = 1000;
        int minSleepDurationMs = 100;

        try {
            TimeUnit.MILLISECONDS.sleep(Math.max(minSleepDurationMs, random.nextInt(maxSleepDurationMs)));//min  1000
        } catch (InterruptedException e) {
            logger.severe("Exception during random sleep :" + e.getMessage());
        }
    }

    /**
     * Démarrage de l'écoute du client
     * @throws InterruptedException
     */
    public void listen()  {
        while (this.clientSocket.isConnected()) {
            String msg = SBS1MessagesSamples.getRandomMsg();

            //logger.fine("Message try..");
            out.print(msg);
            logger.fine("Message sent: " + msg);
            out.flush();

            randomSleep();
        }

        try {
            this.clientSocket.close();
        } catch (IOException e) {
            logger.severe("Client socket close error: " + e.getMessage());
        }
    }
}
