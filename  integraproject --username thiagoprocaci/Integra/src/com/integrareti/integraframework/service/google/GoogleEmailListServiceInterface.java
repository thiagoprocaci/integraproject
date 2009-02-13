package com.integrareti.integraframework.service.google;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.integra.IntegraServiceInterface;

/**
 * This interface offers method to manupulates email lists at google and integra
 * database
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public interface GoogleEmailListServiceInterface extends IntegraServiceInterface<EmailList, Integer> {
	/**
	 * 
	 * @param name
	 * @param domainName
	 * @return Returns an email list by name and domain
	 * @throws DataAccessException
	 */
	public EmailList getEmailListByNameAndDomain(String name, String domainName) throws Exception;

	/**
	 * Adds a new recipient to email list
	 * 
	 * @param emailList
	 * @param recipient
	 * @throws ServiceException
	 */
	public Person addRecipient(EmailList emailList, Person recipient) throws Exception;

	/**
	 * Adds a set of recipient to email list
	 * 
	 * @param emailList
	 * @param recipient
	 * @return nod added <code>Person</code>s
	 */
	public List<Person> addRecipients(EmailList emailList, Set<Person> recipients) throws Exception;

	/**
	 * Removes recipients from email list
	 * 
	 * @param emailList
	 * @param recipients
	 */
	public List<Person> removeRecipients(EmailList emailList, List<Person> recipients) throws Exception;

	/**
	 * Removes recipients from email list
	 * 
	 * @param emailList
	 * @param recipients
	 */
	public List<Person> removeRecipients(EmailList emailList, Set<Person> recipients) throws Exception;

	/**
	 * Removes a recipients from email list
	 * 
	 * @param emailList
	 * @param recipient
	 */
	public Person removeRecipient(EmailList emailList, Person recipient) throws Exception;

	/**
	 * Saves an email list only at google
	 * 
	 * @param emailList
	 * @throws ServiceException
	 */
	public void saveEmailListOnlyAtGoogle(EmailList emailList) throws Exception;

	/**
	 * Saves an email list only at integra database
	 * 
	 * @param emailList
	 * @throws DataAccessException
	 */
	public void saveOnlyAtIntegra(EmailList emailList) throws Exception;

	/**
	 * Deletes an email list only at integra database
	 * 
	 * @param emailList
	 * @throws Exception
	 */
	public void deleteEmailListOnlyAtIntegra(EmailList emailList) throws Exception;

	/**
	 * Deletes an email list only at google
	 * 
	 * @param emailList
	 * @throws Exception
	 */
	public void deleteEmailListOnlyAtGoogle(EmailList emailList) throws Exception;

	/**
	 * Checks if an email list has a valid name at its domain
	 * 
	 * @param emailList
	 * @return true if is valid
	 * @throws Exception
	 */
	public boolean validateEmailListName(EmailList emailList) throws Exception;
}
