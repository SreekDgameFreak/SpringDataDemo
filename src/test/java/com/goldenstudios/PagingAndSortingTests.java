package com.goldenstudios;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.goldenstudios.entity.Flight;
import com.goldenstudios.repository.FlightRepository;

@DataMongoTest
public class PagingAndSortingTests {

	@Autowired
	private FlightRepository flightRepository;

	@BeforeEach
	public void setUp() {
		flightRepository.deleteAll();
	}

	@Test
	public void shouldSortFlightsByDestination() {
		final Flight f1 = createFlight("London");
		final Flight f2 = createFlight("New Jersey");
		final Flight f3 = createFlight("Bangalore");
		
		flightRepository.save(f1);
		flightRepository.save(f2);
		flightRepository.save(f3);
		
		final Iterable<Flight> flights = flightRepository.findAll(Sort.by("destination"));
		
		assertThat(flights).hasSize(3);
		
		final Iterator<Flight> iterator = flights.iterator();
		
		assertThat(iterator.next().getDestination()).isEqualTo("Bangalore");
		assertThat(iterator.next().getDestination()).isEqualTo("London");
		assertThat(iterator.next().getDestination()).isEqualTo("New Jersey");
	
	}
	
	@Test
	public void shouldSortFlightsByDestinationAndShcheduledAt() {
		final LocalDateTime now = LocalDateTime.now();
		final Flight p1 = createFlight("Paris",now);
		final Flight p2 = createFlight("Paris",now.plusHours(2));
		final Flight p3 = createFlight("Paris",now.minusHours(1));
		final Flight l1 = createFlight("London",now.plusHours(1));
		final Flight l2 = createFlight("London",now);
		
		flightRepository.save(p1);
		flightRepository.save(p2);
		flightRepository.save(p3);
		flightRepository.save(l1);
		flightRepository.save(l2);
		
		final Iterable<Flight> flights = flightRepository.findAll(Sort.by("origin","scheduledAt"));
		
		assertThat(flights).hasSize(5);
		
		final Iterator<Flight> iterator = flights.iterator();
		
		assertThat(iterator.next()).isEqualToComparingFieldByField(l2);
		assertThat(iterator.next()).isEqualToComparingFieldByField(l1);
		assertThat(iterator.next()).isEqualToComparingFieldByField(p3);
		assertThat(iterator.next()).isEqualToComparingFieldByField(p1);
		assertThat(iterator.next()).isEqualToComparingFieldByField(p2);
	
	}
	
	@Test
	public void shouldPageResults() {
		for(int i=0;i<50;i++)
			flightRepository.save(createFlight(String.valueOf(i)));
		
		final Page<Flight> page = flightRepository.findAll(PageRequest.of(2, 5));
		
		assertThat(page.getTotalElements()).isEqualTo(50);
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getContent())
			.extracting(Flight::getDestination)
			.containsExactly("10","11","12","13","14");
		
	}
	
	@Test
	public void shouldPageAndSortResults() {
		for(int i=0;i<50;i++)
			flightRepository.save(createFlight(String.valueOf(i)));
		
		final Page<Flight> page = flightRepository.
				findAll(PageRequest.of(2, 5,Sort.by(Direction.DESC,"destination")));
		
		assertThat(page.getTotalElements()).isEqualTo(50);
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getContent())
			.extracting(Flight::getDestination)
			.containsExactly("44","43","42","41","40");
		
	}
	
	@Test
	public void shouldPageAndSortResultsADerivedQuery() {
		
		for(int i=0;i<10;i++) {
			final Flight flight = createFlight("Paris",String.valueOf(i));
			flightRepository.save(flight);
		}
		
		for(int i=0;i<10;i++) {
			final Flight flight = createFlight("London",String.valueOf(i));
			flightRepository.save(flight);
		}
		
		final Page<Flight> page = flightRepository.findByOrigin(
						"London",
						PageRequest.of(0, 5,Sort.by(Direction.DESC,"destination")));
		
		assertThat(page.getTotalElements()).isEqualTo(10);
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getContent())
			.extracting(Flight::getDestination)
			.containsExactly("9","8","7","6","5");
		
	}
	

	private Flight createFlight(String destination) {
		final Flight flight = new Flight();
		flight.setOrigin("Hyderabad");
		flight.setDestination(destination);
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
	
	private Flight createFlight(String origin, LocalDateTime scheduledAt) {
		final Flight flight = new Flight();
		flight.setOrigin(origin);
		flight.setDestination("Hyderabad");
		flight.setScheduledAt(scheduledAt);

		return flight;
	}

}
