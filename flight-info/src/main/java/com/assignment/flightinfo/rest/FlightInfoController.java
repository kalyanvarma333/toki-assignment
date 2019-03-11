package com.assignment.flightinfo.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.flightinfo.dto.FlightInfoDTO;
import com.assignment.flightinfo.dto.FlightInfoResponse;
import com.assignment.flightinfo.service.FlightInfoService;

@RestController
public class FlightInfoController {

	@Autowired
	FlightInfoService flightService;

	private static final Logger log = LoggerFactory.getLogger(FlightInfoController.class);

	@GetMapping("/flights/info/test")
	public void test() {

		flightService.test();

	}

	@GetMapping("/flights/info/all")
	public ResponseEntity<FlightInfoResponse<List<FlightInfoDTO>>> getAllFlights() {
		try {
			List<FlightInfoDTO> flights = flightService.getAllFlights();
			return ResponseEntity.ok().body(new FlightInfoResponse<List<FlightInfoDTO>>("Success", "", flights));
		} catch (Exception e) {
           log.error("Error while getting all flights info {}",e.getMessage());
           return ResponseEntity.badRequest().body(new FlightInfoResponse<List<FlightInfoDTO>>("Error while getting flight info", e.getMessage(), null));
		}

	}
	
	@GetMapping("/flights/info")
	public ResponseEntity<FlightInfoResponse<List<FlightInfoDTO>>> getFlights(@RequestParam(value="departureCity",required=false) String departureCity,
			@RequestParam(value="arrivalCity",required=false) String arrivalCity,@RequestParam(value="sortBy",required=false) String sortBy) {
		try {
			log.info("Filter by Arrival City {} and Departure City {} ",arrivalCity,departureCity);
			List<FlightInfoDTO> flights = flightService.getFlightsWith(arrivalCity,departureCity,sortBy);
			return ResponseEntity.ok().body(new FlightInfoResponse<List<FlightInfoDTO>>("Success", "", flights));
		} catch (Exception e) {
           log.error("Error while getting all flights info {}",e.getMessage());
           return ResponseEntity.badRequest().body(new FlightInfoResponse<List<FlightInfoDTO>>("Error while getting flight info", e.getMessage(), null));
		}

	}

}
