package ch.sebooom.dump1090;

import spark.Spark;

import java.util.logging.Logger;

/**
 * Created by seb on 22.11.16.
 */
public class Server {


    private int port;
    private RxBus bus;
    final static Logger logger = Logger.getLogger(Server.class.getName());

    public static Server newInstance(){
        return new Server();
    }

    private Server(){}

    public void start(){
        Spark.port(port);

        Spark.webSocket("/dump1090",new Dump1090WebSocketHandler(bus));

        Spark.get("/test", (request,response) -> {
            logger.fine("/test request ok");
            return "ok";
        });

        Spark.init();

        logger.info("Server started on port :" + port);


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
