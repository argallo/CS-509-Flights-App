package com.csanon;

import java.io.IOException;
import java.io.StringReader;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class TimeUtil {
	private static DateTimeFormatter dtparseformat = DateTimeFormatter.ofPattern("yyyy L dd hh:mm Z");
	
	public static OffsetDateTime string2OffsetDateTime(String aDateTime, double Lat, double Long) throws ParserConfigurationException, IOException, SAXException {
		int offset = getOffsetByLatLong(Lat, Long);
		return string2OffsetDateTime(aDateTime, offset);		
	}
	
	public static OffsetDateTime string2OffsetDateTime(String aDateTime, int aOffset) {
		OffsetDateTime DateTime = OffsetDateTime.parse(aDateTime, dtparseformat);
		DateTime = DateTime.withOffsetSameInstant(ZoneOffset.ofHoursMinutesSeconds(0, 0, aOffset));
		return DateTime;
	}
	
	public static int getOffsetByLatLong(double Lat, double Long) throws ParserConfigurationException, IOException, SAXException {
		String result = null;
		try {
			HttpRequest request = Unirest.get("http://api.timezonedb.com")
					.queryString("lat", new Double(Lat))
					.queryString("lng", new Double(Long))
					.queryString("key", "NWZDDPVDUNKW");
			HttpResponse<String> response = request.asString();
			result = response.getBody();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * load the xml string into a DOM document check whether the result is valid and then return the offset
		 */
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(result));
			
			Document docTimezone =  docBuilder.parse(inputSource);
			
			Element topelement = docTimezone.getDocumentElement();
			String status = topelement.getAttributeNode("status").getValue();
			
			if (!status.equals("OK")) {
				//TODO : throw exception saying not a valid message
			}
			
			int offset = Integer.parseInt(topelement.getAttributeNode("gmtOffset").getValue());
			
			return offset;
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw e;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		catch (SAXException e) {
			e.printStackTrace();
			throw e;
		}
	}	
}
