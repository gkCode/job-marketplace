package com.org.marketplace.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic utility
 * 
 * @author gauravkahadane
 *
 */
public final class AppUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUtils.class);

	/**
	 * Converts a date to specific format
	 * 
	 * @param dateStr LocalDate in a string format
	 * @return LocalDate
	 * @throws Exception
	 */
	public static LocalDate getDate(String dateStr) throws Exception {
		try {
			if (dateStr == null) {
				throw new NullPointerException("Date is null");
			}

			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
					Locale.ENGLISH);

			LocalDate date = LocalDate.parse(dateStr, inputFormatter);

			return date;
		} catch (Exception e) {
			LOGGER.error("Failed to parse the date: " + e);
			throw new Exception("Enter a valid date");
		}
	}
}
