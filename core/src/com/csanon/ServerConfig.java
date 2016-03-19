package com.csanon;

public class ServerConfig {

	private String teamNum = "Team05";
	private String serverURL = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";

	public String getURL() {
		return serverURL;
	}

	public String getTeamNum() {
		return teamNum;
	}
}
