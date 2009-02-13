package com.integrareti.integraframework.business;

import java.io.Serializable;

/**
 * Identifiable interface
 * 
 * @author Thiago
 * 
 * @param <T>
 */
public interface Identifiable<T extends Serializable> extends Serializable{
	
	/**
	 * 
	 * @return Returns a object by id
	 */
	T getId();

	/**
	 * Sets a object by id
	 * 
	 * @param id
	 */
	void setId(T id);
}
