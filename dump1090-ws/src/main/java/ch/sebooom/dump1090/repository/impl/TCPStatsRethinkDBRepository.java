package ch.sebooom.dump1090.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Max;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;

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

        return  new MapObject().with("msgs",new MapObject().with("byType",msgs))
        		.with("date", new MapObject()
        				.with("total",tcpStats.getTotalCount())
        				.with("start",tcpStats.getStartTime())
        				.with("stop",tcpStats.getStopTime())
        				.with("stop_date", new Date(tcpStats.getStopTime()).toString())
        				.with("start_date", new Date(tcpStats.getStartTime()).toString()))
                .with("duration",tcpStats.getTotalTime());
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
