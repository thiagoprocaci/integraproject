package com.integrareti.integraframework.dao.integra;

import java.util.List;

import org.hibernate.Session;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;

/**
 * Class adapter of personDao
 * 
 * @author Thiago
 * 
 */
public class PersonDaoJDBCAdapter implements PersonDao {
	private static final String MSG = "Not implementedn here - see PersonDaoHibernate";

	/**
	 * 
	 * @param domainName
	 * @return Returns all by domain name
	 * @throws Exception
	 */
	@Override
	public List<Person> getAllByDomainName(String domainName) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param googleAccount
	 * @return Returns a person by google account
	 * @throws Exception
	 */
	@Override
	public Person getByGoogleAccount(String googleAccount, String domainName) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param registry
	 * @return Returns a person by registry
	 * @throws Exception
	 */
	@Override
	public Person getByRegistry(String registry) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param registries
	 * @return Returns a list of person by a list of registries
	 * @throws Exception
	 */
	@Override
	public List<Person> getByRegistry(List<String> registries) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns the people from a domain
	 * @throws Exception
	 */
	@Override
	public List<Person> getGoogleDomainAdmins(String domainName) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * Checks if a person exists in integra database
	 * 
	 * @param registry
	 * @return Returns id if person exist
	 * @throws Exception
	 */
	@Override
	public Integer isPersonSaved(String registry) throws Exception {
		return null;
	}

	/**
	 * Deletes a person
	 */
	@Override
	public void delete(Person row) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * @return Returns all people
	 */
	@Override
	public List<Person> getAll() throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * @return Returns a person by id
	 */
	@Override
	public Person getById(Integer id) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * @return Returns hibernate session
	 */
	@Override
	public Session getHibernateSession() {
		return null;
	}

	/**
	 * Saves a person
	 */
	@Override
	public void save(Person row) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param person
	 * @return Returns person`s groups
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroups(String registry) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * close JDBC session
	 */
	@Override
	public void closeSession() {
		throw new RuntimeException(MSG);
	}

	/**
	 * Open JDBC session
	 */
	@Override
	public void openSession() {
		throw new RuntimeException(MSG);
	}
}
