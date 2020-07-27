package com.goldenstudios;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.goldenstudios.entity.Flight;
import com.goldenstudios.repository.FlightRepository;
import com.goldenstudios.service.FlightService;

@SpringBootTest
public class TransactionalTests {

	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private FlightService flightService;
	
	@BeforeEach
	public void setUp() {
		flightRepository.deleteAll();
	}
	
	@Test
	public void shouldNotRollBackWhenTheresNoTrasaction() {
		try {
			flightService.saveFlight(new Flight());
		} catch(Exception e) {
			
		}
		finally {
			assertThat(flightRepository.findAll())
				.isNotEmpty();
		}
	}
	
	@Test
	public void shouldRollBackWhenThereIsATransaction() {
		try {
			flightService.saveFlightTransaction(new Flight());
		} catch(Exception e) {
			//Do Nothing
		}
		finally {
			assertThat(flightRepository.findAll())
				.isEmpty();
		}
	}
	
}
