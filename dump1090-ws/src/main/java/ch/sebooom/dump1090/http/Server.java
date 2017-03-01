package ch.sebooom.dump1090.http;

import ch.sebooom.dump1090.bus.RxBus;
import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
import ch.sebooom.dump1090.messages.JsonTransformer;
import ch.sebooom.dump1090.messages.TestMessage;
import ch.sebooom.dump1090.service.TCPStatsService;

import java.util.logging.Logger;

import static spark.Spark.*;

/**
 * Server http basé sur Spark
 */
public class Server {

	
    private int port;
    private RxBus bus;
    static final Logger logger = Logger.getLogger(Server.class.getName());
    public TCPStatsService statsService;

    public static Server newInstance(TCPStatsService service){
        return new Server(service);
    }

    private Server(TCPStatsService service){
    	this.statsService = service;
    }

    public void start(){

        port(port);

        webSocket(HttpPaths.WS_ENDPOINT.path,new Dump1090WebSocketHandler(bus));

        enableCORS();

        get(HttpPaths.REST_TEST.path, (request,response) -> {

            logger.info(
                    JsonLog.technical(
                            String.format("GET/ %s complete",HttpPaths.REST_TEST.path),
                            EventType.REST_GET_TEST,0)
            );

            return new TestMessage();


        }, new JsonTransformer());

        get(HttpPaths.REST_STATS_LAST.path, (request, response) -> {
            logger.info(
                    JsonLog.technical(
                            String.format("GET/ %s complete",HttpPaths.REST_STATS_LAST.path),
                            EventType.REST_GET,0)
            );
        	return statsService.findLastStats();
        	
        }, new JsonTransformer());
        
        get(HttpPaths.REST_STATS_PERIOD.path, (request, response) -> {
            logger.info(
                    JsonLog.technical(
                            String.format("GET/ %s complete",HttpPaths.REST_STATS_PERIOD.path),
                            EventType.REST_GET,0)
            );

        	Long start = Long.valueOf(request.params("from"));
        	Long stop = Long.valueOf(request.params("to"));
        	
        	return statsService.findByPeriod(start, stop);
        	
        }, new JsonTransformer());

        init();

        logger.info(JsonLog.technical(
                String.format("Server started on port : %d",port),
                EventType.WEB_SERVER,0));
    }


    public  Server withPort(int port){
        this.port = port;
        return this;
    }

    public Server withBus(RxBus bus) {
        this.bus = bus;
        return this;
    }

    /**
     * Enable CORS request for all rest endpoint
     */
    private static void enableCORS() {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "POST, GET");
            response.header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
            response.type("application/json");
        });
    }
}
