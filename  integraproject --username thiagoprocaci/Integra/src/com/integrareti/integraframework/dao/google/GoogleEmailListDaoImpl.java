package com.integrareti.integraframework.dao.google;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.appsforyourdomain.AppsForYourDomainQuery;
import com.google.gdata.client.appsforyourdomain.EmailListRecipientService;
import com.google.gdata.client.appsforyourdomain.EmailListService;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.EmailList;
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListRecipientEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListRecipientFeed;
import com.google.gdata.data.extensions.Who;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.dao.integra.DomainDao;

/**
 * 
 * This class offers methods to manipulates mail lists at google database
 * 
 * @version 1.0
 * @created 09-Aug-2007 13:10:04
 * @author Thiago Baesso Procaci
 */
public class GoogleEmailListDaoImpl extends GoogleDomainDao {

	private static final String EMAIL_LIST_SERVICE_ARG = "gdata-sample-AppsForYourDomain-EmailListService";
	private static final String EMAIL_LIST_RECIPIENT_SERVICE_ARG = "gdata-sample-AppsForYourDomain-EmailListRecipientService";

	// private variables
	private String domainUrlBase;
	private EmailListRecipientService emailListRecipientService;
	private EmailListService emailListService;
	private DomainDao domainDao;
	private String domainName;

	/**
	 * Creates a new GoogleEmailListDaoImpl
	 * 
	 * @param domainDao
	 */
	public GoogleEmailListDaoImpl(DomainDao domainDao) {
		LOGGER = Logger.getLogger(GoogleEmailListDaoImpl.class.getName());
		this.domainDao = domainDao;
		emailListService = new EmailListService(EMAIL_LIST_SERVICE_ARG);
		emailListRecipientService = new EmailListRecipientService(
				EMAIL_LIST_RECIPIENT_SERVICE_ARG);
	}

	/**
	 * Creates an empty email list.
	 * 
	 * @param emailList
	 *            The name of the email list you wish to create.
	 * @return An EmailListEntry object of the newly created email list.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public EmailListEntry createEmailList(String emailList, String domainName)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {
		prepareEmailListService(domainName);
		emailList = emailList.toLowerCase();
		LOGGER.log(Level.INFO, "Creating email list '" + emailList + "'.");
		EmailListEntry entry = new EmailListEntry();
		EmailList emailListExtension = new EmailList();
		emailListExtension.setName(emailList.toLowerCase());
		entry.addExtension(emailListExtension);
		URL insertUrl = new URL(domainUrlBase + "emailList/" + SERVICE_VERSION);
		return emailListService.insert(insertUrl, entry);
	}

	/**
	 * Retrieves all email lists in which the recipient is subscribed. Recipient
	 * can be given as a username or an email address of a hosted user.
	 * 
	 * @param recipient
	 *            The email address or username of the recipient.
	 * @return An EmailListFeed object containing the email lists.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public EmailListFeed retrieveEmailLists(String recipient, String domainName)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {
		prepareEmailListService(domainName);
		recipient = recipient.toLowerCase();
		LOGGER.log(Level.INFO, "Retrieving email lists for '" + recipient
				+ "'.");
		URL feedUrl = new URL(domainUrlBase + "emailList/" + SERVICE_VERSION);
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(feedUrl);
		query.setRecipient(recipient);
		return emailListService.query(query, EmailListFeed.class);
	}

	/**
	 * Retrieves all email lists in domain. This method may be very slow for
	 * domains with a large number of email lists. Any changes to email lists,
	 * including creations and deletions, which are made after this method is
	 * called may or may not be included in the Feed which is returned.
	 * 
	 * @return A EmailListFeed object of the retrieved email lists.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public EmailListFeed retrieveAllEmailLists(String domainName)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {

		prepareEmailListService(domainName);
		LOGGER.log(Level.INFO, "Retrieving all email lists.");
		URL retrieveUrl = new URL(domainUrlBase + "emailList/"
				+ SERVICE_VERSION + "/");
		EmailListFeed allEmailLists = new EmailListFeed();
		EmailListFeed currentPage;
		Link nextLink;
		do {
			currentPage = emailListService.getFeed(retrieveUrl,
					EmailListFeed.class);
			allEmailLists.getEntries().addAll(currentPage.getEntries());
			nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
			if (nextLink != null) {
				retrieveUrl = new URL(nextLink.getHref());
			}
		} while (nextLink != null);
		return allEmailLists;
	}

	/**
	 * Retrieves one page (100) of email lists in domain. Any changes to email
	 * lists, including creations and deletions, which are made after this
	 * method is called may or may not be included in the Feed which is
	 * returned. If the optional startEmailListName parameter is specified, one
	 * page of email lists is returned which have names at or after
	 * startEmailListName as per ASCII value ordering with case-insensitivity. A
	 * value of null or empty string indicates you want results from the
	 * beginning of the list.
	 * 
	 * @param startEmailListName
	 *            The starting point of the page (optional).
	 * @return A EmailListFeed object of the retrieved email lists.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public EmailListFeed retrievePageOfEmailLists(String startEmailListName,
			String domain) throws AppsForYourDomainException, ServiceException,
			IOException, Exception {
		prepareEmailListService(domainName);
		LOGGER.log(Level.INFO, "Retrieving one page of email lists"
				+ (startEmailListName != null ? " starting at "
						+ startEmailListName : "") + ".");

		URL retrieveUrl = new URL(domainUrlBase + "emailList/"
				+ SERVICE_VERSION + "/");
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
		query.setStartEmailListName(startEmailListName);
		return emailListService.query(query, EmailListFeed.class);
	}

	/**
	 * Retrieves an email list.
	 * 
	 * @param emailList
	 *            The name of the email list you want to retrieve.
	 * @return An EmailListEntry object of the retrieved email list.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public EmailListEntry retrieveEmailList(String emailList, String domainName)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {
		prepareEmailListService(domainName);
		emailList = emailList.toLowerCase();
		LOGGER.log(Level.INFO, "Retrieving email list '" + emailList + "'.");

		URL retrieveUrl = new URL(domainUrlBase + "emailList/"
				+ SERVICE_VERSION + "/" + emailList);
		return emailListService.getEntry(retrieveUrl, EmailListEntry.class);
	}

	/**
	 * Deletes an email list.
	 * 
	 * @param emailList
	 *            The email list you with to delete.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public void deleteEmailList(String emailList, String domainName)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {
		prepareEmailListService(domainName);
		emailList = emailList.toLowerCase();
		LOGGER.log(Level.INFO, "Attempting to delete emailList '" + emailList
				+ "'.");

		URL deleteUrl = new URL(domainUrlBase + "emailList/" + SERVICE_VERSION
				+ "/" + emailList);
		emailListService.delete(deleteUrl);
	}

	/**
	 * Retrieves all recipients in an email list. This method may be very slow
	 * for email lists with a large number of recipients. Any changes to the
	 * email list contents, including adding or deleting recipients which are
	 * made after this method is called may or may not be included in the Feed
	 * which is returned.
	 * 
	 * @return An EmailListRecipientFeed object of the retrieved recipients.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public EmailListRecipientFeed retrieveAllRecipients(String emailList,
			String domainName) throws AppsForYourDomainException,
			ServiceException, IOException, Exception {
		prepareEmailListRecipientService(domainName);
		emailList = emailList.toLowerCase();
		LOGGER.log(Level.INFO, "Retrieving all recipients in emailList '"
				+ emailList + "'.");

		URL retrieveUrl = new URL(domainUrlBase + "emailList/"
				+ SERVICE_VERSION + "/" + emailList + "/recipient/");

		EmailListRecipientFeed allRecipients = new EmailListRecipientFeed();
		EmailListRecipientFeed currentPage;
		Link nextLink;
		do {
			currentPage = emailListRecipientService.getFeed(retrieveUrl,
					EmailListRecipientFeed.class);
			allRecipients.getEntries().addAll(currentPage.getEntries());
			nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
			if (nextLink != null) {
				retrieveUrl = new URL(nextLink.getHref());
			}
		} while (nextLink != null);

		return allRecipients;
	}

	/**
	 * Retrieves one page (100) of recipients in an email list. Changes to the
	 * email list recipients including creations and deletions, which are made
	 * after this method is called may or may not be included in the Feed which
	 * is returned. If the optional startRecipient parameter is specified, one
	 * page of recipients is returned which have email addresses at or after
	 * startRecipient as per ASCII value ordering with case-insensitivity. A
	 * value of null or empty string indicates you want results from the
	 * beginning of the list.
	 * 
	 * @param emailList
	 *            The name of the email list for which we are retrieving
	 *            recipients.
	 * @param startRecipient
	 *            The starting point of the page (optional).
	 * @return A EmailListRecipientFeed object of the retrieved recipients.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public EmailListRecipientFeed retrievePageOfRecipients(String emailList,
			String startRecipient, String domainName)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {
		prepareEmailListRecipientService(domainName);
		emailList = emailList.toLowerCase();
		LOGGER.log(Level.INFO, "Retrieving one page of recipients"
				+ (startRecipient != null ? " starting at " + startRecipient
						: "") + ".");

		URL retrieveUrl = new URL(domainUrlBase + "emailList/"
				+ SERVICE_VERSION + "/" + emailList + "/recipient/");
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
		query.setStartRecipient(startRecipient);
		return emailListRecipientService.query(query,
				EmailListRecipientFeed.class);
	}

	/**
	 * Adds an email address to an email list.
	 * 
	 * @param recipientAddress
	 *            The email address you wish to add.
	 * @param emailList
	 *            The email list you wish to modify.
	 * @return The EmailListRecipientEntry of the newly created email list
	 *         recipient.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public EmailListRecipientEntry addRecipientToEmailList(
			String recipientAddress, String emailList, String domainName)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {

		prepareEmailListRecipientService(domainName);
		recipientAddress = recipientAddress.toLowerCase();
		emailList = emailList.toLowerCase();
		LOGGER.log(Level.INFO, "Adding '" + recipientAddress
				+ "' to emailList '" + emailList + "'.");

		EmailListRecipientEntry emailListRecipientEntry = new EmailListRecipientEntry();
		Who who = new Who();
		who.setEmail(recipientAddress);
		emailListRecipientEntry.addExtension(who);

		URL insertUrl = new URL(domainUrlBase + "emailList/" + SERVICE_VERSION
				+ "/" + emailList + "/recipient");
		return emailListRecipientService.insert(insertUrl,
				emailListRecipientEntry);
	}

	/**
	 * Removes an email address from an email list.
	 * 
	 * @param recipientAddress
	 *            The email address you wish to remove.
	 * @param emailList
	 *            The email list you wish to modify.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public void removeRecipientFromEmailList(String recipientAddress,
			String emailList, String domainName)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {
		prepareEmailListRecipientService(domainName);

		recipientAddress = recipientAddress.toLowerCase();
		emailList = emailList.toLowerCase();
		LOGGER.log(Level.INFO, "Removing '" + recipientAddress
				+ "' from emailList '" + emailList + "'.");

		URL deleteUrl = new URL(domainUrlBase + "emailList/" + SERVICE_VERSION
				+ "/" + emailList + "/recipient/" + recipientAddress);
		emailListRecipientService.delete(deleteUrl);
	}

	/**
	 * Prepares the Google App's service with the domainUrlBase, domainName,
	 * admin username (from <code>IntegraDomainServiceInterface</code>) and
	 * admin pass (from <code>IntegraDomainServiceInterface</code>). If it
	 * gets called more than one time for the same domain, does nothing.
	 * 
	 * @param the
	 *            domain name
	 * @throws AuthenticationException
	 */
	private void prepareEmailListService(String domain)
			throws AuthenticationException, Exception {
		domainUrlBase = APPS_FEEDS_URL_BASE + domain + "/";
		LOGGER.log(Level.INFO,
				"Preparing emailListService with domainUrlBase = "
						+ domainUrlBase);
		Login login = domainDao.getGoogleDomainAdminLogin(domain);
		emailListService.setUserCredentials(login.getUserName() + "@" + domain,
				login.getPassword());
	}

	/**
	 * Prepares the Google App's service with the domainUrlBase, domainName,
	 * admin username (from <code>IntegraDomainServiceInterface</code>) and
	 * admin pass (from <code>IntegraDomainServiceInterface</code>). If it
	 * gets called more than one time for the same domain, does nothing.
	 * 
	 * @param the
	 *            domain name
	 * @throws AuthenticationException
	 */
	private void prepareEmailListRecipientService(String domain)
			throws AuthenticationException, Exception {
		this.domainName = domain;
		domainUrlBase = APPS_FEEDS_URL_BASE + domain + "/";
		LOGGER.log(Level.INFO,
				"Preparing emailListRecipientService with domainUrlBase = "
						+ domainUrlBase);
		Login login = domainDao.getGoogleDomainAdminLogin(domain);
		emailListRecipientService.setUserCredentials(login.getUserName() + "@"
				+ domain, login.getPassword());
	}
}