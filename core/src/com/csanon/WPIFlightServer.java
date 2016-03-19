package com.csanon;

import com.mashape.unirest.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class WPIFlightServer implements FlightServer {
	private static ServerConfig config;

	public WPIFlightServer(ServerConfig config) {
		this.config = config;
	}

	@Override
	public String getAirports() {
		String result = null;
		try {
			HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum()).queryString("action", "list")
			.queryString("list_type", "airports");
			System.out.println(request.getUrl());
			HttpResponse<String> response = request.asString();
			result = response.getBody();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public String getFlights(String airportCode, String date) {
		// TODO Auto-generated method stub
		return null;
	}

}