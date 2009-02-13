package com.integrareti.integraframework.exceptions;

/**
 * An exception class for when there's a problem handling SAML messages.
 */
@SuppressWarnings("serial")
public class SamlException extends Exception {

	protected String message = "";

	/**
	 * Creates a new SamlException
	 */
	public SamlException() {

	}

	/**
	 * Creates a new SamlException
	 * 
	 * @param e
	 */
	public SamlException(Throwable e) {
		super(e);
	}

	/**
	 * Creates a new SamlException
	 * 
	 * @param message
	 */
	public SamlException(String message) {
		this.message = message;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Returns the message exception
	 */
	public String toString() {
		return "SAML exception: " + message;
	}
}