package com.assignment.flightinfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BusinessFlightDTO extends FlightInfoDTO{
	
	private String departure;
	
	private String arrival;
	
	private String uuid;
	
	private String flightRoute;

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFlightRoute() {
		return flightRoute;
	}

	
	@JsonProperty("flight")
	public void setFlightRoute(String flightRoute) {
		this.flightRoute = flightRoute;
	}

}
