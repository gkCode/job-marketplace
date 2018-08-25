package com.org.marketplace.exception;

import static com.org.marketplace.exception.PreCondition.notEmpty;
import static com.org.marketplace.exception.PreCondition.notNull;

/**
 * @author gauravkahadane
 *
 */
public class FieldError {
	private final String field;

	private final String message;

	FieldError(String field, String message) {
		notNull(field, "Field cannot be null.");
		notEmpty(field, "Field cannot be empty");

		notNull(message, "Message cannot be null.");
		notEmpty(message, "Message cannot be empty.");

		this.field = field;
		this.message = message;
	}

	public String getField() {
		return field;
	}

	public String getMessage() {
		return message;
	}
}
