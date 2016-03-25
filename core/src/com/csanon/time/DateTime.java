package com.csanon.time;

import java.io.IOException;
import java.io.StringReader;
import java.time.Duration;
import java.time.LocalTime;
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

/**
 * Immutable date time class
 */
public final class DateTime {
	private static final DateTimeFormatter serverDateTimeFormat = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm z");
	private static final DateTimeFormatter serverDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd");
	private static final DateTimeFormatter humanDateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm z");
	private final OffsetDateTime dateTime;

	/**
	 * Creates a DateDime from year month and day at midnight with the given offset
	 * @param year Year
	 * @param month Month
	 * @param day Day
	 * @param offset Time zone offset
	 * @return New DateTime
	 */
	public static DateTime of(int year, int month, int day, int offset) {
		return new DateTime(OffsetDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.ofHours(offset)));
	}

	/**
	 * Creates a new DateTime with the offset from the given lat long
	 * @param dateString
	 * @param lat
	 * @param lon
	 * @return New DateTime
	 */
	public static DateTime of(String dateString, double lat, double lon) {
		return new DateTime(dateString, lat, lon);
	}

	public static DateTime of(String dateString, int offset) {
		return new DateTime(dateString, offset);
	}

	public static DateTime of(OffsetDateTime dateTime) {
		return new DateTime(dateTime);
	}

	private DateTime(String dateString, double lat, double lon) {
		this(dateString, getOffsetByLatLong(lat, lon));
	}

	private DateTime(String dateString, int offset) {
		OffsetDateTime DateTime = ZonedDateTime.parse(dateString, serverDateTimeFormat).toOffsetDateTime();
		dateTime = DateTime.withOffsetSameInstant(ZoneOffset.ofHoursMinutesSeconds(0, 0, offset));
	}

	private DateTime(OffsetDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public DateTime getUTC() {
		return new DateTime(dateTime.withOffsetSameInstant(ZoneOffset.ofHours(0)));
	}

	public String toServerDateString() {
		return dateTime.format(serverDateFormat);
	}

	public DateTime getMidnight() {
		return new DateTime(OffsetDateTime.of(dateTime.toLocalDate(), LocalTime.MIDNIGHT, dateTime.getOffset()));
	}

	public DateTime getNextDay() {
		return new DateTime(dateTime.plusDays(1));
	}

	public int compareTo(DateTime other) {
		return dateTime.compareTo(other.dateTime);
	}

	public DateTime plusSeconds(long seconds) {
		return new DateTime(dateTime.plusSeconds(seconds));
	}

	public Duration getDifference(DateTime other) {
		return Duration.between(dateTime, other.dateTime);
	}

	public static int getOffsetByLatLong(double Lat, double Long) {
		int offset = 0;
		try {
			HttpRequest request = Unirest.get("http://api.timezonedb.com").queryString("lat", new Double(Lat))
					.queryString("lng", new Double(Long)).queryString("key", "NWZDDPVDUNKW");
			HttpResponse<String> response = request.asString();
			String result = response.getBody();

			/**
			 * load the xml string into a DOM document check whether the result is valid and then return the offset
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
	
	@Override
	public String toString(){
		return dateTime.toZonedDateTime().format(humanDateTimeFormat);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DateTime other = (DateTime) obj;
		if (dateTime == null) {
			if (other.dateTime != null) {
				return false;
			}
		} else if (!dateTime.equals(other.dateTime)) {
			return false;
		}
		return true;
	}

	public boolean isUTC() {
		return dateTime.getOffset().getTotalSeconds() == 0;
	}
}
