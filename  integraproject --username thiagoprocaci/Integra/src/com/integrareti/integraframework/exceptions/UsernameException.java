package com.integrareti.integraframework.exceptions;

/**
 * Describes a entity that represents an username exception
 * 
 * @author Thiago Baesso Procaci
 */
@SuppressWarnings("serial")
public class UsernameException extends Exception {
	private static final String MSG_EXCEPTION = "Detailed error: ";

	/**
	 * Constructs a new UsernameException with the specified detail message
	 * 
	 * @param msg
	 */
	public UsernameException(String msg) {
		super(MSG_EXCEPTION + msg);
	}
}
