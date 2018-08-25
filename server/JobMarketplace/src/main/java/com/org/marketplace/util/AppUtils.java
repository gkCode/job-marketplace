package com.org.marketplace.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @author gauravkahadane
 *
 */
public final class AppUtils {
	public static Instant getInstant(String dateStr) {

		return Instant.now();
	}

	public static Date getDate(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");
		try {
//			return formatter.parse(dateStr);
			return new Date();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
