package ch.sebooom.dump1090.repository.impl;

import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
import ch.sebooom.dump1090.messages.sbs1.MessageType;
import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.tcp.TCPStats;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.gen.ast.Time;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class TCPStatsRethinkDBRepository implements TCPStatsRepository{

	public static final RethinkDB r = RethinkDB.r;
	private Connection rethinkDbConnection;
	private  Table statsTable;
	private static final String STATS_TABLE_NAME = "test3";
	private final static Logger logger = Logger.getLogger(TCPStatsRethinkDBRepository.class.getName());


	public TCPStatsRethinkDBRepository(String rethinkDBHost, int port, String db, String table) {
		rethinkDbConnection = r.connection().hostname("localhost").port(28015).connect();
		statsTable = r.db(db).table(table);
		logger.info(JsonLog.log(
				String.format("RethonkDBRepository initialized: %s",rethinkDbConnection.toString()),
				EventType.REPOSITORY,
				JsonLog.EMPTY_CORRELATION_ID));
	}

	@Override
	public void save(TCPStats tcpStats) {
		statsTable.insert(getMapObject(tcpStats)).run(rethinkDbConnection);
	}
	
	
	private MapObject getMapObject(TCPStats tcpStats){
		
		MapObject msgs = new MapObject();
		
		//map message <-> nbreOccurence
		HashMap<MessageType, AtomicInteger> messagesByType = tcpStats.getMessagesByType();

        messagesByType.keySet().forEach(messageType -> {
            msgs.with(messageType.toString(),messagesByType.get(messageType));
        });

        Calendar cstart = Calendar.getInstance();
        cstart.setTime(new Date(tcpStats.getStartTime()));
        
        Calendar cstop = Calendar.getInstance();
        cstop.setTime(new Date(tcpStats.getStopTime()));
        
        Time start = r.time(cstart.get(Calendar.YEAR), cstart.get(Calendar.MONTH)+1, cstart.get(Calendar.DAY_OF_MONTH), 
        		cstart.get(Calendar.HOUR_OF_DAY),cstart.get(Calendar.MINUTE),cstart.get(Calendar.SECOND),"Z");
        
        Time stop = r.time(cstop.get(Calendar.YEAR), cstop.get(Calendar.MONTH)+1, cstop.get(Calendar.DAY_OF_MONTH), 
        		cstop.get(Calendar.HOUR_OF_DAY),cstop.get(Calendar.MINUTE),cstop.get(Calendar.SECOND),"Z");
        
        
        
        
        
        return  new MapObject().with("msgs",new MapObject().with("byType",msgs)
        		.with("total",tcpStats.getTotalMsgsCount()))
        		.with("timing", new MapObject()
        				.with("duration",tcpStats.getTotalTime())
        				.with("start",start)
						.with("start_millis",tcpStats.getStartTime())
        				.with("stop",stop)
								.with("stop_millis",tcpStats.getStopTime()));

                
	}

	@Override
	public Map findLastStats() {

		Map a = null;
		
		try{
			a = statsTable.max(stats -> stats.g("timing").g("start")).run(rethinkDbConnection);
			System.out.println(a);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return  a;
	}

	@Override
	public List<Map> findByStartAndStopDate(Long start, Long stop) {


		LocalDateTime startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		LocalDateTime stopDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(stop), ZoneId.systemDefault());

		Time startTime = r.time(startDate.getYear(),startDate.getMonth().getValue(),startDate.getDayOfMonth()
				,startDate.getHour(),startDate.getMinute(),startDate.getSecond(),"Z");
		Time stopTime = r.time(stopDate.getYear(),stopDate.getMonth().getValue(),stopDate.getDayOfMonth()
				,stopDate.getHour(),stopDate.getMinute(),stopDate.getSecond(),"Z");

		List c = null;


		try{
			c = statsTable.filter(row ->
					row.g("timing").g("start").during(startTime, stopTime)
			).orderBy(r.asc(doc ->
					doc.g("timing").g("start")
			)).run(rethinkDbConnection);

		}catch(Exception e){
			e.printStackTrace();
		}

		return c;
	}


}
