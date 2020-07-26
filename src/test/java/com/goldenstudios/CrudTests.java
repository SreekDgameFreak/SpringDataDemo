package com.goldenstudios;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.goldenstudios.entity.Flight;
import com.goldenstudios.repository.FlightRepository;

@DataMongoTest
class CrudTests {

	@Autowired
	private FlightRepository flightRepository;
	
	@BeforeEach
	public void setUp() {
		flightRepository.deleteAll();
	}
	
	@Test
	public void shouldPerformCRUDOperations() {

		final Flight flight = new Flight();
		
		flight.setOrigin("Tirupathi");
		flight.setDestination("Hyderabad");
		flight.setScheduledAt(LocalDateTime.parse("2020-11-21T12:12:12"));
		
		flightRepository.save(flight);
		
		assertThat(flightRepository.findAll())
			.hasSize(1)
			.first()
			.isEqualToComparingFieldByField(flight);
		
		flightRepository.deleteById(flight.getId());
		
		assertThat(flightRepository.count())
			.isZero();
		
	}
	

}
