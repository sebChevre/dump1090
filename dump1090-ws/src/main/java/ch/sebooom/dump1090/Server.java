package ch.sebooom.dump1090;

import spark.Spark;

/**
 * Created by seb on 22.11.16.
 */
public class Server {


    private int port;
    private RxBus bus;

    public static Server newInstance(){
        return new Server();
    }

    private Server(){}

    public void start(){
        Spark.port(port);

        Spark.webSocket("/dump1090",new WSTCPDump1090WebSocketHandler(bus));

        Spark.get("/test", (request,response) -> {
            return "ok";
        });

        Spark.init();




    }


    public  Server withPort(int port){
        this.port = port;
        return this;
    }

    public Server withBus(RxBus bus) {
        this.bus = bus;
        return this;
    }
}
