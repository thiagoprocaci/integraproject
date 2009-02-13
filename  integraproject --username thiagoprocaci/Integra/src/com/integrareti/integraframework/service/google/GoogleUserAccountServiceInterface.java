package com.integrareti.integraframework.service.google;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.integra.IntegraServiceInterface;

/**
 * This interface offers services o manipulates person at integra and google
 * database
 * 
 * @author Thiago
 * 
 */
public interface GoogleUserAccountServiceInterface extends
		IntegraServiceInterface<Person, Integer> {

	/**
	 * @return The object form the database of the specified class an ID
	 * @param class
	 * @param objPK
	 * 
	 * @param objID
	 * @throws IOException
	 * @throws ServiceException
	 * @throws AppsForYourDomainException
	 * @exception Exception
	 */
	public Person getFromGoogleById(Integer id)
			throws AppsForYourDomainException, DataAccessException,Exception;

	/**
	 * 
	 * @param googleAccount
	 * @return Returns a person by googleAccount
	 * @throws DataAccessException
	 */
	public Person getByGoogleAccount(String googleAccount,String domainName)
			throws Exception;

	/**
	 * 
	 * @param registry
	 * @return Returns a person by registry
	 * @throws DataAccessException
	 */
	public Person getByRegistry(String registry) throws Exception;

	/**
	 * 
	 * @param registries
	 * @return Returns a list of person by a list of registries
	 * @throws DataAccessException
	 */
	public List<Person> getByRegistry(List<String> registries)
			throws Exception;
	
	/**
	 * Checks if a person exists in integra database - Needs to open connection
	 * @param registry
	 * @return Returns id if person exist 
	 * @throws Exception
	 */
	public Integer isPersonSaved(String registry,boolean stillOpenConnection) throws Exception;
	
	
	/**
	 * Checks if people exist in integra database by registry.
	 * @param registries
	 * @return Returns a list of saved people
	 * @throws Exception
	 */
	public List<Person> arePeopleSaved(List<String> registries) throws Exception;
	
	/**
	 * 
	 * @param registry
	 * @return Returns person`s groups
	 * @throws Exception
	 */
	public List<Group> getGroups(String registry) throws Exception;
	
	/**
	 * Open integra database connection -using JDBC
	 * @throws Exception
	 */
	public void openConnection() throws Exception;
	
	/**
	 * Close integra database connection - -using JDBC
	 * @throws Exception
	 */
	public void closeConnection() throws Exception;

}
