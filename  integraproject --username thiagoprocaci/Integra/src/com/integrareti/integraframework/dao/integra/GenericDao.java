package com.integrareti.integraframework.dao.integra;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import com.integrareti.integraframework.business.Identifiable;

/**
 * This interface offers the basics operations to access integra database
 * 
 * @author Thiago
 * 
 * @param <Reg>
 * @param <ID>
 */
public interface GenericDao<Reg extends Identifiable<ID>, ID extends Serializable> {

	/**
	 * Saves an object
	 * 
	 * @param row
	 */
	public void save(Reg row) throws Exception;

	/**
	 * 
	 * @return Returns all
	 */
	public List<Reg> getAll() throws Exception;
	
	/**
	 * 
	 * @return Returns all by domainName
	 */
	public List<Reg> getAllByDomainName(String domainName) throws Exception;

	/**
	 * Deletes an object
	 * 
	 * @param row
	 */
	public void delete(Reg row) throws Exception;

	/**
	 * 
	 * @param id
	 * @return Returns a object by id
	 */
	public Reg getById(ID id) throws Exception;
	
	/**
	 * 
	 * @return Returns hibernate session
	 */
	public Session getHibernateSession();
	
	/**
	 * Open hibernate session
	 */
	public void openSession();
	
	/**
	 * Closed hibernate session
	 */
	public void closeSession();
	
	
}
