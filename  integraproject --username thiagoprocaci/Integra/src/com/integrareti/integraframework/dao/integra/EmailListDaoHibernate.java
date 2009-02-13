package com.integrareti.integraframework.dao.integra;

import java.util.List;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Person;

/**
 * This class offers methods to manipulates email lists at integra database
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class EmailListDaoHibernate extends
		GenericDaoHibernate<EmailList, Integer> implements EmailListDao {

	/**
	 * 
	 * @param emailListName
	 * @return Returns email lists by name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmailList> getEmailListByName(String emailListName)
			throws Exception {
		return getHibernateTemplate().find(
				"SELECT e FROM EmailList e WHERE e.name=?", emailListName);
	}

	/**
	 * 
	 * @param emailListName
	 * @param domainName
	 * @return Returns a email list by name and domain
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EmailList getEmailListByNameAndDomain(String emailListName,
			String domainName) throws Exception {
		List<EmailList> list = (List<EmailList>) getHibernateTemplate()
				.find(
						"SELECT e FROM EmailList e WHERE e.name = ? AND e.domain.name = ?",
						new String[] { emailListName, domainName });
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns the email lists of a domain
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmailList> getEmailListsByDomainName(String domainName)
			throws Exception {
		return getHibernateTemplate()
				.find("SELECT e FROM EmailList e WHERE e.domain.name = ?",
						domainName);
	}

	/**
	 * Checks if a email list contains a person
	 * 
	 * @param person
	 * @param emailList
	 * @return true if contains. False if doesn't contain
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean containsPerson(Person person, EmailList emailList)
			throws Exception {
		List<Integer> list = getHibernateTemplate()
				.find(
						"SELECT p.id FROM Person p, EmailList e WHERE p in elements(e.recipients) AND p=? AND e=?",
						new Object[] { person, emailList });
		if (!list.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param emailList
	 * @return Returns the recipients of the email list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getRecipients(EmailList emailList)
			throws Exception {
		List<Person> list = getHibernateTemplate().find(
				"SELECT e.recipients FROM EmailList e WHERE e=?", emailList);
		return list;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns email lists by domain name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmailList> getAllByDomainName(String domainName) throws Exception {		
		return getHibernateTemplate().find("FROM EmailList el WHERE el.domain.name = ?", domainName);
	}

}
