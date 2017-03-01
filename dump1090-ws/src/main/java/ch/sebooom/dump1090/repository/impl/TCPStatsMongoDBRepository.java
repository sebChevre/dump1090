package ch.sebooom.dump1090.repository.impl;

import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
import ch.sebooom.dump1090.messages.sbs1.ICAOIdent;
import ch.sebooom.dump1090.messages.sbs1.MessageType;
import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.tcp.TCPStats;
import ch.sebooom.dump1090.utils.Chrono;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class TCPStatsMongoDBRepository implements TCPStatsRepository{

    private MongoCollection<Document> collection;
    private final static Logger logger = Logger.getLogger(TCPStatsMongoDBRepository.class.getName());

    public TCPStatsMongoDBRepository(String host, int port, String db, String mongoCollection){
        String connectionString = String.format("mongodb://%s:%d",host,port);
        MongoClient mongoClient = new MongoClient(new MongoClientURI(connectionString));
        MongoDatabase database = mongoClient.getDatabase(db);
        collection = database.getCollection(mongoCollection);
        logger.info(JsonLog.technical(String.format("MongoDBRepository initialized: %s",connectionString),
                EventType.REPOSITORY,0));
    }

    @Override
    public void save(TCPStats tcpStats) {

        Chrono c = Chrono.start();
        logger.info(JsonLog.technical(
                String.format("Saving stats"),
                EventType.STATS_SAVING,0))
        ;

        collection.insertOne(getDocumentObject(tcpStats));

        logger.info(JsonLog.technical(
                String.format("Stats saved sucessfully:%s",tcpStats),
                EventType.STATS_SAVING,c.stop()))
        ;
    }

    @Override
    public Map findLastStats() {

        Chrono c = Chrono.start();

        logger.info(JsonLog.technical(
                "Finding last stats",
                EventType.STATS_FIND_LAST,0))
        ;

        List<Document> foundDocument = collection.find().into(new ArrayList<>());

        List<Document> docs = collection.find()
                .sort(descending("timing.stop_millis")).limit(1).into(new ArrayList<>());

        Map returnMap = convertBSONToMap(docs.get(0));

        logger.info(JsonLog.technical(
                "Last stats find",
                EventType.STATS_FIND_LAST,c.stop()))
        ;

        return returnMap;
    }

    private Map convertBSONToMap(Document doc) {
        Map m = new HashMap();

        doc.forEach((key,value)-> m.put(key,value));
        return m;
    }

    @Override
    public List<Map> findByStartAndStopDate(Long start, Long stop) {

        Chrono c = Chrono.start();
        logger.info(JsonLog.technical(
                String.format("Finding period stats [%d - %d]",start,stop),
                EventType.STATS_FIND_PERIOD,0))
        ;

        List<Map> returnList = new ArrayList<>();

        List<Document> docs = collection.find(and(
                gte("timing.start_millis",start),lte("timing.stop_millis",stop)))
                .into(new ArrayList<>());



        docs.forEach(doc -> {
            Map m = convertBSONToMap(doc);

            returnList.add(m);
        });

        logger.info(JsonLog.technical(
                String.format("Find period stats [%d - %d]",start,stop),
                EventType.STATS_FIND_PERIOD,c.stop()))
        ;



        return returnList;

    }

    private Document getDocumentObject(TCPStats tcpStats){

        Document byType = new Document();

        //map message <-> nbreOccurence
        HashMap<MessageType, AtomicInteger> messagesByType = tcpStats.getMessagesByType();

        messagesByType.keySet().forEach(messageType -> {
            byType.append(messageType.toString(),messagesByType.get(messageType));
            //msgs.with(messageType.toString(),messagesByType.get(messageType));
        });

        Document byPlane = new Document();

        //map message <-> nbreOccurence
        HashMap<ICAOIdent, AtomicInteger> messageByPlane = tcpStats.getMessagesByPlane();

        messageByPlane.keySet().forEach(plane -> {
            byPlane.append(plane.icaoIdent(),messageByPlane.get(plane));
            //msgs.with(messageType.toString(),messagesByType.get(messageType));
        });



        Date start = new Date(tcpStats.getStartTime());
        Date stop = new Date(tcpStats.getStopTime());

        return new Document("msgs",
                new Document("byType",byType)
                .append("byPlane",byPlane)
                .append("total_msgs",tcpStats.getTotalMsgsCount())
                .append("total_planes",tcpStats.getTotalPlanesCount()))
            .append("timing",new Document()
                .append("duration",tcpStats.getTotalTime())
                .append("start",start)
                .append("start_millis",tcpStats.getStartTime())
                .append("stop",stop)
                .append("stop_millis",tcpStats.getStopTime()));


    }
}
