package com.integrareti.integraframework.service.google;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.dao.google.GoogleEmailListDaoImpl;
import com.integrareti.integraframework.dao.integra.EmailListDao;
import com.integrareti.integraframework.exceptions.EmailListException;
import com.integrareti.integraframework.service.integra.IntegraServiceImpl;

/**
 * This class offers method to manupulates email lists at google and integra
 * database
 * 
 * @author Thiago
 * 
 */
@Transactional
public class GoogleEmailListServiceImpl extends IntegraServiceImpl<EmailList, Integer> implements GoogleEmailListServiceInterface {
	private EmailListDao emailListDao;
	private GoogleEmailListDaoImpl googleEmailListDao;

	/**
	 * Creates a new GoogleEmailListServiceImpl
	 * 
	 * @param emailListDao
	 */
	public GoogleEmailListServiceImpl(EmailListDao emailListDao, GoogleEmailListDaoImpl googleEmailListDao) {
		super(emailListDao);
		this.emailListDao = emailListDao;
		this.googleEmailListDao = googleEmailListDao;
	}

	/**
	 * 
	 * @param name
	 * @param domainName
	 * @return Returns an email list by name and domain
	 * @throws Exception
	 */
	@Override
	public EmailList getEmailListByNameAndDomain(String name, String domainName) throws Exception {
		EmailList emailList = emailListDao.getEmailListByNameAndDomain(name, domainName);
		return emailList;
	}

	/**
	 * Deletes an email list
	 * 
	 * @param emailList
	 * @throws Exception
	 * @throws EmailListException
	 */
	@Override
	public void delete(EmailList emailList) throws Exception {
		// deleting the emailList at integra database
		super.delete(emailList);
		// deleting the emailList at google
		googleEmailListDao.deleteEmailList(emailList.getName(), emailList.getDomain().getName());
	}

	/**
	 * Removes recipients from email list
	 * 
	 * @param emailList
	 * @param recipients
	 * @throws Exception
	 */
	@Override
	public List<Person> removeRecipients(EmailList emailList, List<Person> recipients) throws Exception {
		List<Person> notRemovedOnes = new ArrayList<Person>();
		for (Person person : recipients) {
			Person p2 = removeRecipient(emailList, person);
			if (p2 != null)
				notRemovedOnes.add(p2);
		}
		if (notRemovedOnes.isEmpty())
			return null;
		return notRemovedOnes;
	}

	/**
	 * Removes recipients from email list
	 * 
	 * @param emailList
	 * @param recipients
	 * @throws Exception
	 */
	@Override
	public List<Person> removeRecipients(EmailList emailList, Set<Person> recipients) throws Exception {
		List<Person> notRemovedOnes = new ArrayList<Person>();
		for (Person person : recipients) {
			Person p2 = removeRecipient(emailList, person);
			if (p2 != null)
				notRemovedOnes.add(p2);
		}
		if (notRemovedOnes.isEmpty())
			return null;
		return notRemovedOnes;
	}

	/**
	 * Removes a recipients from email list
	 * 
	 * @param emailList
	 * @param recipient
	 * @return null if person was removed, otherwise the person not removed
	 * @throws Exception
	 */
	@Override
	public Person removeRecipient(EmailList emailList, Person recipient) throws Exception {
		emailList.removeRecipient(recipient);
		emailListDao.getHibernateSession().merge(emailList);
		googleEmailListDao.removeRecipientFromEmailList(recipient.getEmail(), emailList.getName(), emailList.getDomain().getName());
		return recipient;
	}

	/**
	 * Adds a new recipient to email list
	 * 
	 * @param emailList
	 * @param recipient
	 * @throws Exception
	 */
	@Override
	public Person addRecipient(EmailList emailList, Person recipient) throws Exception {
		emailList.addRecipient(recipient);
		emailListDao.getHibernateSession().merge(emailList);
		googleEmailListDao.addRecipientToEmailList(recipient.getEmail(), emailList.getName(), emailList.getDomain().getName());
		return recipient;
	}

	/**
	 * Adds a set of recipient to email list
	 * 
	 * @param emailList
	 * @param recipient
	 * @return nod added <code>Person</code>s
	 * @throws Exception
	 */
	@Override
	public List<Person> addRecipients(EmailList emailList, Set<Person> recipients) throws Exception {
		List<Person> notAddedOnes = new ArrayList<Person>();
		for (Person person : recipients) {
			Person p2 = addRecipient(emailList, person);
			if (p2 != null)
				notAddedOnes.add(p2);
		}
		if (notAddedOnes.isEmpty())
			return null;
		return notAddedOnes;
	}

	/**
	 * 
	 * Saves an email list
	 * 
	 * @param emailList
	 * @throws IOException
	 * @throws ServiceException
	 * @throws AppsForYourDomainException
	 * @throws EmailListException
	 * @throws Exception
	 * @throws Exception
	 */
	@Override
	public void save(EmailList emailList) throws Exception {
		boolean create = false;
		if (emailList.getId() == null)
			create = true;
		// Checking if the email list name is valid
		if (!validateEmailListName(emailList))
			throw new EmailListException(emailList.getName() + ": Nome de lista inválido. Lista de e-mail já definida para domínio " + emailList.getDomain().getName());
		// saving the emailList at integra database
		super.save(emailList);
		if (create) // creating the emailList at google
			googleEmailListDao.createEmailList(emailList.getName(), emailList.getDomain().getName());
	}

	/**
	 * Saves an email list only at google
	 * 
	 * @param emailList
	 * @return saved emailList
	 * @throws Exception
	 */
	@Override
	public void saveEmailListOnlyAtGoogle(EmailList emailList) throws Exception {
		googleEmailListDao.createEmailList(emailList.getName(), emailList.getDomain().getName());
	}

	/**
	 * Saves an email list only at integra database
	 * 
	 * @param emailList
	 * @return saved emailList
	 * @throws Exception
	 */
	@Override
	public void saveOnlyAtIntegra(EmailList emailList) throws Exception {
		emailListDao.save(emailList);
	}

	/**
	 * Checks if an email list has a valid name at its domain
	 * 
	 * @param emailList
	 * @return true if is valid
	 * @throws Exception
	 */
	@Override
	public boolean validateEmailListName(EmailList emailList) throws Exception {
		// checking if exist
		if (emailList.getId() == null) {
			// Checking if the email list name is valid
			EmailList emailListAux = emailListDao.getEmailListByNameAndDomain(emailList.getName(), emailList.getDomain().getName());
			if (emailListAux != null)
				return false;
		}
		return true;
	}

	/**
	 * Deletes an email list only at google
	 * 
	 * @param emailList
	 * @throws Exception
	 */
	@Override
	public void deleteEmailListOnlyAtGoogle(EmailList emailList) throws Exception {
		// deleting the emailList at integra database
		super.delete(emailList);
	}

	/**
	 * Deletes an email list only at integra database
	 * 
	 * @param emailList
	 * @throws Exception
	 */
	@Override
	public void deleteEmailListOnlyAtIntegra(EmailList emailList) throws Exception {
		// deleting the emailList at google
		googleEmailListDao.deleteEmailList(emailList.getName(), emailList.getDomain().getName());
	}
}
