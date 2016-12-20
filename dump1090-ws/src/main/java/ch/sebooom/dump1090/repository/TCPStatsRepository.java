package ch.sebooom.dump1090.repository;

import ch.sebooom.dump1090.tcp.TCPStats;

import java.util.List;
import java.util.Map;

public interface TCPStatsRepository {

	void save(TCPStats tcpStats);
	
	Map findLastStats();

	List<Map> findByStartAndStopDate(Long start, Long stop);

}
