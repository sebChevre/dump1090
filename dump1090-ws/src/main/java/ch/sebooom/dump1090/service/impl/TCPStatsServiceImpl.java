package ch.sebooom.dump1090.service.impl;

import java.util.Map;

import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.service.TCPStatsService;
import ch.sebooom.dump1090.tcp.TCPStats;

public class TCPStatsServiceImpl implements TCPStatsService {
	
	private TCPStatsRepository repository;

	public TCPStatsServiceImpl(TCPStatsRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void saveStats(TCPStats tcpStats) {
		repository.save(tcpStats);
		
	}

	@Override
	public Map findLastStats() {
		return repository.findLastStats();
	}
	
	

}
