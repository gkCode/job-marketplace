package com.org.marketplace.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gauravkahadane
 *
 */
public final class AppUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUtils.class);

	public static LocalDate getDate(String dateStr) {
		try {
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);
			LocalDate date = LocalDate.parse(dateStr, inputFormatter);
			return date;
		} catch (Exception e) {
			LOGGER.error("Parsing Date: "+e);
		}
		return null;
	}
}
