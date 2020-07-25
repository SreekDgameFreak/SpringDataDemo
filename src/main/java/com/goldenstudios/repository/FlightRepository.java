package com.goldenstudios.repository;

import org.springframework.data.repository.CrudRepository;

import com.goldenstudios.entity.Flight;

public interface FlightRepository extends CrudRepository<Flight, Long>{

}
