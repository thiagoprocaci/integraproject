package com.integrareti.integraframework.service.google;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.dao.google.GoogleUserAccountDaoImpl;
import com.integrareti.integraframework.dao.integra.PersonDao;
import com.integrareti.integraframework.dao.integra.PersonDaoJDBC;
import com.integrareti.integraframework.exceptions.DeleteUserException;
import com.integrareti.integraframework.service.integra.IntegraServiceImpl;
import com.integrareti.integraframework.util.StringUtil;

/**
 * This class offers services to manipulates users
 * 
 * @author Thiago
 */
@Transactional(rollbackFor = { AppsForYourDomainException.class, AuthenticationException.class, Exception.class, IOException.class, ServiceException.class, DataAccessException.class })
public class GoogleUserAccountServiceImpl extends IntegraServiceImpl<Person, Integer> implements GoogleUserAccountServiceInterface {
	private GoogleUserAccountDaoImpl googlePersonDao;
	private PersonDao personDao;
	private PersonDao personDaoJDBC;

	/**
	 * Creates a new GoogleUserAccountServiceImpl
	 * 
	 * @param googlePersonDao
	 * @param personDao
	 */
	public GoogleUserAccountServiceImpl(GoogleUserAccountDaoImpl googlePersonDao, PersonDao personDao) {
		super(personDao);
		this.googlePersonDao = googlePersonDao;
		this.personDao = personDao;
	}

	/**
	 * Deletes a person
	 * 
	 * @throws Exception
	 */
	@Override
	public void delete(Person p) throws AppsForYourDomainException, ServiceException, IOException, Exception {
		Person person = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (person.equals(p))
			throw new DeleteUserException(p);
		if (p.getDomain() == null)
			throw new IllegalStateException("person.domain not specified.");
		// remove from Integra's database
		super.delete(p);
		// remove from Google's database
		googlePersonDao.deleteUser(p.getGoogleAccount(), p.getDomain().getName());
	}

	/**
	 * Saves an person
	 */
	@Override
	public void save(Person person) throws AppsForYourDomainException, ServiceException, IOException, Exception {
		Person googlePerson = person.clone();
		// the user already exists (it's an update)
		if (person.getId() != null)
			update(person);
		else {
			if (person.getDomain() == null)
				throw new IllegalStateException("person.domain not specified.");
			// save in Integra's database
			person.setPassword(StringUtil.SHA1Encrypt(person.getPassword()));
			super.save(person);
			// save in Google's database
			// reencrypt pass
			googlePerson.setPassword(StringUtil.SHA1Encrypt(person.getPassword()));
			googlePersonDao.createUser(googlePerson);
		}
	}

	/**
	 * Updates a person
	 * 
	 * @param p
	 * @return
	 * @throws AppsForYourDomainException
	 * @throws ServiceException
	 * @throws IOException
	 * @throws Exception
	 */
	public Person update(Person p) throws AppsForYourDomainException, ServiceException, IOException, Exception {
		if (p.getDomain() == null)
			throw new IllegalStateException("person.domain not specified.");
		// update in Integra's database
		super.save(p);
		googlePersonDao.updateUser(p.getGoogleAccount(), p.getUserEntry(), p.getDomain().getName());
		return p;
	}

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
	@Override
	public Person getFromGoogleById(Integer objID) throws AppsForYourDomainException, DataAccessException, Exception, ServiceException, IOException {
		Person p = null;
		p = (Person) super.getById(objID);
		// returns google's data
		UserEntry uEntry = null;
		uEntry = getGooglePersonDao().retrieveUser(p.getGoogleAccount(), p.getDomain().getName());
		p.setUserEntry(uEntry);
		return p;
	}

	/**
	 * @throws IOException
	 * @throws ServiceException
	 * @exception Exception
	 * @return returns all users
	 * @throws IOException
	 * @throws ServiceException
	 * @throws AppsForYourDomainException
	 */
	public List<Person> getAll(String domain) throws AppsForYourDomainException, ServiceException, Exception, IOException {
		List<Person> people = null;
		List<UserEntry> gPeople = null;
		people = super.getAll();
		gPeople = getGooglePersonDao().retrieveAllUsers(domain).getEntries();
		for (Person person : people) {
			for (UserEntry entry : gPeople) {
				if (person.getGoogleAccount().equals(entry.getLogin().getUserName()))
					person.setUserEntry(entry);
			}
		}
		return people;
	}

	/**
	 * 
	 * @param googleAccount
	 * @return Returns a person by googleAccount
	 * @throws Exception
	 */
	@Override
	public Person getByGoogleAccount(String googleAccount, String domainName) throws Exception {
		return getPersonDao().getByGoogleAccount(googleAccount, domainName);
	}

	/**
	 * 
	 * @param registry
	 * @return Returns a person by registry
	 * @throws Exception
	 */
	@Override
	public Person getByRegistry(String registry) throws Exception {
		return getPersonDao().getByRegistry(registry);
	}

	/**
	 * 
	 * @param registries
	 * @return Returns a list of person by a list of registries
	 * @throws Exception
	 */
	@Override
	public List<Person> getByRegistry(List<String> registries) throws Exception {
		return personDao.getByRegistry(registries);
	}

	/**
	 * Checks if a person exists in integra database - using JBDC to improve
	 * performance
	 * 
	 * @param registry
	 * @return Returns id if person exist
	 * @throws Exception
	 */
	@Override
	public Integer isPersonSaved(String registry, boolean stillOpenConnection) throws Exception {
		if (!isConnectionOpenJDBC())
			openConnection();
		Integer id = personDaoJDBC.isPersonSaved(registry);
		if (!stillOpenConnection)
			closeConnection();
		return id;
	}

	/**
	 * Checks if people exist in integra database by registry.
	 * 
	 * @param registries
	 * @return Returns a list of saved people
	 * @throws Exception
	 */
	@Override
	public List<Person> arePeopleSaved(List<String> registries) throws Exception {
		if (registries == null || registries.isEmpty())
			return new ArrayList<Person>(0);
		int aux = registries.size();
		boolean stillOpenConnection = true;
		List<Person> list = new ArrayList<Person>();
		for (String string : registries) {
			if (aux == 1)
				stillOpenConnection = false;
			Integer id = this.isPersonSaved(string, stillOpenConnection);
			if (id != null)
				list.add(this.getById(id));
			aux--;
		}
		return list;
	}

	/**
	 * 
	 * @param registry
	 * @return Returns person`s groups
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroups(String registry) throws Exception {
		return personDao.getGroups(registry);
	}

	/**
	 * Open integra database connection -using JDBC
	 * 
	 * @throws Exception
	 */
	@Override
	public void openConnection() throws Exception {
		((PersonDaoJDBC) personDaoJDBC).openConnection();
	}

	/**
	 * Close integra database connection - -using JDBC
	 * 
	 * @throws Exception
	 */
	@Override
	public void closeConnection() throws Exception {
		((PersonDaoJDBC) personDaoJDBC).closeConnection();
	}

	/**
	 * Returns googlePersonDao
	 */
	public GoogleUserAccountDaoImpl getGooglePersonDao() {
		return googlePersonDao;
	}

	/**
	 * Sets googlePersonDao
	 * 
	 * @param personDao
	 */
	public void setGooglePersonDao(GoogleUserAccountDaoImpl personDao) {
		this.googlePersonDao = personDao;
	}

	/**
	 * 
	 * @return Returns personDao
	 */
	public PersonDao getPersonDao() {
		return personDao;
	}

	/**
	 * Sets personDao
	 * 
	 * @param personDao
	 */
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	/**
	 * 
	 * @return personDaoJDBC
	 */
	public PersonDao getPersonDaoJDBC() {
		return personDaoJDBC;
	}

	/**
	 * Sets personDaoJDBC
	 * 
	 * @param personDaoJDBC
	 */
	public void setPersonDaoJDBC(PersonDao personDaoJDBC) {
		this.personDaoJDBC = personDaoJDBC;
	}

	/**
	 * Checks if siga database connection is open
	 * 
	 * @return True if open. False if close
	 * @throws SQLException
	 */
	private boolean isConnectionOpenJDBC() throws SQLException {
		return ((PersonDaoJDBC) personDaoJDBC).isConnectionOpen();
	}
}
