package com.csanon.server;

public class ServerConfig {

	private static ServerConfig INSTANCE = new ServerConfig();

	private String teamNum = "Team05";
	private String serverURL = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
	private final String latLongKey = "NWZDDPVDUNKW";

	private ServerConfig() {

	}

	public String getURL() {
		return serverURL;
	}

	public String getTeamNum() {
		return teamNum;
	}

	public static ServerConfig getConfig() {
		return INSTANCE;
	}

	public String getLatLongKey() {
		return latLongKey;
	}
}
