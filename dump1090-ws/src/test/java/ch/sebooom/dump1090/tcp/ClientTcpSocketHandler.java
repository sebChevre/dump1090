package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.tcp.messages.SBS1MessagesSamples;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by seb on 01.12.16.
 */
class ClientTcpSocketHandler {

    private static Random random = new Random();
    private PrintWriter out;

    public ClientTcpSocketHandler(Socket clientSocket) throws IOException {

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            listen();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        } finally {
            out.close();
            clientSocket.close();
        }

    }

    private static void randomSleep() throws InterruptedException {
        int maxSleepDurationMs = 1000;
        int minSleepDurationMs = 100;


        TimeUnit.MILLISECONDS.sleep(Math.max(minSleepDurationMs, random.nextInt(maxSleepDurationMs)));//min  1000
    }

    private void listen() throws InterruptedException {
        while (true) {

            out.print(SBS1MessagesSamples.getRandomMsg());
            System.out.println("Message sent");
            out.flush();

            randomSleep();
        }
    }
}
