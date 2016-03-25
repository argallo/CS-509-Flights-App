package com.csanon.time;

import java.io.IOException;
import java.io.StringReader;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

public final class DateTime {
	private static final DateTimeFormatter serverDateTimeFormat = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm z");
	private static final DateTimeFormatter serverDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd");
	private final OffsetDateTime dateTime;

	public DateTime(String dateString, double lat, double lon) {
		this(dateString, getOffsetByLatLong(lat, lon));
	}

	public DateTime(String dateString, int offset) {
		OffsetDateTime DateTime = ZonedDateTime.parse(dateString, serverDateTimeFormat).toOffsetDateTime();
		dateTime = DateTime.withOffsetSameInstant(ZoneOffset.ofHoursMinutesSeconds(0, 0, offset));
	}

	public DateTime(OffsetDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public DateTime getUTC() {
		return new DateTime(dateTime.withOffsetSameInstant(ZoneOffset.ofHours(0)));
	}
	
	public String toServerDateString(){
		return dateTime.format(serverDateFormat);
	}

	public static int getOffsetByLatLong(double Lat, double Long) {
		int offset = 0;
		try {
			HttpRequest request = Unirest.get("http://api.timezonedb.com").queryString("lat", new Double(Lat))
					.queryString("lng", new Double(Long)).queryString("key", "NWZDDPVDUNKW");
			HttpResponse<String> response = request.asString();
			String result = response.getBody();

			/**
			 * load the xml string into a DOM document check whether the result
			 * is valid and then return the offset
			 */
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				InputSource inputSource = new InputSource();
				inputSource.setCharacterStream(new StringReader(result));

				Document docTimezone = docBuilder.parse(inputSource);

				Element topelement = docTimezone.getDocumentElement();
				String status = topelement.getAttributeNode("status").getValue();

				if (!status.equals("OK")) {
					// TODO : throw exception saying not a valid message
				}

				offset = Integer.parseInt(topelement.getAttributeNode("gmtOffset").getValue());

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				// TODO: handle
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: handle
			} catch (SAXException e) {
				e.printStackTrace();
				// TODO: handle
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO: handle
		}
		return offset;
	}
}
