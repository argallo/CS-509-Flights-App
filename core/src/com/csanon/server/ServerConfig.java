package com.csanon.server;

/**
 * Class to hold the server configuration information
 */
public class ServerConfig {

	private static ServerConfig INSTANCE = new ServerConfig();

	private String teamNum = "Team05";
	private int lockTime = 120;
	private String serverURL = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
	private final String latLongURL = "http://api.timezonedb.com";// "https://timezonedb.p.mashape.com";
	private final String latLongKey = "NWZDDPVDUNKW";// "1Crny22Lvtmsh6CKUwUSsEwlrTyVp1O7iEHjsnqIr04LmPUP4l";

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

	public String getLatLongURL() {
		return latLongURL;
	}

	public int getLockTime() {
		return lockTime;
	}
}
