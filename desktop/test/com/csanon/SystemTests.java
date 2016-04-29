package com.csanon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.After;
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

	private final static long TIMEOUT = 2 * 60;
	private final static List<Flight> flightsEWR2MDW_5_14_16 = new LinkedList<Flight>();
	private final static List<Flight> flightsMDW2EWR_5_15_16 = new LinkedList<Flight>();

	private final static List<ITrip> tripsEWR2MDW_5_14_16_All = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_FilterEcon = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_FilterFirst = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_SortPrice = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_SortPriceReverse = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_SortTime = new LinkedList<ITrip>();
	private final static List<ITrip> tripsEWR2MDW_5_14_16_SortTimeReverse = new LinkedList<ITrip>();

	private final static List<ITrip> tripsMDW2EWR_5_15_16_All = new LinkedList<ITrip>();
	private final static List<ITrip> tripsMDW2EWR_5_15_16_filtered = new LinkedList<ITrip>();

	private final static String newTripEWR2MDWXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Flights>"
			+ "<Flight Airplane=\"717\" FlightTime=\"120\" Number=\"20107\">" + "      <Departure>"
			+ "         <Code>EWR</Code>" + "         <Time>2016 May 15 00:54 GMT</Time>" + "      </Departure>"
			+ "      <Arrival>" + "         <Code>TPA</Code>" + "         <Time>2016 May 15 02:54 GMT</Time>"
			+ "      </Arrival>" + "      <Seating>" + "         <FirstClass Price=\"$380.26\">6</FirstClass>"
			+ "         <Coach Price=\"$111.29\">58</Coach>" + "      </Seating>" + "   </Flight>"
			+ "   <Flight Airplane=\"777\" FlightTime=\"448\" Number=\"31550\">" + "      <Departure>"
			+ "         <Code>TPA</Code>" + "         <Time>2016 May 15 05:21 GMT</Time>" + "      </Departure>"
			+ "      <Arrival>" + "         <Code>SJC</Code>" + "         <Time>2016 May 15 12:49 GMT</Time>"
			+ "      </Arrival>" + "      <Seating>" + "         <FirstClass Price=\"$430.01\">6</FirstClass>"
			+ "         <Coach Price=\"$84.93\">319</Coach>" + "      </Seating>" + "   </Flight>"
			+ "      <Flight Airplane=\"A380\" FlightTime=\"316\" Number=\"29044\">" + "      <Departure>"
			+ "         <Code>SJC</Code>" + "         <Time>2016 May 15 16:03 GMT</Time>" + "      </Departure>"
			+ "      <Arrival>" + "         <Code>MDW</Code>" + "         <Time>2016 May 15 21:19 GMT</Time>"
			+ "      </Arrival>" + "      <Seating>" + "         <FirstClass Price=\"$187.02\">94</FirstClass>"
			+ "         <Coach Price=\"$56.06\">373</Coach>" + "      </Seating>" + "   </Flight>"
			+ "   <Flight Airplane=\"777\" FlightTime=\"39\" Number=\"31551\">" + "      <Departure>"
			+ "         <Code>TPA</Code>" + "         <Time>2016 May 15 05:55 GMT</Time>" + "      </Departure>"
			+ "      <Arrival>" + "         <Code>MIA</Code>" + "         <Time>2016 May 15 06:34 GMT</Time>"
			+ "      </Arrival>" + "      <Seating>" + "         <FirstClass Price=\"$37.32\">29</FirstClass>"
			+ "         <Coach Price=\"$7.37\">31</Coach>" + "      </Seating>" + "   </Flight>"
			+ "   <Flight Airplane=\"747\" FlightTime=\"157\" Number=\"16358\">" + "      <Departure>"
			+ "         <Code>MIA</Code>" + "         <Time>2016 May 15 10:06 GMT</Time>" + "      </Departure>"
			+ "      <Arrival>" + "         <Code>MDW</Code>" + "         <Time>2016 May 15 12:43 GMT</Time>"
			+ "      </Arrival>" + "      <Seating>" + "         <FirstClass Price=\"$95.80\">54</FirstClass>"
			+ "         <Coach Price=\"$29.70\">302</Coach>" + "      </Seating>" + "   </Flight>" + "</Flights>";

	private final static String newTripMDW2EWRXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Flights>"
			+ "<Flight Airplane=\"777\" FlightTime=\"251\" Number=\"4349\">" + "      <Departure>"
			+ "         <Code>MDW</Code>" + "         <Time>2016 May 16 01:42 GMT</Time>" + "      </Departure>"
			+ "      <Arrival>" + "         <Code>LAX</Code>" + "         <Time>2016 May 16 05:53 GMT</Time>"
			+ "      </Arrival>" + "      <Seating>" + "         <FirstClass Price=\"$240.44\">20</FirstClass>"
			+ "         <Coach Price=\"$47.49\">103</Coach>" + "      </Seating>" + "   </Flight>"
			+ "    <Flight Airplane=\"767\" FlightTime=\"336\" Number=\"15132\">" + "      <Departure>"
			+ "         <Code>LAX</Code>" + "         <Time>2016 May 16 09:22 GMT</Time>" + "      </Departure>"
			+ "      <Arrival>" + "         <Code>BOS</Code>" + "         <Time>2016 May 16 14:58 GMT</Time>"
			+ "      </Arrival>" + "      <Seating>" + "         <FirstClass Price=\"$244.81\">22</FirstClass>"
			+ "         <Coach Price=\"$127.30\">48</Coach>" + "      </Seating>" + "   </Flight>"
			+ "   <Flight Airplane=\"747\" FlightTime=\"28\" Number=\"3113\">" + "      <Departure>"
			+ "         <Code>BOS</Code>" + "         <Time>2016 May 16 18:29 GMT</Time>" + "      </Departure>"
			+ "      <Arrival>" + "         <Code>EWR</Code>" + "         <Time>2016 May 16 18:57 GMT</Time>"
			+ "      </Arrival>" + "      <Seating>" + "         <FirstClass Price=\"$17.09\">121</FirstClass>"
			+ "         <Coach Price=\"$5.30\">309</Coach>" + "      </Seating>" + "   </Flight>" + "</Flights>";

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

		encoded = Files.readAllBytes(Paths.get("FlightsMDWtoEWR.xml"));
		xml = new String(encoded);
		List<Flight> MDW2EWR = flightFactory.parseFlightsFromXML(xml);
		flightsMDW2EWR_5_15_16.addAll(MDW2EWR);

		ITrip trip_E_0106_004028 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20083", "EWR"));
		ITrip trip_F_0106_007746 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20083", "EWR"));

		ITrip trip_E_0709_014836 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31551", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA"));
		ITrip trip_F_0709_051338 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31551", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA"));

		ITrip trip_E_1225_025228 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"));
		ITrip trip_F_1225_099729 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"));

		ITrip trip_E_0683_020388 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20108", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30936", "STL"), getFlight(flightsEWR2MDW_5_14_16, "22026", "MCO"));
		ITrip trip_F_0683_074662 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20108", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30936", "STL"), getFlight(flightsEWR2MDW_5_14_16, "22026", "MCO"));

		ITrip trip_E_0765_010478 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20109", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "0536", "ATL"), getFlight(flightsEWR2MDW_5_14_16, "16361", "MIA"));
		ITrip trip_F_0765_089160 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20109", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "0536", "ATL"), getFlight(flightsEWR2MDW_5_14_16, "16361", "MIA"));

		ITrip trip_E_1617_067419 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28398", "SFO"), getFlight(flightsEWR2MDW_5_14_16, "16388", "MIA"));
		ITrip trip_F_1617_434938 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28398", "SFO"), getFlight(flightsEWR2MDW_5_14_16, "16388", "MIA"));

		ITrip trip_E_1402_047817 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28395", "SFO"), getFlight(flightsEWR2MDW_5_14_16, "22053", "MCO"));
		ITrip trip_F_1402_198293 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28395", "SFO"), getFlight(flightsEWR2MDW_5_14_16, "22053", "MCO"));

		ITrip trip_E_1086_045196 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28396", "SFO"), getFlight(flightsEWR2MDW_5_14_16, "30963", "STL"));
		ITrip trip_F_1086_204823 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20111", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "28396", "SFO"), getFlight(flightsEWR2MDW_5_14_16, "30963", "STL"));

		ITrip trip_E_0769_006980 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20077", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6161", "CLE"), getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"));
		ITrip trip_F_0769_028141 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20077", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6161", "CLE"), getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"));

		ITrip trip_E_0675_012197 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20079", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6161", "CLE"), getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"));
		ITrip trip_F_0675_043475 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20079", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6161", "CLE"), getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"));

		ITrip trip_E_0860_013415 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20079", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6164", "CLE"), getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"));
		ITrip trip_F_0860_071411 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20079", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6164", "CLE"), getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"));

		ITrip trip_E_1176_027767 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20080", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "3643", "CLT"), getFlight(flightsEWR2MDW_5_14_16, "28365", "SFO"));
		ITrip trip_F_1176_196684 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20080", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "3643", "CLT"), getFlight(flightsEWR2MDW_5_14_16, "28365", "SFO"));

		ITrip trip_E_0937_015027 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30909", "STL"), getFlight(flightsEWR2MDW_5_14_16, "9336", "FLL"));
		ITrip trip_F_0937_111179 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30909", "STL"), getFlight(flightsEWR2MDW_5_14_16, "9336", "FLL"));

		ITrip trip_E_0337_005378 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30901", "STL"), getFlight(flightsEWR2MDW_5_14_16, "13140", "IND"));
		ITrip trip_F_0337_040917 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30901", "STL"), getFlight(flightsEWR2MDW_5_14_16, "13140", "IND"));

		ITrip trip_E_0731_012324 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30905", "STL"), getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"));
		ITrip trip_F_0731_107175 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20084", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "30905", "STL"), getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"));

		// Only one
		ITrip trip_E_1007_024931 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7423", "DFW"), getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"));
		ITrip trip_F_1007_091721 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7423", "DFW"), getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"));

		// Only one
		ITrip trip_E_0819_009806 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7420", "DFW"), getFlight(flightsEWR2MDW_5_14_16, "16954", "MSP"));
		ITrip trip_F_0819_037260 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7420", "DFW"), getFlight(flightsEWR2MDW_5_14_16, "16954", "MSP"));

		ITrip trip_E_0930_013222 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7422", "DFW"), getFlight(flightsEWR2MDW_5_14_16, "26470", "SLC"));
		ITrip trip_F_0930_060754 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20085", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "7422", "DFW"), getFlight(flightsEWR2MDW_5_14_16, "26470", "SLC"));

		ITrip trip_E_0735_014165 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20086", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "16320", "MIA"), getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"));
		ITrip trip_F_0735_130566 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20086", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "16320", "MIA"), getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"));

		ITrip trip_E_0730_015919 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20088", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "13145", "IND"), getFlight(flightsEWR2MDW_5_14_16, "3027", "BOS"));
		ITrip trip_F_0730_123756 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20088", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "13145", "IND"), getFlight(flightsEWR2MDW_5_14_16, "3027", "BOS"));

		ITrip trip_E_0368_005531 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20088", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "13142", "IND"), getFlight(flightsEWR2MDW_5_14_16, "23929", "PIT"));
		ITrip trip_F_0368_050496 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20088", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "13142", "IND"), getFlight(flightsEWR2MDW_5_14_16, "23929", "PIT"));

		ITrip trip_E_0565_016100 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20089", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "27116", "SAT"));
		ITrip trip_F_0565_069131 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20089", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "27116", "SAT"));

		ITrip trip_E_0502_010880 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20090", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6793", "CMH"), getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"));
		ITrip trip_F_0502_047245 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20090", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6793", "CMH"), getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"));

		ITrip trip_E_0708_014511 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20090", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6794", "CMH"), getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"));
		ITrip trip_F_0708_064598 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20090", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "6794", "CMH"), getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"));

		ITrip trip_E_0905_042208 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20095", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "20740", "OAK"), getFlight(flightsEWR2MDW_5_14_16, "7452", "DFW"));
		ITrip trip_F_0905_253675 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20095", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "20740", "OAK"), getFlight(flightsEWR2MDW_5_14_16, "7452", "DFW"));

		ITrip trip_E_0697_013688 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20096", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "25216", "RDU"), getFlight(flightsEWR2MDW_5_14_16, "12523", "HOU"));
		ITrip trip_F_0697_120046 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20096", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "25216", "RDU"), getFlight(flightsEWR2MDW_5_14_16, "12523", "HOU"));

		ITrip trip_E_0729_010784 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19481", "LGA"), getFlight(flightsEWR2MDW_5_14_16, "12529", "HOU"));
		ITrip trip_F_0729_106202 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19481", "LGA"), getFlight(flightsEWR2MDW_5_14_16, "12529", "HOU"));

		ITrip trip_E_1000_039678 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19480", "LGA"), getFlight(flightsEWR2MDW_5_14_16, "28389", "SFO"));

		ITrip trip_E_0855_026495 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19483", "LGA"), getFlight(flightsEWR2MDW_5_14_16, "23325", "PHX"));
		ITrip trip_F_0855_120753 = new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20100", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "19483", "LGA"), getFlight(flightsEWR2MDW_5_14_16, "23325", "PHX"));

		ITrip trip_E_0987_022575 = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20101", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "16961", "MSP"), getFlight(flightsEWR2MDW_5_14_16, "28389", "SFO"));

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
		tripsEWR2MDW_5_14_16_All.add(trip_E_1086_045196);
		tripsEWR2MDW_5_14_16_All.add(trip_F_1086_204823);
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
		tripsEWR2MDW_5_14_16_All.add(trip_E_1007_024931);
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
		tripsEWR2MDW_5_14_16_All.add(trip_E_0905_042208);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0905_253675);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0697_013688);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0697_120046);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0729_010784);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0729_106202);
		tripsEWR2MDW_5_14_16_All.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_All.add(trip_F_0855_120753);
		tripsEWR2MDW_5_14_16_All.add(trip_E_0987_022575);

		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0709_014836);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0765_010478);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1086_045196);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0769_006980);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0675_012197);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0860_013415);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0337_005378);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0731_012324);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1007_024931);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0819_009806);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0735_014165);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0730_015919);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0708_014511);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0905_042208);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0697_013688);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0729_010784);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_FilterEcon.add(trip_E_0987_022575);

		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0106_007746);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0709_051338);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0683_074662);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0765_089160);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1617_434938);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1086_204823);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0860_071411);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0337_040917);
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
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0905_253675);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0697_120046);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0729_106202);
		tripsEWR2MDW_5_14_16_FilterFirst.add(trip_F_0855_120753);

		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0337_005378);
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
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0730_015919);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0987_022575);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1007_024931);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0819_037260);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0337_040917);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_0905_042208);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_E_1086_045196);
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
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_1086_204823);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_0905_253675);
		tripsEWR2MDW_5_14_16_SortPrice.add(trip_F_1617_434938);

		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_1617_434938);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0905_253675);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_1086_204823);
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
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1086_045196);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0675_043475);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0905_042208);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0337_040917);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0819_037260);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_F_0769_028141);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0855_026495);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_1007_024931);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0987_022575);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0683_020388);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0565_016100);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0730_015919);
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
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0337_005378);
		tripsEWR2MDW_5_14_16_SortPriceReverse.add(trip_E_0106_004028);

		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0106_007746);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0337_005378);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0337_040917);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0368_050496);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0502_047245);
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
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0905_042208);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0905_253675);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0930_060754);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_0987_022575);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1007_024931);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1007_091721);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1086_045196);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1086_204823);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_SortTime.add(trip_F_1617_434938);

		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1617_067419);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1617_434938);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1402_047817);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1402_198293);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1225_025228);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1225_099729);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1176_027767);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1176_196684);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1086_045196);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1086_204823);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1007_024931);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_1007_091721);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_1000_039678);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0987_022575);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0937_015027);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0937_111179);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0930_013222);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0930_060754);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0905_042208);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0905_253675);
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
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0502_010880);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0502_047245);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0368_005531);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0368_050496);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0337_005378);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0337_040917);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_E_0106_004028);
		tripsEWR2MDW_5_14_16_SortTimeReverse.add(trip_F_0106_007746);

		ITrip trip_4311_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4311", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29041", "SJC"), getFlight(flightsMDW2EWR_5_15_16, "18908", "JFK"));
		ITrip trip_4314_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4314", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "3053", "BOS"), getFlight(flightsMDW2EWR_5_15_16, "13183", "IND"));
		ITrip trip_4315_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4315", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "13175", "IND"), getFlight(flightsMDW2EWR_5_15_16, "9375", "FLL"));
		ITrip trip_4316_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4316", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "13822", "MCI"), getFlight(flightsMDW2EWR_5_15_16, "22712", "PHL"));
		ITrip trip_4321_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4321", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "18259", "MSY"), getFlight(flightsMDW2EWR_5_15_16, "27807", "SAN"));
		ITrip trip_4321_2_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4321", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "18259", "MSY"), getFlight(flightsMDW2EWR_5_15_16, "27805", "SAN"));
		ITrip trip_4327_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4327", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "23989", "PIT"), getFlight(flightsMDW2EWR_5_15_16, "32217", "IAD"));
		ITrip trip_4328_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4328", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "32210", "IAD"), getFlight(flightsMDW2EWR_5_15_16, "29077", "SJC"));
		ITrip trip_4328_2_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4328", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "32203", "IAD"), getFlight(flightsMDW2EWR_5_15_16, "25269", "RDU"));
		ITrip trip_4329_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4329", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "3706", "CLT"), getFlight(flightsMDW2EWR_5_15_16, "29077", "SJC"));
		ITrip trip_4331_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4331", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "4979", "ORD"), getFlight(flightsMDW2EWR_5_15_16, "30345", "SEA"));
		ITrip trip_4334_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4334", "MDW"));
		ITrip trip_4336_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4336", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "3711", "CLT"), getFlight(flightsMDW2EWR_5_15_16, "29077", "SJC"));
		ITrip trip_4337_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4337", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29068", "SJC"), getFlight(flightsMDW2EWR_5_15_16, "3738", "CLT"));
		ITrip trip_4338_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4338", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "18908", "JFK"));
		ITrip trip_4339_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4339", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "5593", "CVG"), getFlight(flightsMDW2EWR_5_15_16, "26535", "SLC"));
		ITrip trip_4340_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4340", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "6237", "CLE"), getFlight(flightsMDW2EWR_5_15_16, "20802", "OAK"));
		ITrip trip_4342_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4342", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "25918", "SMF"), getFlight(flightsMDW2EWR_5_15_16, "29077", "SJC"));
		ITrip trip_4343_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4343", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "3719", "CLT"), getFlight(flightsMDW2EWR_5_15_16, "20802", "OAK"));
		ITrip trip_4343_2_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4343", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "3717", "CLT"), getFlight(flightsMDW2EWR_5_15_16, "19539", "LGA"));
		ITrip trip_4344_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4344", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "20797", "OAK"), getFlight(flightsMDW2EWR_5_15_16, "17037", "MSP"));
		ITrip trip_4345_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4345", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "1228", "ANC"), getFlight(flightsMDW2EWR_5_15_16, "3117", "BOS"));
		ITrip trip_4346_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4346", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "17016", "MSP"));
		ITrip trip_4347_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4347", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29707", "SNA"), getFlight(flightsMDW2EWR_5_15_16, "18303", "MSY"));
		ITrip trip_4347_2_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4347", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29710", "SNA"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS"));
		ITrip trip_4348_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4348", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "6243", "CLE"), getFlight(flightsMDW2EWR_5_15_16, "19539", "LGA"));
		ITrip trip_4349_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15127", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "29084", "SJC"));
		ITrip trip_4349_2_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15132", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS"));
		ITrip trip_4351_1_E = new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4351", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "9410", "FLL"));

		ITrip trip_4311_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4311", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29041", "SJC"), getFlight(flightsMDW2EWR_5_15_16, "18908", "JFK"));
		ITrip trip_4314_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4314", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "3053", "BOS"), getFlight(flightsMDW2EWR_5_15_16, "13183", "IND"));
		ITrip trip_4315_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4315", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "13175", "IND"), getFlight(flightsMDW2EWR_5_15_16, "9375", "FLL"));
		ITrip trip_4316_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4316", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "13822", "MCI"), getFlight(flightsMDW2EWR_5_15_16, "22712", "PHL"));
		ITrip trip_4321_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4321", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "18259", "MSY"), getFlight(flightsMDW2EWR_5_15_16, "27807", "SAN"));
		ITrip trip_4321_2_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4321", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "18259", "MSY"), getFlight(flightsMDW2EWR_5_15_16, "27805", "SAN"));
		ITrip trip_4323_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4323", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15753", "MEM"), getFlight(flightsMDW2EWR_5_15_16, "16386", "MIA"));
		ITrip trip_4325_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4325", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15753", "MEM"), getFlight(flightsMDW2EWR_5_15_16, "16386", "MIA"));
		ITrip trip_4327_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4327", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "23989", "PIT"), getFlight(flightsMDW2EWR_5_15_16, "32217", "IAD"));
		ITrip trip_4327_2_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4327", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "23987", "PIT"), getFlight(flightsMDW2EWR_5_15_16, "16386", "MIA"));
		ITrip trip_4327_3_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4327", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "23991", "PIT"), getFlight(flightsMDW2EWR_5_15_16, "16386", "MIA"));
		ITrip trip_4328_2_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4328", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "32203", "IAD"), getFlight(flightsMDW2EWR_5_15_16, "25269", "RDU"));
		ITrip trip_4331_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4331", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "4979", "ORD"), getFlight(flightsMDW2EWR_5_15_16, "30345", "SEA"));
		ITrip trip_4334_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4334", "MDW"));
		ITrip trip_4337_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4337", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29068", "SJC"), getFlight(flightsMDW2EWR_5_15_16, "3738", "CLT"));
		ITrip trip_4338_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4338", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "18908", "JFK"));
		ITrip trip_4339_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4339", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "5593", "CVG"), getFlight(flightsMDW2EWR_5_15_16, "26535", "SLC"));
		ITrip trip_4340_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4340", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "6237", "CLE"), getFlight(flightsMDW2EWR_5_15_16, "20802", "OAK"));
		ITrip trip_4343_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4343", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "3719", "CLT"), getFlight(flightsMDW2EWR_5_15_16, "20802", "OAK"));
		ITrip trip_4343_2_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4343", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "3717", "CLT"), getFlight(flightsMDW2EWR_5_15_16, "19539", "LGA"));
		ITrip trip_4344_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4344", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "20797", "OAK"), getFlight(flightsMDW2EWR_5_15_16, "17037", "MSP"));
		ITrip trip_4345_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4345", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "1228", "ANC"), getFlight(flightsMDW2EWR_5_15_16, "3117", "BOS"));
		ITrip trip_4346_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4346", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "17016", "MSP"));
		ITrip trip_4347_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4347", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29707", "SNA"), getFlight(flightsMDW2EWR_5_15_16, "18303", "MSY"));
		ITrip trip_4347_2_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4347", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29710", "SNA"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS"));
		ITrip trip_4348_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4348", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "6243", "CLE"), getFlight(flightsMDW2EWR_5_15_16, "19539", "LGA"));
		ITrip trip_4349_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15127", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "29084", "SJC"));
		ITrip trip_4349_2_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15132", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS"));
		ITrip trip_4351_1_F = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4351", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "9410", "FLL"));

		tripsMDW2EWR_5_15_16_All.add(trip_4311_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4314_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4315_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4316_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4321_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4321_2_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4327_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4328_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4328_2_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4329_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4331_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4334_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4336_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4337_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4338_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4339_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4340_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4342_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4343_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4343_2_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4344_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4345_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4346_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4347_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4347_2_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4348_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4349_1_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4349_2_E);
		tripsMDW2EWR_5_15_16_All.add(trip_4351_1_E);

		tripsMDW2EWR_5_15_16_All.add(trip_4311_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4314_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4315_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4316_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4321_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4321_2_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4323_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4325_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4327_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4327_2_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4327_3_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4328_2_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4331_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4334_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4337_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4338_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4339_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4340_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4343_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4343_2_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4344_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4345_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4346_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4347_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4347_2_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4348_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4349_1_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4349_2_F);
		tripsMDW2EWR_5_15_16_All.add(trip_4351_1_F);

		tripsMDW2EWR_5_15_16_filtered.add(trip_4342_1_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4343_1_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4343_2_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4344_1_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4345_1_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4346_1_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4347_1_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4347_2_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4348_1_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4349_1_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4349_2_E);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4351_1_E);

		tripsMDW2EWR_5_15_16_filtered.add(trip_4343_1_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4343_2_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4344_1_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4345_1_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4346_1_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4347_1_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4347_2_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4348_1_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4349_1_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4349_2_F);
		tripsMDW2EWR_5_15_16_filtered.add(trip_4351_1_F);
	}

	private static Flight getFlight(List<Flight> flights, String flightNum, String departureCode) {
		List<Flight> reducedFlights = flights.stream().filter(flight -> flight.getFlightNum().equals(flightNum)
				&& flight.getDepartureAirport().getCode().equals(departureCode)).collect(Collectors.toList());
		assertEquals(1, reducedFlights.size());
		return reducedFlights.get(0);
	}

	@Before
	public void setUp() throws Exception {
		ServerFactory.getServer().resetServer();
	}

	@After
	public void breakDown() throws Exception {
		ServerFactory.getServer().resetServer();
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from EWR to MDW Select the cheapest option Lock the server Book the
	 * trip unlock the server Search trips again confirm that the components
	 * flights have changed
	 * 
	 * @throws Exception
	 */
	@Test
	public void RegularOneWay() throws Exception {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("EWR");
		Airport arrivalAirport = Airports.getAirport("MDW");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		ITrip bookedTrip = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"));

		// verify that we get the expected trips
		assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(actualTrips));

		Consumer<String> callback = message -> {
			fail("Server Timed out when it shouldn't have");
		};

		boolean locked = flightServer.lockServer(callback);
		assertEquals(true, locked);

		flightServer.bookTrips(new LinkedList<ITrip>(Arrays.asList(bookedTrip)));

		flightServer.unlockServer();

		List<ITrip> changedTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);

		List<ITrip> prevTrips = new LinkedList<ITrip>();
		prevTrips.add(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31551", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA")));
		prevTrips.add(new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31551", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA")));
		prevTrips.add(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC")));
		prevTrips.add(new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC")));

		List<Flight> newFlights = FlightFactory.getInstance().parseFlightsFromXML(newTripEWR2MDWXML);
		List<ITrip> newTrips = new LinkedList<ITrip>();
		newTrips.add(new EconomyTrip(getFlight(newFlights, "20107", "EWR"), getFlight(newFlights, "31551", "TPA"),
				getFlight(newFlights, "16358", "MIA")));
		newTrips.add(new FirstClassTrip(getFlight(newFlights, "20107", "EWR"), getFlight(newFlights, "31551", "TPA"),
				getFlight(newFlights, "16358", "MIA")));
		newTrips.add(new EconomyTrip(getFlight(newFlights, "20107", "EWR"), getFlight(newFlights, "31550", "TPA"),
				getFlight(newFlights, "29044", "SJC")));
		newTrips.add(new FirstClassTrip(getFlight(newFlights, "20107", "EWR"), getFlight(newFlights, "31550", "TPA"),
				getFlight(newFlights, "29044", "SJC")));

		expectedTrips.removeAll(prevTrips);
		expectedTrips.addAll(newTrips);
		assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(changedTrips));
	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from EWR to MDW Select the most expensive option on the way
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

		Airport departureAirport = Airports.getAirport("EWR");
		Airport arrivalAirport = Airports.getAirport("MDW");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		DateTime returnTime = DateTime.of(2016, 5, 15, 0);
		List<ITrip> actualDepartureTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> actualreturnTrips = tripBuilder.getTrips(arrivalAirport, departureAirport, returnTime);
		List<ITrip> expectedDepartureTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);
		List<ITrip> expectedReturnTrips = new LinkedList<ITrip>(tripsMDW2EWR_5_15_16_All);

		ITrip bookedDepartureTrip = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"));
		ITrip bookedreturnTrip = new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15132", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS"));

		// verify that we get the expected trips
		assertEquals(SortUtil.sortByTravelTime(expectedDepartureTrips),
				SortUtil.sortByTravelTime(actualDepartureTrips));
		System.out.println("exd: " + expectedDepartureTrips.size() + " acd: " + actualDepartureTrips.size() + " exr: "
				+ expectedReturnTrips.size() + " acr: " + actualreturnTrips.size());
		assertEquals(SortUtil.sortByTravelTime(expectedReturnTrips), SortUtil.sortByTravelTime(actualreturnTrips));

		Consumer<String> callback = message -> {
			fail("Server Timed out when it shouldn't have");
		};

		// Check that the values are properly filtered
		List<ITrip> actualfiltered = FilterUtil.filterByTrip(bookedDepartureTrip, actualreturnTrips);
		assertEquals(SortUtil.sortByTravelTime(tripsMDW2EWR_5_15_16_filtered),
				SortUtil.sortByTravelTime(actualfiltered));

		boolean locked = flightServer.lockServer(callback);
		assertEquals(true, locked);

		flightServer.bookTrips(new LinkedList<ITrip>(Arrays.asList(bookedDepartureTrip)));
		flightServer.bookTrips(new LinkedList<ITrip>(Arrays.asList(bookedreturnTrip)));

		flightServer.unlockServer();

		// check that the depature trips have changed
		List<ITrip> changedDepartureTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);

		List<ITrip> prevDepartureTrips = new LinkedList<ITrip>();
		prevDepartureTrips.add(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31551", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA")));
		prevDepartureTrips.add(new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31551", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA")));
		prevDepartureTrips.add(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC")));
		prevDepartureTrips.add(new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC")));

		List<Flight> newDepatureFlights = FlightFactory.getInstance().parseFlightsFromXML(newTripEWR2MDWXML);
		List<ITrip> newDepatureTrips = new LinkedList<ITrip>();
		newDepatureTrips.add(new EconomyTrip(getFlight(newDepatureFlights, "20107", "EWR"),
				getFlight(newDepatureFlights, "31551", "TPA"), getFlight(newDepatureFlights, "16358", "MIA")));
		newDepatureTrips.add(new FirstClassTrip(getFlight(newDepatureFlights, "20107", "EWR"),
				getFlight(newDepatureFlights, "31551", "TPA"), getFlight(newDepatureFlights, "16358", "MIA")));
		newDepatureTrips.add(new EconomyTrip(getFlight(newDepatureFlights, "20107", "EWR"),
				getFlight(newDepatureFlights, "31550", "TPA"), getFlight(newDepatureFlights, "29044", "SJC")));
		newDepatureTrips.add(new FirstClassTrip(getFlight(newDepatureFlights, "20107", "EWR"),
				getFlight(newDepatureFlights, "31550", "TPA"), getFlight(newDepatureFlights, "29044", "SJC")));

		expectedDepartureTrips.removeAll(prevDepartureTrips);
		expectedDepartureTrips.addAll(newDepatureTrips);
		assertEquals(SortUtil.sortByTravelTime(expectedDepartureTrips),
				SortUtil.sortByTravelTime(changedDepartureTrips));

		// check that the return trips have changed
		List<ITrip> changedReturnTrips = tripBuilder.getTrips(arrivalAirport, departureAirport, returnTime);
		List<ITrip> prevReturnTrips = new LinkedList<ITrip>();
		prevReturnTrips.add(new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4347", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29710", "SNA"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS")));
		prevReturnTrips.add(new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15127", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "29084", "SJC")));
		prevReturnTrips.add(new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15132", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS")));

		prevReturnTrips.add(new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4347", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29710", "SNA"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS")));
		prevReturnTrips.add(new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15127", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "29084", "SJC")));
		prevReturnTrips.add(new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15132", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "3113", "BOS")));

		List<Flight> newReturnFlights = FlightFactory.getInstance().parseFlightsFromXML(newTripMDW2EWRXML);
		List<ITrip> newReturnTrips = new LinkedList<ITrip>();
		newReturnTrips.add(new EconomyTrip(getFlight(flightsMDW2EWR_5_15_16, "4347", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29710", "SNA"), getFlight(newReturnFlights, "3113", "BOS")));
		newReturnTrips.add(new EconomyTrip(getFlight(newReturnFlights, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15127", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "29084", "SJC")));
		newReturnTrips.add(new EconomyTrip(getFlight(newReturnFlights, "4349", "MDW"),
				getFlight(newReturnFlights, "15132", "LAX"), getFlight(newReturnFlights, "3113", "BOS")));

		newReturnTrips.add(new FirstClassTrip(getFlight(flightsMDW2EWR_5_15_16, "4347", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "29710", "SNA"), getFlight(newReturnFlights, "3113", "BOS")));
		newReturnTrips.add(new FirstClassTrip(getFlight(newReturnFlights, "4349", "MDW"),
				getFlight(flightsMDW2EWR_5_15_16, "15127", "LAX"), getFlight(flightsMDW2EWR_5_15_16, "29084", "SJC")));
		newReturnTrips.add(new FirstClassTrip(getFlight(newReturnFlights, "4349", "MDW"),
				getFlight(newReturnFlights, "15132", "LAX"), getFlight(newReturnFlights, "3113", "BOS")));

		expectedReturnTrips.removeAll(prevReturnTrips);
		expectedReturnTrips.addAll(newReturnTrips);
		assertEquals(SortUtil.sortByTravelTime(expectedReturnTrips), SortUtil.sortByTravelTime(changedReturnTrips));
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from EWR to MDW Select the cheapest option Lock the server Wait for
	 * 2.5 minutes attempt to book the trip
	 * 
	 * @throws Exception
	 */
	@Test
	public void ServerTimeout() throws Exception {
		List<Boolean> success = new LinkedList<Boolean>();
		success.add(false);
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("EWR");
		Airport arrivalAirport = Airports.getAirport("MDW");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		ITrip bookedTrip = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"));

		// verify that we get the expected trips
		assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(actualTrips));

		Consumer<String> callback = message -> {
			success.set(0, true);
		};

		boolean locked = flightServer.lockServer(callback);
		assertEquals(true, locked);

		TimeUnit.SECONDS.sleep(TIMEOUT + 30);

		assertTrue("The callback was not called", success.get(0));

		try {
			flightServer.bookTrips(new LinkedList<ITrip>(Arrays.asList(bookedTrip)));
			fail("Able to book a trip which should not happen");
		} catch (Exception e) {

		}
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from EWR to MDW Sort trips by the price ascending Sort trips by the
	 * price descending
	 */
	@Test
	public void SortbyPrice() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("EWR");
		Airport arrivalAirport = Airports.getAirport("MDW");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(actualTrips));

		List<ITrip> expectedSortedASCTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_SortPrice);
		List<ITrip> expectedSortedDESCTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_SortPriceReverse);
		List<ITrip> actualASCTrips = SortUtil.sortByPrice(actualTrips);
		List<ITrip> actualDESCTrips = SortUtil.sortByPrice(actualTrips, true);

		// verify that we get the expected trips
		assertEquals(expectedSortedASCTrips, actualASCTrips);
		assertEquals(expectedSortedDESCTrips, actualDESCTrips);
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from EWR to MDW Sort trips by the travel time descending Sort trips
	 * by the travel time ascending
	 */
	@Test
	public void SortbyTravelTime() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("EWR");
		Airport arrivalAirport = Airports.getAirport("MDW");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(actualTrips));

		List<ITrip> expectedSortedASCTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_SortTime);
		List<ITrip> expectedSortedDESCTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_SortTimeReverse);
		List<ITrip> actualASCTrips = SortUtil.sortByTravelTime(actualTrips);
		List<ITrip> actualDESCTrips = SortUtil.sortByTravelTime(actualTrips, true);

		// verify that we get the expected trips
		assertEquals(expectedSortedASCTrips, actualASCTrips);
		assertEquals(expectedSortedDESCTrips, actualDESCTrips);
	}

	/**
	 * 
	 * For a flight from EWR to MDW, confirm that latitude and longitude are
	 * converted to offsets correctly. Then confirm that the GMT times of the
	 * departure and arrival times are converted correctly. Finally confirm that
	 * the duration of the flight is calculated correctly given the different
	 * time zone offsets.
	 * 
	 */
	@Test
	public void LocalTime() {
		FlightServer server = ServerFactory.getServer();
		Flight flight = getFlight(server.getFlightsDeparting(Airports.getAirport("EWR"), DateTime.of(2016, 5, 14, 0)), "20083", "EWR");
		
		//Confirm the lat longs are converted correctly
		Airport departureAirport = flight.getDepartureAirport();
		Airport arrivalAirport = flight.getArrivalAirport();
		
		assertEquals(-4*60*60, departureAirport.getOffset());
		assertEquals(-5*60*60, arrivalAirport.getOffset());
		
		//Confirm that the departure time and arrival time are converted and printed in local time
		assertEquals("05/14/2016 04:03 -04:00", flight.getDepartureTime().toString());
		assertEquals("05/14/2016 04:49 -05:00", flight.getArrivalTime().toString());
		
		//Confirm that the total time is calculated correctly
		assertEquals("106", flight.getDuration());
	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from EWR to MDW Filter and confirm trips for only economy
	 * seats Filter and confirm trips for only first class seats Confirm that
	 * those two filters make up the entire list of trips
	 */
	@Test
	public void FilterBySeat() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("EWR");
		Airport arrivalAirport = Airports.getAirport("MDW");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(actualTrips));

		List<ITrip> expectedFilteredECONTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_FilterEcon);
		List<ITrip> expectedFilteredFIRSTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_FilterFirst);
		List<ITrip> actualFilteredECONTrips = FilterUtil.filterByEconomy(actualTrips);
		List<ITrip> actualFilteredFIRSTrips = FilterUtil.filterByFirstClass(actualTrips);

		// verify that we get the expected trips
		assertEquals(SortUtil.sortByTravelTime(expectedFilteredECONTrips),
				SortUtil.sortByTravelTime(actualFilteredECONTrips));
		assertEquals(SortUtil.sortByTravelTime(expectedFilteredFIRSTrips),
				SortUtil.sortByTravelTime(actualFilteredFIRSTrips));

		List<ITrip> combined = new LinkedList<ITrip>(actualFilteredECONTrips);
		combined.addAll(actualFilteredFIRSTrips);
		assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(combined));

	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from BOS to LAX For each trip book the last leg of the trip
	 * Research Confirming that the specific trip both seat options no longer
	 * contains that trip Confirm after all possible trips there are no trips
	 * left
	 * 
	 * @throws Exception
	 */
	@Test
	public void NoFlightsIfAllBooked() throws Exception {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("EWR");
		Airport arrivalAirport = Airports.getAirport("MDW");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> fullActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>(tripsEWR2MDW_5_14_16_All);

		assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(fullActualTrips));
		List<List<ITrip>> tripsToRemove = new LinkedList<List<ITrip>>();
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20083", "EWR")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "20083", "EWR"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "28389", "SFO"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "16358", "MIA"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "22026", "MCO")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "22026", "MCO"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "16361", "MIA")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "16361", "MIA"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "16388", "MIA")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "16388", "MIA"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "22053", "MCO")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "22053", "MCO"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "30963", "STL")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "30963", "STL"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "3650", "CLT"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "8082", "DEN"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "28365", "SFO")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "28365", "SFO"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "9336", "FLL")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "9336", "FLL"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "13140", "IND")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "13140", "IND"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "9988", "RSW"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "16954", "MSP")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "16954", "MSP"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "26470", "SLC")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "26470", "SLC"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "13788", "MCI"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "3027", "BOS")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "3027", "BOS"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "23929", "PIT")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "23929", "PIT"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "27116", "SAT")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "27116", "SAT"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "7452", "DFW")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "7452", "DFW"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "12523", "HOU")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "12523", "HOU"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "12529", "HOU")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "12529", "HOU"))));
		tripsToRemove.add(Arrays.asList(new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "23325", "PHX")),
				new FirstClassTrip(getFlight(flightsEWR2MDW_5_14_16, "23325", "PHX"))));

		tripsToRemove.forEach(aTripList -> {
			Consumer<String> callback = message -> {
				fail("Server Timed out when it shouldn't have");
			};

			Set<ITrip> toRemoveExpected = new HashSet<ITrip>();

			aTripList.forEach(aTrip -> {
				try {

					boolean locked = flightServer.lockServer(callback);
					assertEquals(true, locked);
					while (flightServer.checkTripsAvailable(new LinkedList<ITrip>(Arrays.asList(aTrip)))) {
						try {
							flightServer.bookTrips(new LinkedList<ITrip>(Arrays.asList(aTrip)));
						} catch (Exception e) {
							e.printStackTrace();
							fail("Unable to book a trip which should not happen");
						}
					}
					flightServer.unlockServer();

				} catch (Exception e) {
					e.printStackTrace();
					fail("Unable to check a trip which should not happen");
				}

				expectedTrips.forEach(eTrip -> {
					if (eTrip.getLegs().containsAll(aTrip.getLegs())) {
						toRemoveExpected.add(eTrip);
					}
				});
			});

			expectedTrips.removeAll(toRemoveExpected);

			List<ITrip> partialActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
			assertEquals(SortUtil.sortByTravelTime(expectedTrips), SortUtil.sortByTravelTime(partialActualTrips));

		});

		List<ITrip> finalActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);

		assertEquals(0, finalActualTrips.size());
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

	/**
	 * Confirm that the system cannot book a trip if there are no seats left
	 * 
	 * @throws Exception
	 */
	@Test
	public void bookFullFlight() throws Exception {
		FlightServer flightServer = ServerFactory.getServer();

		ITrip bookTrip = new EconomyTrip(getFlight(flightsEWR2MDW_5_14_16, "20107", "EWR"),
				getFlight(flightsEWR2MDW_5_14_16, "31550", "TPA"), getFlight(flightsEWR2MDW_5_14_16, "29044", "SJC"));

		Consumer<String> callback = message -> {
			fail("Server Timed out when it shouldn't have");
		};

		boolean locked = flightServer.lockServer(callback);
		assertEquals(true, locked);

		while (flightServer.checkTripsAvailable(new LinkedList<ITrip>(Arrays.asList(bookTrip)))) {
			flightServer.bookTrips(new LinkedList<ITrip>(Arrays.asList(bookTrip)));
		}
		boolean booked = flightServer.bookTrips(new LinkedList<ITrip>(Arrays.asList(bookTrip)));
		flightServer.unlockServer();
		assertEquals(false, booked);
	}

}
