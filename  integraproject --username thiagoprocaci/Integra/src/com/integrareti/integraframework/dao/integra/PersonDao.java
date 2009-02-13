package com.integrareti.integraframework.dao.integra;

import java.util.List;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;

/**
 * This interface offers methods to manipulates person at integra database
 * 
 * @author Thiago
 * 
 */
public interface PersonDao extends GenericDao<Person, Integer> {
	/**
	 * 
	 * @param domainName
	 * @return Returns the people from a domain
	 * @throws Exception
	 */
	public List<Person> getGoogleDomainAdmins(String domainName) throws Exception;

	/**
	 * 
	 * @param googleAccount
	 * @return Returns a person by google account
	 * @throws Exception
	 */
	public Person getByGoogleAccount(String googleAccount, String domainName) throws Exception;

	/**
	 * 
	 * @param registry
	 * @return Returns a person by registry
	 * @throws Exception
	 */
	public Person getByRegistry(String registry) throws Exception;

	/**
	 * 
	 * @param registry
	 * @return Returns person`s groups
	 * @throws Exception
	 */
	public List<Group> getGroups(String registry) throws Exception;

	/**
	 * 
	 * @param registries
	 * @return Returns a list of person by a list of registries
	 * @throws Exception
	 */
	public List<Person> getByRegistry(List<String> registries) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @return Returns all by domain name
	 * @throws Exception
	 */
	public List<Person> getAllByDomainName(String domainName) throws Exception;

	/**
	 * Checks if a person exists in integra database
	 * 
	 * @param registry
	 * @return Returns id if person exist
	 * @throws Exception
	 */
	public Integer isPersonSaved(String registry) throws Exception;
}
