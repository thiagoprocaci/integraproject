package com.integrareti.integraframework.exceptions;

import com.integrareti.integraframework.business.Person;

/**
 * Describes a entity tha represents a DeleteUserException
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class DeleteUserException extends Exception {
	private static final String MSG_Exception = "This user can not be deleted: ";

	/**
	 * Constructs a new DeleteUserException
	 * 
	 * @param msg
	 */
	public DeleteUserException(Person person) {
		super(MSG_Exception + person.getRegistry());
	}
}
