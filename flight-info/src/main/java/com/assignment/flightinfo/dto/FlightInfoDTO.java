package com.assignment.flightinfo.dto;

public class FlightInfoDTO {

	private String departureCity;

	private String arrivalCity;

	private String arrivalDate;

	private String departureDate;

	private String flightId;

	public String getDepartureCity() {
		return departureCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public String getFlightId() {
		return flightId;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public String getDepartureDate() {
		return departureDate;
	}
	
	public FlightInfoDTO() {
		
	}

	public FlightInfoDTO(String departureCity, String arrivalCity, String arrivalDate, String departureDate,
			String flightId) {
		super();
		this.departureCity = departureCity;
		this.arrivalCity = arrivalCity;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
		this.flightId = flightId;
	}

}
