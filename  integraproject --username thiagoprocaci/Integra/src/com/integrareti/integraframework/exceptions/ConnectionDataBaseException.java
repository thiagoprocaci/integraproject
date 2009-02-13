package com.integrareti.integraframework.exceptions;

/**
 * Describes a entity tha represents a Connection DataBase exception
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class ConnectionDataBaseException extends Exception {
	private static final String MSG_Exception = "Detailed error: The connection is not open";

	/**
	 * Constructs a new Connection DataBase Exception
	 * 
	 * @param msg
	 */
	public ConnectionDataBaseException() {
		super(MSG_Exception);
	}
}
