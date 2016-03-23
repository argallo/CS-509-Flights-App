package com.csanon.server;

public class ServerFactory {

	private static FlightServer fightServer = new WPIFlightServer(ServerConfig.getConfig());
	
	public static FlightServer getServer(){
		return fightServer;
	}
}
