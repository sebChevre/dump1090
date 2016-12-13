package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.RxBus;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.net.Connection;
import rx.functions.Action0;
import rx.functions.Action1;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * TCP msg stats generation
 */
public class TCPStatsGenerator {


    private final static Logger logger = Logger.getLogger(TCPStatsGenerator.class.getName());
    private int port;
    private RxBus bus;
    public static final RethinkDB r = RethinkDB.r;

    private TCPStatsGenerator() {
    }

    public static TCPStatsGenerator newInstance(){
        return new TCPStatsGenerator();
    }

    public void start(){

        logger.info("TCP Stats started");

        Connection connection = r.connection().hostname("localhost").port(28015).connect();
        Table msgCount = r.db("dump1090").table("msgCount");

        Action1<Throwable> errrorHandler = throwable ->
                logger.severe("Error during stream processing for ch.sebooom.dump1090.tcptestserver.tcp stats: "
                + throwable.getMessage());

        Action0 completeHandler = () ->
                logger.severe("Stream complete. This wouldnt happend");

//        bus.toObserverable()
//                //.map(msg -> {
//
//                //})
//                .buffer(10, TimeUnit.SECONDS)
//                .map(TCPStats::from)
//                .subscribe(next -> {
//
//                    int count = next.getCount();
//
//                    HashMap<MessageType, AtomicInteger> msgTypeCount = next.getMessagesByType();
//
//                    //pour chaque type de message
//                    next.getMessagesByType().keySet().forEach(msgType->{
//                        //Si msg pas présent
//
//                        //Get g = msgCount.get(msgType).run(connection);
//                        //r.hashMap()
//
//
//
//                        if(g == msgCount.get(msgType)){
//                            msgCount.insert(
//                                    r.hashMap("id",msgType.toString()
//                                    ).with("count",msgTypeCount.get(msgType))).run(connection);
//                        }else{
//                            GetField a = g.getField("count");
//                            a.
//                            msgCount.get(msgType).update(
//                                    r.hashMap("count",g.getField("count").) )).run(connection);
//                        }
//                    });
//
//                    .insert(next.getMapObject()).run(connection);
//
//                    logger.fine(count + " messages received in last seconds");
//                    logger.fine("Stats:" + next.toJson());
//
//                }, errrorHandler, completeHandler);

        bus.toObserverable()
                .buffer(1, TimeUnit.SECONDS)
                .map(TCPStats::from)
                .subscribe(next -> {

                    int count = next.getCount();

                    logger.fine(count + " messages received in last seconds");
                    logger.fine("Stats:" + next.toJson());

                }, errrorHandler, completeHandler);
    }




    public TCPStatsGenerator withBus(RxBus bus) {
        this.bus = bus;
        return this;
    }
}
