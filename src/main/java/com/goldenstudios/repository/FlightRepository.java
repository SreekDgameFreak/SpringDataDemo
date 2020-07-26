package com.goldenstudios.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goldenstudios.entity.Flight;

public interface FlightRepository extends PagingAndSortingRepository<Flight, Long>{

	//Derived Queries
	List<Flight> findByOrigin(String origin);

	List<Flight> findByOriginAndDestination(String origin, String destination);

	List<Flight> findByOriginIn(String ... origins);

	List<Flight> findByOriginIgnoreCase(String origin);
	
	//Paging and Sorting
	Page<Flight> findByOrigin(String origin, Pageable pageRequest);
	
}
