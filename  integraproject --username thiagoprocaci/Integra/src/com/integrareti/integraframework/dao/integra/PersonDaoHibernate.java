package com.integrareti.integraframework.dao.integra;

import java.util.GregorianCalendar;
import java.util.List;

import com.integrareti.integraframework.authentication.UserGroup;
import com.integrareti.integraframework.business.DeletedGoogleUser;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;

/**
 * This class offers methods to manipulates person at integra database - using
 * hibernate
 * 
 * @author Thiago
 * 
 */
public class PersonDaoHibernate extends GenericDaoHibernate<Person, Integer>
		implements PersonDao {

	DeletedGoogleUserDao deletedGoogleUserDao;

	/**
	 * Deletes a person
	 */
	@Override
	public void delete(Person row) throws Exception {
		DeletedGoogleUser deletedGoogleUser = new DeletedGoogleUser();
		deletedGoogleUser.setDomain(row.getDomain());
		deletedGoogleUser.setDeletedGoogleAccount(row.getGoogleAccount());
		deletedGoogleUser.setExclusionDate(new GregorianCalendar());
		super.delete(row);
		deletedGoogleUserDao.save(deletedGoogleUser);
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns the people from a domain
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getGoogleDomainAdmins(String d) throws Exception {
		return getHibernateTemplate().find(
				"SELECT p " + "FROM " + "Person  p," + "UserGroup  ug "
						+ " WHERE " + " ug in elements(p.userGroups) AND "
						+ " ug.name = '" + UserGroup.GOOGLE_ADMIN_GROUP
						+ "' AND " + " p.domain.name = ?", d);

	}

	/**
	 * 
	 * @param googleAccount
	 * @return Returns a person by google account
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Person getByGoogleAccount(String googleAccount, String domainName)
			throws Exception {
		List<Person> list = getHibernateTemplate().find(
				"FROM Person p WHERE p.googleAccount = ? and p.domain.name = ?",
				new String[] { googleAccount, domainName });
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @return Returns a person by registry
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Person getByRegistry(String registry) throws Exception {
		List<Person> list = (List<Person>) getHibernateTemplate().find(
				"FROM Person p WHERE p.registry = ?", registry);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * Saves a person
	 * 
	 * @throws Exception
	 */
	@Override
	public void save(Person row) throws Exception {
		super.save(row);
	}

	/**
	 * 
	 * @param registries
	 * @return Returns a list of person by a list of registries
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getByRegistry(List<String> registries) throws Exception {
		StringBuffer query = new StringBuffer("SELECT p FROM Person p WHERE ");
		int count = 0;
		for (String string : registries) {
			query.append("p.registry = '" + string.trim() + "'");
			if (count != registries.size() - 1) {
				query.append(" OR ");
			}
			count++;
		}
		return getHibernateTemplate().find(query.toString());
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns all by domain name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getAllByDomainName(String domainName) throws Exception {
		return getHibernateTemplate().find(
				"FROM Person p WHERE p.domain.name = ?", domainName);
	}

	/**
	 * Checks if a person exists in integra database
	 * 
	 * @param registry
	 * @return Returns id if person exist
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer isPersonSaved(String registry) throws Exception {
		List<Integer> list = (List<Integer>) getHibernateTemplate().find(
				"select p.id from Person p where p.registry = ?", registry);
		if (!list.isEmpty())
			return list.get(0);
		return null;
	}

	/**
	 * 
	 * @param person
	 * @return Returns person`s groups
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroups(String registry) throws Exception {
		return getHibernateTemplate()
				.find(
						"select g from Group g, Person p where p in elements(g.participants) and p.registry = ?",
						registry);
	}

	/**
	 * 
	 * @return Returns deletedGoogleUserDao
	 */
	public DeletedGoogleUserDao getDeletedGoogleUserDao() {
		return deletedGoogleUserDao;
	}

	/**
	 * Sets deletedGoogleUserDao
	 * 
	 * @param deletedGoogleUserDao
	 */
	public void setDeletedGoogleUserDao(
			DeletedGoogleUserDao deletedGoogleUserDao) {
		this.deletedGoogleUserDao = deletedGoogleUserDao;
	}

}
