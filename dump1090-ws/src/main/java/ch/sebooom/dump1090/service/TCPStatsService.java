package ch.sebooom.dump1090.service;

import java.util.Map;

import ch.sebooom.dump1090.tcp.TCPStats;

public interface TCPStatsService {

	public void saveStats(TCPStats next);
	
	public Map findLastStats();

}
