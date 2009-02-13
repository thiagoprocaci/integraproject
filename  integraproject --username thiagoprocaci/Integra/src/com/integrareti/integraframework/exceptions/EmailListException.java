package com.integrareti.integraframework.exceptions;

/**
 * Describes a entity tha represents an emaillist exception
 * @author Thiago Baesso Procaci
 *
 */
@SuppressWarnings("serial")
public class EmailListException extends Exception {

	
	
	/**
	 * Creates a new EmailListException
	 * @param msg
	 */
	public EmailListException(String msg){
		super(msg);
	}
	
}
