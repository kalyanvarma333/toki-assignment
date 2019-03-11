package com.assignment.flightinfo.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assignment.flightinfo.dto.BusinessFlightDTO;
import com.assignment.flightinfo.dto.CheapFlightDTO;
import com.assignment.flightinfo.dto.FlightInfoDTO;
import com.assignment.flightinfo.service.FlightInfoService;

@Service
public class FlightInfoServiceImpl implements FlightInfoService {

	@Value("${cheap.flights.url}")
	private String cheapFlightsUrl;

	@Value("${business.flights.url}")
	private String businessFlightsUrl;

	@Autowired
	private RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(FlightInfoServiceImpl.class);

	@Override
	public List<FlightInfoDTO> getAllFlights() throws Exception {
		try {
			ExecutorService executor = Executors.newWorkStealingPool();

			List<Callable<Object>> callables = Arrays.asList(() -> {

				return restTemplate.exchange(cheapFlightsUrl, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<CheapFlightDTO>>() {
						}).getBody();

			}, () -> {

				return restTemplate.exchange(businessFlightsUrl, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<BusinessFlightDTO>>() {
						}).getBody();

			});

			List<FlightInfoDTO> flightsInfo = executor.invokeAll(callables).stream().map(future -> {
				try {
					return future.get();
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}).map(data -> {

				List<FlightInfoDTO> listOfFlights = (List<FlightInfoDTO>) data ;
				List<FlightInfoDTO> flightList = new ArrayList<>();
				if (Objects.nonNull(listOfFlights) && ((List<FlightInfoDTO>) listOfFlights).size() > 0) {

					if (listOfFlights.get(0) instanceof BusinessFlightDTO) {
						listOfFlights.stream().forEach(obj -> {
							BusinessFlightDTO businessFlight = (BusinessFlightDTO) obj;
							String arrivalTime = businessFlight.getArrival();
							String depTime = businessFlight.getDeparture();
							String id = businessFlight.getUuid();
							String[] flightRouterArr = businessFlight.getFlightRoute().split("->");

							FlightInfoDTO flightDTO = new FlightInfoDTO(flightRouterArr[0], flightRouterArr[1],
									arrivalTime, depTime, id);
							flightList.add(flightDTO);
						});

					} else if (listOfFlights.get(0) instanceof CheapFlightDTO) {
						listOfFlights.stream().forEach(obj -> {
							CheapFlightDTO cheapFlight = (CheapFlightDTO) obj;
							String departure = cheapFlight.getDeparture();
							String arrival = cheapFlight.getArrival();
							String arrivalTime = cheapFlight.getArrival();
							String depTime = cheapFlight.getDeparture();
							String id = cheapFlight.getId() + "";
							FlightInfoDTO flightDTO = new FlightInfoDTO(departure, arrival, arrivalTime, depTime, id);
							flightList.add(flightDTO);
						});

					}

				}

				return flightList;

			}).flatMap(a -> a.stream()).collect(Collectors.toList());

			return flightsInfo;

		} catch (Exception e) {
			log.error("Error while retrieving flight info {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public void test() {
		ResponseEntity<List<BusinessFlightDTO>> businessFlightDTOList = restTemplate.exchange(businessFlightsUrl,
				HttpMethod.GET, null, new ParameterizedTypeReference<List<BusinessFlightDTO>>() {
				});

		ResponseEntity<List<CheapFlightDTO>> cheapFlightDTOList = restTemplate.exchange(cheapFlightsUrl, HttpMethod.GET,
				null, new ParameterizedTypeReference<List<CheapFlightDTO>>() {
				});

		log.info("cheap flights list size {}", cheapFlightDTOList == null ? -1 : cheapFlightDTOList.getBody().size());
		log.info("business flights list size {}",
				businessFlightDTOList == null ? -1 : businessFlightDTOList.getBody().size());

	}

	@Override
	public List<FlightInfoDTO> getFlightsWith(String arrivalCity, String departureCity, String sortBy)
			throws Exception {
		try {
			List<FlightInfoDTO> listOfFlights = getAllFlights();
			if(Objects.nonNull(arrivalCity) && Objects.nonNull(departureCity)) {
			   return listOfFlights.stream().filter(a -> a.getArrivalCity().trim().equals(arrivalCity.trim()) && a.getDepartureCity().trim().equals(departureCity.trim())).collect(Collectors.toList());
			}
		} catch(Exception e) {
			log.error("Error while getting flights with arrivalCity {} departure city {} ",arrivalCity,departureCity);
			throw new Exception("Error while retrieving flight details"+e.getMessage());
		}
		return Collections.emptyList();
	}

}
