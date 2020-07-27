package com.goldenstudios;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.goldenstudios.entity.Flight;
import com.goldenstudios.repository.FlightRepository;

@DataJpaTest
public class DerivedQueryTests {

	@Autowired
	private FlightRepository flightRepository;
	
	@BeforeEach
	public void setUp() {
		flightRepository.deleteAll();
	}
	
	@Test
	public void shouldFindFlightsFromLondon() {
		final Flight f1 = createFlight("London");
		final Flight f2 = createFlight("London");
		final Flight f3 = createFlight("Bangalore");
		
		flightRepository.save(f1);
		flightRepository.save(f2);
		flightRepository.save(f3);
		
		List<Flight> flightsToLondon = flightRepository.findByOrigin("London");
		assertThat(flightsToLondon).hasSize(2);
	}
	
	@Test
	public void shouldFindFlightsFromLondonToHyderabad() {
		final Flight f1 = createFlight("London","Hyderabad");
		final Flight f2 = createFlight("New Jersey","Hyderabad");
		final Flight f3 = createFlight("Bangalore","Hyderabad");
		
		flightRepository.save(f1);
		flightRepository.save(f2);
		flightRepository.save(f3);
		
		final List<Flight> londonToHyderabad = flightRepository
				.findByOriginAndDestination("London","Hyderabad");
		
		assertThat(londonToHyderabad)
			.hasSize(1)
			.first()
			.isEqualToComparingFieldByField(f1);
		
	}
	
	@Test
	public void shouldFindFlightsFromBangaloreOrLondon() {
		final Flight f1 = createFlight("London","Hyderabad");
		final Flight f2 = createFlight("New Jersey","Hyderabad");
		final Flight f3 = createFlight("Bangalore","Hyderabad");
		
		flightRepository.save(f1);
		flightRepository.save(f2);
		flightRepository.save(f3);
		
		final List<Flight> bangaloreOrLondon = flightRepository
				.findByOriginIn("London","Bangalore");
		
		assertThat(bangaloreOrLondon).hasSize(2);
		assertThat(bangaloreOrLondon.get(0)).isEqualToComparingFieldByField(f1);
		assertThat(bangaloreOrLondon.get(1)).isEqualToComparingFieldByField(f3);
		
	}
	
	@Test
	public void shouldFindFlightsFromLondonIgnoringCase() {
		final Flight f1 = createFlight("LONDON");
		
		flightRepository.save(f1);
		
		List<Flight> flightsToLondon = flightRepository.findByOriginIgnoreCase("London");
		
		assertThat(flightsToLondon)
			.hasSize(1)
			.first()
			.isEqualToComparingFieldByField(f1);
		
	}
	
	private Flight createFlight(String origin) {
		final Flight flight = new Flight();
		flight.setOrigin(origin);
		flight.setDestination("Hyderabad");
		flight.setScheduledAt(LocalDateTime.parse("2011-12-14T12:12:00"));
		
		return flight;
	}
	
	private Flight createFlight(String origin, String destination) {
		final Flight flight = new Flight();
		flight.setOrigin(origin);
		flight.setDestination(destination);
		flight.setScheduledAt(LocalDateTime.parse("2011-12-14T12:12:00"));
		
		return flight;
	}
}
