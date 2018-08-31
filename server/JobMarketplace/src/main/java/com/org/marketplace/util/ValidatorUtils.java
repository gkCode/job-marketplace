package com.org.marketplace.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validation utility
 * 
 * @author gauravkahadane
 *
 */
public final class ValidatorUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorUtils.class);

	/**
	 * Validates a name
	 * @param name project name
	 * @return true if name is valid otherwise false
	 * @throws NullPointerException
	 * @throws Exception
	 */
	public static boolean validateName(String name) throws NullPointerException, Exception {
		if (name == null) {
			throw new NullPointerException();
		}

		final String pattern = "^[a-zA-Z]{3}\\w*[-]*\\w*$";

		try {
			if (name.matches(pattern)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Invalid Project Name: "+e);
			throw new Exception("Invalid Project Name");
		}

		return false;
	}
}
