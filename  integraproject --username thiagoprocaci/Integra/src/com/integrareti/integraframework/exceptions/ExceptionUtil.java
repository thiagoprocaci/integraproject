package com.integrareti.integraframework.exceptions;

/**
 * This class offers methods to manipulate exceptions messages
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class ExceptionUtil {
	/**
	 * 
	 * @param e
	 * 
	 * @return Returns a string with the message and cause of the exception
	 */
	public static String formatMessage(Exception e) {
		String s = "Message: " + e.getMessage() + "\n";
		s += " Cause: " + e.getCause();
		return s;
	}
}
