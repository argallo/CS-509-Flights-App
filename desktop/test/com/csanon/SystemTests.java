package com.csanon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.csanon.factrories.FlightFactory;
import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;
import com.csanon.time.DateTime;

public class SystemTests {

	private final static long TIMEOUT = 2 * 60 * 5;
	private final static List<Flight> flightsEWR2MDW_5_14_16 = new LinkedList<Flight>();
	private final static List<Flight> flightsMDW2EWR_5_20_16 = new LinkedList<Flight>();

	private final static List<ITrip> tripsEWR2MDW_5_14_16_All = new LinkedList<ITrip>();
	private final static List<ITrip> flightsMDW2EWR_5_20_16_All = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_FilterEcon = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_FilterFirst = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_SortPrice = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_SortPriceReverse = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_SortTime = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_SortTimeReverse = new LinkedList<ITrip>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + "/OffsetLatLong.pref"));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;

		Airports.initialize();
		Airplanes.initialize();

		byte[] encoded = Files.readAllBytes(Paths.get("FlightsEWRtoMDW.xml"));
		String xml = new String(encoded);
		FlightFactory flightFactory = FlightFactory.getInstance();
		List<Flight> EWR2MDW = flightFactory.parseFlightsFromXML(xml);
		flightsEWR2MDW_5_14_16.addAll(EWR2MDW);
		
		ITrip trip_E_0106_004028 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20083", "EWR"));
		ITrip trip_F_0106_007746 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20083", "EWR"));
		
		ITrip trip_E_0709_014836 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31551", "TPA"),
				getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA"));
		ITrip trip_F_0709_051338 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31551", "TPA"),
				getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA"));
		
		ITrip trip_E_1225_025228 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"),
				getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"));
		ITrip trip_F_1225_099729 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"),
				getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"));
		
		ITrip trip_E_0683_020388 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20108", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30936", "STL"),
				getFlight(flightsEWR2MDW_5_14_16, "22026", "MCO"));
		ITrip trip_F_0683_074662 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20108", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30936", "STL"),
				getFlight(flightsEWR2MDW_5_14_16, "22026", "MCO"));
		
		ITrip trip_E_0765_010478 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20109", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "0536", "ATL"),
				getFlight(flightsEWR2MDW_5_14_16, "16361", "MIA"));
		ITrip trip_F_0765_089160 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20109", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "0536", "ATL"),
				getFlight(flightsEWR2MDW_5_14_16, "16361", "MIA"));
		
		ITrip trip_E_1617_067419 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28398", "SFO"),
				getFlight(flightsEWR2MDW_5_14_16, "16388", "MIA"));
		ITrip trip_F_1617_434938 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28398", "SFO"),
				getFlight(flightsEWR2MDW_5_14_16, "16388", "MIA"));
		
		ITrip trip_E_1402_047817 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28395", "SFO"),
				getFlight(flightsEWR2MDW_5_14_16, "22053", "MCO"));
		ITrip trip_F_1402_198293 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28395", "SFO"),
				getFlight(flightsEWR2MDW_5_14_16, "22053", "MCO"));
		
		ITrip trip_E_0546_045196 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28396", "SFO"),
				getFlight(flightsEWR2MDW_5_14_16, "30963", "STL"));
		ITrip trip_F_0546_204823 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28396", "SFO"),
				getFlight(flightsEWR2MDW_5_14_16, "30963", "STL"));
		
		ITrip trip_E_0769_006980 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20077", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6161", "CLE"),
				getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"));
		ITrip trip_F_0769_028141 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20077", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6161", "CLE"),
				getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"));
		
		ITrip trip_E_0675_012197 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20079", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6161", "CLE"),
				getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"));
		ITrip trip_F_0675_043475 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20079", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6161", "CLE"),
				getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"));
		
		ITrip trip_E_0860_013415 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20079", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6164", "CLE"),
				getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"));
		ITrip trip_F_0860_071411 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20079", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6164", "CLE"),
				getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"));
		
		ITrip trip_E_1176_027767 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20080", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "3643", "CLT"),
				getFlight(flightsEWR2MDW_5_14_16, "28365", "SFO"));
		ITrip trip_F_1176_196684 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20080", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "3643", "CLT"),
				getFlight(flightsEWR2MDW_5_14_16, "28365", "SFO"));
		
		ITrip trip_E_0937_015027 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30909", "STL"),
				getFlight(flightsEWR2MDW_5_14_16, "9336", "FLL"));
		ITrip trip_F_0937_111179 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30909", "STL"),
				getFlight(flightsEWR2MDW_5_14_16, "9336", "FLL"));
		
		ITrip trip_E_0337_005378 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30901", "STL"),
				getFlight(flightsEWR2MDW_5_14_16, "13140", "IND"));
		ITrip trip_F_0337_040917 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30901", "STL"),
				getFlight(flightsEWR2MDW_5_14_16, "13140", "IND"));
		
		ITrip trip_E_0731_012324 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30905", "STL"),
				getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"));
		ITrip trip_F_0731_107175 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30905", "STL"),
				getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"));
		
		ITrip trip_E_1007_015031 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7432", "DFW"),
				getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"));
		ITrip trip_F_1007_091721 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7432", "DFW"),
				getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"));
		
		ITrip trip_E_0819_009806 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7420", "DFW"),
				getFlight(flightsEWR2MDW_5_14_16, "16954", "MSP"));
		ITrip trip_F_0819_037260 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7420", "DFW"),
				getFlight(flightsEWR2MDW_5_14_16, "16954", "MSP"));
		
		ITrip trip_E_0930_013222 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7422", "DFW"),
				getFlight(flightsEWR2MDW_5_14_16, "26470", "SLC"));
		ITrip trip_F_0930_060754 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7422", "DFW"),
				getFlight(flightsEWR2MDW_5_14_16, "26470", "SLC"));
		
		ITrip trip_E_0735_014165 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20086", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "16320", "MIA"),
				getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"));
		ITrip trip_F_0735_130566 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20086", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "16320", "MIA"),
				getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"));
		
		ITrip trip_E_0730_015919 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20088", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "13145", "IND"),
				getFlight(flightsEWR2MDW_5_14_16, "3027", "BOS"));
		ITrip trip_F_0730_123756 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20088", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "13145", "IND"),
				getFlight(flightsEWR2MDW_5_14_16, "3027", "BOS"));
		
		ITrip trip_E_0368_005531 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20088", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "13142", "IND"),
				getFlight(flightsEWR2MDW_5_14_16, "23929", "PIT"));
		ITrip trip_F_0368_050496 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20088", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "13142", "IND"),
				getFlight(flightsEWR2MDW_5_14_16, "23929", "PIT"));
		
		ITrip trip_E_0565_016100 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20089", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "27116", "SAT"));
		ITrip trip_F_0565_069131 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20089", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "27116", "SAT"));
		
		ITrip trip_E_0502_010880 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20090", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6793", "CMH"),
				getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"));		
		ITrip trip_F_0502_047245 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20090", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6793", "CMH"),
				getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"));
		
		ITrip trip_E_0708_014511 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20090", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6794", "CMH"),
				getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"));
		ITrip trip_F_0708_064598 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20090", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6794", "CMH"),
				getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"));
		
		ITrip trip_E_1505_042208 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20095", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "20740", "OAK"),
				getFlight(flightsEWR2MDW_5_14_16, "7452", "DFW"));
		ITrip trip_F_1505_253675 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20095", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "20740", "OAK"),
				getFlight(flightsEWR2MDW_5_14_16, "7452", "DFW"));
		
		ITrip trip_E_0697_013688 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20096", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "25216", "RDU"),
				getFlight(flightsEWR2MDW_5_14_16, "12523", "HOU"));		
		ITrip trip_F_0697_120046 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20096", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "25216", "RDU"),
				getFlight(flightsEWR2MDW_5_14_16, "12523", "HOU"));
		
		ITrip trip_E_0729_010784 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19481", "LGA"),
				getFlight(flightsEWR2MDW_5_14_16, "12529", "HOU"));	
		ITrip trip_F_0729_106202 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19481", "LGA"),
				getFlight(flightsEWR2MDW_5_14_16, "12529", "HOU"));	
		
		ITrip trip_E_1000_039678 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19480", "LGA"),
				getFlight(flightsEWR2MDW_5_14_16, "28389", "SFO"));
		
		ITrip trip_E_0855_026495 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19483", "LGA"),
				getFlight(flightsEWR2MDW_5_14_16, "23325", "PHX"));
		ITrip trip_F_0855_120753 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19483", "LGA"),
				getFlight(flightsEWR2MDW_5_14_16, "23325", "PHX"));

		ITrip trip_E_1000_022575 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20101", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "16961", "MSP"),
				getFlight(flightsEWR2MDW_5_14_16, "28389", "SFO"));
		
		
		
		tripsEWR2MDW_5_14_16_All.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0106_007746);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0709_014836);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0709_051338);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_All.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0683_074662);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0765_010478);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0765_089160);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_All.add(trip_F_1617_434938);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_All.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0546_045196);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0546_204823);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0769_006980);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0675_012197);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0860_013415);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0860_071411);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_All.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0337_005378);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0337_040917);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0731_012324);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0731_107175);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1007_015031);
		tripsEWR2MDW_5_14_16_All.add(trip_F_1007_091721);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0819_009806);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0819_037260);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0930_060754);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0735_014165);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0735_130566);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0730_015919);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0730_123756);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0368_050496);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0565_069131);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0502_047245);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0708_014511);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0708_064598);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1505_042208);
		tripsEWR2MDW_5_14_16_All.add(trip_F_1505_253675);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0697_013688);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0697_120046);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0729_010784);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0729_106202);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0855_120753);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1000_022575);
		
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0709_014836);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0765_010478);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0546_045196);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0769_006980);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0675_012197);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0860_013415);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0337_005378 );
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0731_012324);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1007_015031);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0819_009806);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0735_014165);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0730_015919);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0708_014511);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1505_042208);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0697_013688);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0729_010784);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1000_022575);
		
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0106_007746);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0709_051338);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0683_074662);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0765_089160);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1617_434938);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0546_204823);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0860_071411);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0337_040917 );
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0731_107175);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1007_091721);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0819_037260);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0930_060754);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0735_130566);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0730_123756);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0368_050496);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0565_069131);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0502_047245);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0708_064598);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1505_253675);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0697_120046);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0729_106202);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0855_120753);
		
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0337_005378 );
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0769_006980);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0106_007746);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0819_009806);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0765_010478);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0729_010784);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0675_012197);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0731_012324);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0860_013415);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0697_013688);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0735_014165);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0708_014511);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0709_014836);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1007_015031);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0730_015919);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1000_022575);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0819_037260);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0337_040917 );
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1505_042208);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0546_045196);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0502_047245);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0368_050496);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0709_051338);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0930_060754);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0708_064598);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0565_069131);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0860_071411);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0683_074662);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0765_089160);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_1007_091721);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0729_106202);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0731_107175);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0697_120046);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0855_120753);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0730_123756);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0735_130566);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0546_204823);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_1505_253675);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_1617_434938);
		
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_1617_434938);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_1505_253675);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0546_204823);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0735_130566);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0730_123756);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0855_120753);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0697_120046);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0731_107175);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0729_106202);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_1007_091721);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0765_089160);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0683_074662);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0860_071411);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0565_069131);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0708_064598);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0930_060754);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0709_051338);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0368_050496);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0502_047245);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0546_045196);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1505_042208);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0337_040917 );
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0819_037260);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1000_022575);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0730_015919);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1007_015031);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0709_014836);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0708_014511);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0735_014165);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0697_013688);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0860_013415);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0731_012324);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0675_012197);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0729_010784);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0765_010478);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0819_009806);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0106_007746);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0769_006980);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0337_005378 );
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0106_004028);

		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0106_007746);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0337_005378 );
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0337_040917 );
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0368_050496);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0502_047245);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0546_045196);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0546_204823);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0565_069131);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0675_012197);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0683_074662);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0697_013688);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0697_120046);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0708_014511);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0708_064598);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0709_014836);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0709_051338);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0729_010784);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0729_106202);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0730_015919);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0730_123756);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0731_012324);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0731_107175);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0735_014165);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0735_130566);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0765_010478);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0765_089160);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0769_006980);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0819_009806);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0819_037260);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0855_120753);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0860_013415);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0860_071411);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0930_060754);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1000_022575);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1007_015031);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1007_091721);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1505_042208);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1505_253675);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1617_434938);
		
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1617_434938);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1505_042208);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1505_253675);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1007_015031);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1007_091721);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1000_022575);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0930_060754);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0860_013415);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0860_071411);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0855_120753);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0819_009806);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0819_037260);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0769_006980);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0765_010478);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0765_089160);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0735_014165);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0735_130566);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0731_012324);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0731_107175);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0730_015919);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0730_123756);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0729_010784);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0729_106202);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0709_014836);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0709_051338);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0708_014511);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0708_064598);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0697_013688);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0697_120046);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0683_074662);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0675_012197);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0565_069131);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0546_045196);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0546_204823);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0502_047245);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0368_050496);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0337_005378 );
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0337_040917 );
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0106_007746);
		
		
	}

	private static Flight getFlight(List<Flight> flights, String flightNum, String departureCode) {
		List<Flight> reducedFlights = flights.stream().filter(flight -> flight.getFlightNum().equals(flightNum)
				&& flight.getDepartureAirport().getCode().equals(departureCode)).collect(Collectors.toList());
		assertEquals(1, reducedFlights.size());
		return reducedFlights.get(0);
	}

	@Before
	public void setUp() throws Exception {
		// TODO ServerFactory.getServer().resetServer()
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX Select the cheapest option Lock the server Book the
	 * trip unlock the server Search trips again confirm that the components
	 * flights have changed
	 * 
	 * @throws Exception
	 */
	@Test
	public void RegularOneWay() throws Exception {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("BOS");
		Airport arrivalAirport = Airports.getAirport("EWR");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		ITrip bookedTrip = null;

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(actualTrips));

		Consumer<String> callback = message -> {
			fail("Server Timed out when it shouldn't have");
		};

		boolean booked = flightServer.lockServer(callback);
		assertEquals(true, booked);
		flightServer.bookTrip(bookedTrip);

		flightServer.unlockServer();

		ITrip newBookedTrip = null;

		List<ITrip> changedTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		assertEquals(true, changedTrips.contains(newBookedTrip));
	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from BOS to LAX Select the most expensive option on the way
	 * Select the cheapest option on the way back Lock the server Book the two
	 * trips unlock the server Search trips again confirm that the components
	 * flights have changed
	 * 
	 * @throws Exception
	 */
	@Test
	public void RegularRoundTrip() throws Exception {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("PHX");
		Airport arrivalAirport = Airports.getAirport("EWR");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		DateTime returnTime = DateTime.of(2016, 5, 20, 0);
		List<ITrip> actualDepartureTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> actualreturnTrips = tripBuilder.getTrips(arrivalAirport, departureAirport, departureTime);
		List<ITrip> expectedDepartureTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);
		List<ITrip> expectedReturnTrips = new LinkedList<ITrip>(flightsMDW2EWR_5_20_16_All);

		ITrip bookedDepartureTrip = null;
		ITrip bookedreturnTrip = null;

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedDepartureTrips), new HashSet<ITrip>(actualDepartureTrips));
		assertEquals(new HashSet<ITrip>(expectedReturnTrips), new HashSet<ITrip>(actualreturnTrips));

		Consumer<String> callback = message -> {
			fail("Server Timed out when it shouldn't have");
		};

		boolean booked = flightServer.lockServer(callback);
		assertEquals(true, booked);

		flightServer.bookTrip(bookedDepartureTrip);
		flightServer.bookTrip(bookedreturnTrip);

		flightServer.unlockServer();

		ITrip changedDepartureBookTrip = null;
		ITrip changedReturnBookTrip = null;

		List<ITrip> changedDepartureTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> changedReturnTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		assertEquals(true, changedDepartureTrips.contains(changedDepartureBookTrip));
		assertEquals(true, changedReturnTrips.contains(changedReturnBookTrip));
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX Select the cheapest option Lock the server Wait for
	 * 2.5 minutes attempt to book the trip
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void ServerTimeout() throws InterruptedException {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("PHX");
		Airport arrivalAirport = Airports.getAirport("EWR");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		ITrip bookedTrip = null;

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(actualTrips));

		Consumer<String> callback = message -> {
		};

		boolean booked = flightServer.lockServer(callback);
		assertEquals(true, booked);

		flightServer.wait(TIMEOUT + 1);

		try {
			flightServer.bookTrip(bookedTrip);
			fail("Able to book a trip which should not happen");
		} catch (Exception e) {

		}
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX Sort trips by the price ascending Sort trips by the
	 * price descending
	 */
	@Test
	public void SortbyPrice() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("PHX");
		Airport arrivalAirport = Airports.getAirport("EWR");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(actualTrips));

		List<ITrip> expectedSortedASCTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_SortPrice);
		List<ITrip> expectedSortedDESCTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_SortPriceReverse);
		List<ITrip> actualASCTrips = null; // sort(actualTrips, False)
		List<ITrip> actualDESCTrips = null; // sort(actualTrips, TRUE)

		// verify that we get the expected trips
		assertEquals(expectedSortedASCTrips, actualASCTrips);
		assertEquals(expectedSortedDESCTrips, actualDESCTrips);
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX Sort trips by the travel time descending Sort trips
	 * by the travel time ascending
	 */
	@Test
	public void SortbyTravelTime() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("PHX");
		Airport arrivalAirport = Airports.getAirport("EWR");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(actualTrips));

		List<ITrip> expectedSortedASCTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_SortTime);
		List<ITrip> expectedSortedDESCTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_SortTimeReverse);
		List<ITrip> actualASCTrips = null; // sort(actualTrips, False)
		List<ITrip> actualDESCTrips = null; // sort(actualTrips, TRUE)

		// verify that we get the expected trips
		assertEquals(expectedSortedASCTrips, actualASCTrips);
		assertEquals(expectedSortedDESCTrips, actualDESCTrips);
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX For each component flight confirm the duration
	 * matches the actual time difference, Confirm the timezone of each airport
	 * as well
	 */
	@Test
	public void LocalTime() {
		// TODO
	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from BOS to LAX Filter and confirm trips for only economy
	 * seats Filter and confirm trips for only first class seats Confirm that
	 * those two filters make up the entire list of trips
	 */
	@Test
	public void FilterBySeat() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("PHX");
		Airport arrivalAirport = Airports.getAirport("EWR");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(actualTrips));

		List<ITrip> expectedFilteredECONTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_FilterEcon);
		List<ITrip> expectedFilteredFIRSTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_FilterFirst);
		List<ITrip> actualFilteredECONTrips = null; // filter(actualTrips,
													// SeatClass.ECONOMY)
		List<ITrip> actualFilteredFIRSTrips = null; // filter(actualTrips,
													// SeatClass.FIRST)

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedFilteredECONTrips), new HashSet<ITrip>(actualFilteredECONTrips));
		assertEquals(new HashSet<ITrip>(expectedFilteredFIRSTrips), new HashSet<ITrip>(actualFilteredFIRSTrips));

		List<ITrip> combined = new LinkedList<ITrip>(actualFilteredECONTrips);
		combined.addAll(actualFilteredFIRSTrips);
		assertEquals(new HashSet<ITrip>(combined), new HashSet<ITrip>(actualFilteredFIRSTrips));

	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from BOS to LAX For each trip book the last leg of the trip
	 * Research Confirming that the specific trip both seat options no longer
	 * contains that trip Confirm after all possible trips there are no trips
	 * left
	 */
	@Test
	public void NoFlightsIfAllBooked() {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("PHX");
		Airport arrivalAirport = Airports.getAirport("EWR");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> fullActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(fullActualTrips));

		fullActualTrips.forEach(aTrip -> {
			Consumer<String> callback = message -> {
				fail("Server Timed out when it shouldn't have");
			};
			Flight lastFlight = aTrip.getLegs().get(aTrip.getLegs().size() - 1);

			flightServer.lockServer(callback);
			int i = 1;
			while (lastFlight.checkEconomyAvailable(i)) {
				i++;
				try {
					flightServer.bookTrip(new EconomyTrip(lastFlight));
				} catch (Exception e) {
					fail("Unable to book a trip which should not happen");
				}
			}
			flightServer.unlockServer();

			flightServer.lockServer(callback);
			i = 1;
			while (lastFlight.checkFirstClassAvailable(i)) {
				i++;
				try {
					flightServer.bookTrip(new FirstClassTrip(lastFlight));
				} catch (Exception e) {
					fail("Unable to book a trip which should not happen");
				}
			}
			flightServer.unlockServer();

			List<ITrip> toRemove = new LinkedList<ITrip>();
			expectedTrips.forEach(eTrip -> {
				if (eTrip.getLegs().contains(lastFlight)) {
					toRemove.add(eTrip);
				}
			});
			expectedTrips.removeAll(toRemove);

			List<ITrip> partialActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
			assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(partialActualTrips));
		});

		List<ITrip> emptyActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> emptyExpectedTrips = new LinkedList<ITrip>();

		assertEquals(emptyExpectedTrips, emptyActualTrips);
	}

	/**
	 * Attempt to search for trips going from BOS to BOS Confirm that there are
	 * no trips available
	 */
	@Test
	public void SameAirport() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("BOS");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, departureAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>();

		// verify that we get the expected trips
		assertEquals(expectedTrips, actualTrips);
	}

}
