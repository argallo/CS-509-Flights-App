package com.csanon.time;

import java.time.Duration;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Immutable date time class
 */
public final class DateTime {
	private static final DateTimeFormatter serverDateTimeFormat = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm z");
	private static final DateTimeFormatter serverDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd");
	private static final DateTimeFormatter humanDateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm z");
	private static final TimeZoneLookup timeZoneLookup = TimeZoneLookup.getInstance();
	private final OffsetDateTime dateTime;

	/**
	 * Creates a DateDime from year month and day at midnight with the given offset
	 * 
	 * @param year
	 *            Year
	 * @param month
	 *            Month
	 * @param day
	 *            Day
	 * @param offset
	 *            Time zone offset
	 * @return New DateTime
	 */
	public static DateTime of(int year, int month, int day, int offset) {
		return new DateTime(OffsetDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.ofHours(offset)));
	}

	/**
	 * Creates a new DateTime with the offset from the given lat long
	 * 
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
		this(dateString, timeZoneLookup.getOffsetFromLatLong(lat, lon));
	}

	private DateTime(String dateString, int offset) {
		OffsetDateTime DateTime = ZonedDateTime.parse(dateString, serverDateTimeFormat).toOffsetDateTime();
		//System.out.println("Offset" + offset);
		dateTime = DateTime.withOffsetSameInstant(ZoneOffset.of("" + offset));
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

	public boolean isUTC() {
		return dateTime.getOffset().getTotalSeconds() == 0;
	}

	@Override
	public String toString() {
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
}
