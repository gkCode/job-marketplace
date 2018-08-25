package com.org.marketplace.util;

public final class ValidatorUtils {
	public static boolean validateName(String name) {
		final String pattern = "^.*[a-zA-Z]{2}[0-9]{7}.*$";

		if (name.matches(pattern)) {
			return true;
		}
		return true;
	}
}
