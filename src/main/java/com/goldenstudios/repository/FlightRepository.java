package com.goldenstudios.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.goldenstudios.entity.Flight;

public interface FlightRepository extends CrudRepository<Flight, Long>{

	//Derived Queries
	List<Flight> findByOrigin(String origin);

	List<Flight> findByOriginAndDestination(String origin, String destination);

	List<Flight> findByOriginIn(String ... origins);

	List<Flight> findByOriginIgnoreCase(String origin);
	
}
