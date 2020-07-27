package com.goldenstudios.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.goldenstudios.entity.Flight;
import com.goldenstudios.repository.FlightRepository;

@Component
public class FlightService {
	
	private final FlightRepository flightRepository;
	
	public FlightService(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}
	
	public void saveFlight(Flight flight) {
		flightRepository.save(flight);
		throw new RuntimeException("I failed");
	}

	@Transactional
	public void saveFlightTransaction(Flight flight) {
		flightRepository.save(flight);
		// TODO Auto-generated method stub
		throw new RuntimeException("I failed");
	}

}
