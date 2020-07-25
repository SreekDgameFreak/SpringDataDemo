package com.goldenstudios;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;

import com.goldenstudios.entity.Flight;

@DataJpaTest
class MyspringdatademoApplicationTests {

	@Autowired
	private EntityManager entityManager;
	
	@Test
	void verifyFlightCanBeSaved() {
		
		final Flight flight = new Flight();
		
		flight.setOrigin("Tirupathi");
		flight.setDestination("Hyderabad");
		flight.setScheduledAt(LocalDateTime.parse("2020-11-21T12:12:12"));
		
		entityManager.persist(flight);
		
		final TypedQuery<Flight> results = entityManager.
				createQuery("SELECT f FROM Flight f", Flight.class);
		
		final List<Flight> resultList = results.getResultList();
		
		assertThat(resultList)
			.hasSize(1)
			.first()
			.isEqualTo(flight);
		
	}

}
