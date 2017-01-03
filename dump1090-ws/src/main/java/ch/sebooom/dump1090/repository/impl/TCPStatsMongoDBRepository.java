package ch.sebooom.dump1090.repository.impl;

import ch.sebooom.dump1090.messages.sbs1.ICAOIdent;
import ch.sebooom.dump1090.messages.sbs1.MessageType;
import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.tcp.TCPStats;
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
    }

    @Override
    public void save(TCPStats tcpStats) {
        collection.insertOne(getDocumentObject(tcpStats));
    }

    @Override
    public Map findLastStats() {


        List<Document> foundDocument = collection.find().into(new ArrayList<Document>());



        List<Document> docs = collection.find()
                .sort(descending("timing.stop_millis")).limit(1).into(new ArrayList<Document>());

        Document doc = docs.get(0);

        Map m = convertBSONToMap(doc);

        return m;
    }

    private Map convertBSONToMap(Document doc) {
        Map m = new HashMap();

        doc.forEach((key,value)->{
            m.put(key,value);
        });
        return m;
    }

    @Override
    public List<Map> findByStartAndStopDate(Long start, Long stop) {

        List<Map> returnList = new ArrayList<Map>();

        List<Document> docs = collection.find(and(
                gte("timing.start_millis",start),lte("timing.stop_millis",stop)))
                .into(new ArrayList<Document>());



        docs.forEach(doc -> {
            Map m = convertBSONToMap(doc);

            returnList.add(m);
        });



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
                .append("total",tcpStats.getTotalCount()))
            .append("timing",new Document()
                .append("duration",tcpStats.getTotalTime())
                .append("start",start)
                .append("start_millis",tcpStats.getStartTime())
                .append("stop",stop)
                .append("stop_millis",tcpStats.getStopTime()));


    }
}
