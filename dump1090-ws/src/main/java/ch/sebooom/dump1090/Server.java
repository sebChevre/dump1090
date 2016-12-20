package ch.sebooom.dump1090;

import ch.sebooom.dump1090.messages.TestMessage;
import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.service.TCPStatsService;
import spark.Spark;

import java.util.Map;
import java.util.logging.Logger;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

import static spark.Spark.before;
import static spark.Spark.options;

/**
 * Created by seb on 22.11.16.
 */
public class Server {

	
    private int port;
    private RxBus bus;
    final static Logger logger = Logger.getLogger(Server.class.getName());
    public TCPStatsService statsService;

    public static Server newInstance(TCPStatsService service){
        return new Server(service);
    }

    private Server(TCPStatsService service){
    	this.statsService = service;
    }

    public void start(){
        Spark.port(port);

        Spark.webSocket("/dump1090",new Dump1090WebSocketHandler(bus));

        enableCORS("*","POST, GET","Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

        Spark.get("/test","application/json", (request,response) -> {
            logger.info("/test request ok");
            return new TestMessage();
        }, new JsonTransformer());

        Spark.get("/stats/:level", (request, response) -> {
        	String level = request.params(":level");
        	
        	switch(level){
        		
        		case "all":
        		break;
        		
        		case "last":
        			
        			return statsService.findLastStats();
        			
        			
//        			Connection rethinkDbConnection = r.connection().hostname("localhost").port(28015).connect();
//        			Cursor cursor = null;
//        			
//        			try{
//        				cursor = r.db("dump1090").table("test3").max("start").filter(row -> row.max("start")).run(rethinkDbConnection);
//        			}catch(Exception e){
//        				e.printStackTrace();
//        			}
//        			
//        			Map m = (Map)cursor.toList().get(0);
//        			
//        			for (Object doc : cursor) {
//        			    System.out.println(doc);
//        			}
        		
        		
        	}
        	
            return "Hello: " + request.params(":level");
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

    private static void enableCORS(final String origin, final String methods, final String headers) {

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
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
}
