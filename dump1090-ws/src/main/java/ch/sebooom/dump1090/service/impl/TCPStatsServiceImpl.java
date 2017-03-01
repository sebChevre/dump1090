package ch.sebooom.dump1090.service.impl;

import ch.sebooom.dump1090.repository.TCPStatsRepository;
import ch.sebooom.dump1090.service.TCPStatsService;
import ch.sebooom.dump1090.tcp.TCPStats;
import ch.sebooom.dump1090.tcp.TCPStatsAggregator;

import java.util.Map;

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

	@Override
	public TCPStatsAggregator findByPeriod(Long start, Long stop) {

		return TCPStatsAggregator.from(repository.findByStartAndStopDate(start, stop));
	}


}
