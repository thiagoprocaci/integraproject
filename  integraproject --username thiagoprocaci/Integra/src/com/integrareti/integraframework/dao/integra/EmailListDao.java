package com.integrareti.integraframework.dao.integra;

import java.util.List;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Person;

/**
 * This interface offers methods to manipulates email lists at integra database
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public interface EmailListDao extends GenericDao<EmailList, Integer> {
	/**
	 * 
	 * @param domainName
	 * @return Returns the email lists of a domain
	 * @throws Exception
	 */
	public List<EmailList> getEmailListsByDomainName(String domainName) throws Exception;

	/**
	 * 
	 * @param emailListName
	 * @return Returns email lists by name
	 * @throws Exception
	 */
	public List<EmailList> getEmailListByName(String emailListName) throws Exception;

	/**
	 * 
	 * @param emailListName
	 * @param domainName
	 * @return Returns a email list by name and domain
	 * @throws Exception
	 */
	public EmailList getEmailListByNameAndDomain(String emailListName, String domainName) throws Exception;

	/**
	 * Checks if a email list contains a person
	 * 
	 * @param person
	 * @param emailList
	 * @return true if contains. False if don't contain
	 * @throws Exception
	 */
	public boolean containsPerson(Person person, EmailList emailList) throws Exception;

	/**
	 * 
	 * @param emailList
	 * @return Returns the recipients of the email list
	 * @throws Exception
	 */
	public List<Person> getRecipients(EmailList emailList) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @return Returns email lists by domain name
	 * @throws Exception
	 */
	public List<EmailList> getAllByDomainName(String domainName) throws Exception;
}
