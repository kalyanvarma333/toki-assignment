package com.assignment.flightinfo.service;

import java.util.List;

import com.assignment.flightinfo.dto.FlightInfoDTO;

public interface FlightInfoService {
	
	List<FlightInfoDTO> getAllFlights() throws Exception;

	void test();

	List<FlightInfoDTO> getFlightsWith(String arrivalCity, String departureCity, String sortBy)throws Exception;

}
