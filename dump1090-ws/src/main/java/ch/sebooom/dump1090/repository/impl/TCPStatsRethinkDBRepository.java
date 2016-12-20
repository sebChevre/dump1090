package ch.sebooom.dump1090.repository.impl;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Max;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.gen.ast.Time;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

import ch.sebooom.dump1090.messages.sbs1.MessageType;
import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.tcp.TCPStats;

public class TCPStatsRethinkDBRepository implements TCPStatsRepository{

	public static final RethinkDB r = RethinkDB.r;
	private Connection rethinkDbConnection;
	private  Table statsTable;
	private static final String STATS_TABLE_NAME = "test3";
	
	public TCPStatsRethinkDBRepository(String rethinkDBHost, int port, String db) {
		rethinkDbConnection = r.connection().hostname("localhost").port(28015).connect();
		statsTable = r.db(db).table(STATS_TABLE_NAME);
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
        		.with("total",tcpStats.getTotalCount()))
        		.with("timing", new MapObject()
        				.with("duration",tcpStats.getTotalTime())
        				.with("start",start)
        				.with("stop",stop));
                
	}

	@Override
	public Map findLastStats() {

		Map a = null;
		
		try{
			a = statsTable.max(stats -> stats.g("date").g("start")).run(rethinkDbConnection);
			System.out.println(a);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return  a;
	}
	
	

}
