package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.RxBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by seb on 22.11.16.
 */
public class TCPListener {

    private int port;
    private String host;
    private RxBus bus;
    private Boolean isRunning = Boolean.FALSE;

    public TCPListener(int port, String host ,RxBus bus){
        this.bus = bus;
        this.port = port;
        this.host = host;
    }


    public void start(){

        isRunning = Boolean.TRUE;


            try {
                Socket skt = new Socket("localhost", 1234);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(skt.getInputStream()));

                while(isRunning){
                    System.out.println(Thread.currentThread().getName() + " running");
                    while (!in.ready()) {}
                    bus.send(in.readLine());
                }

                in.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }




    }


}
