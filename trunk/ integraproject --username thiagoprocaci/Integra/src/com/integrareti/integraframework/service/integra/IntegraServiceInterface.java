package com.integrareti.integraframework.service.integra;

import java.util.List;

import com.integrareti.integraframework.business.Identifiable;

/**
 * This interface offers the basics services to access integra database
 * 
 * @author Thiago
 * 
 */
public interface IntegraServiceInterface<T extends Identifiable<ID>, ID extends java.io.Serializable> {

	/**
	 * Deletes an object
	 * 
	 * @param object
	 * @return deleted object
	 * @throws Exception
	 */
	public void delete(T object) throws Exception;

	/**
	 * @return Returns all
	 * @param class
	 * @throws Exception
	 */
	public List<T> getAll() throws Exception;
	
	
	/**
	 * @return Returns all by domainName
	 * @param class
	 * @throws Exception
	 */
	public List<T> getAllByDomainName(String domainName) throws Exception;	
	

	/**
	 * @return Returns an object by id
	 * 
	 * @param class
	 * @param clazz
	 * @param objPK
	 * @throws Exception
	 */
	public T getById(ID objID) throws Exception;

	/**
	 * Saves an object
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void save(T object) throws Exception;
	

	/**
	 *  Reattach an object to Hibernate Session
	 * @param o
	 */
	public void reattach(Object o);
	
	/**
	 * Initialize an object - hibernate 
	 * @param o
	 */
	public void initialize(Object o);

	/**
	 * Open hibernate session
	 */
	public void openSession();
	
	/**
	 * Closed hibernate session
	 */
	public void closeSession();
}
