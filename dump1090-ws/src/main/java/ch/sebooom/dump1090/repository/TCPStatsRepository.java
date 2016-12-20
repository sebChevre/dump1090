package ch.sebooom.dump1090.repository;

import java.util.Map;

import ch.sebooom.dump1090.tcp.TCPStats;

public interface TCPStatsRepository {

	void save(TCPStats tcpStats);
	
	Map findLastStats();

}
