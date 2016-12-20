package ch.sebooom.dump1090.service;

import ch.sebooom.dump1090.tcp.TCPStats;
import ch.sebooom.dump1090.tcp.TCPStatsAggregator;

import java.util.Map;

public interface TCPStatsService {

	void saveStats(TCPStats next);
	
	Map findLastStats();

	TCPStatsAggregator findByPeriod(Long start, Long stop);

}
