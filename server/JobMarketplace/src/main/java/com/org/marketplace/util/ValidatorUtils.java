package com.org.marketplace.util;

public final class ValidatorUtils {
	public static boolean validateName(String name) {
		final String pattern = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";

		if (name.matches(pattern)) {
			return true;
		}
		return false;
	}
}
