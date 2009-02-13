package com.integrareti.integraframework.dao.google;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.appsforyourdomain.AppsForYourDomainQuery;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.Name;
import com.google.gdata.data.appsforyourdomain.Quota;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.dao.integra.DomainDao;

/**
 * This class manipulates users at google.
 * 
 * @version 1.0
 * @created 08-Aug-2007 17:01:40
 * @author Thiago Gama
 */
public class GoogleUserAccountDaoImpl extends GoogleDomainDao {

	private static final String USER_SERVICE_ARG = "gdata-sample-AppsForYourDomain-UserService";

	private String domainUrlBase;
	private String domainName;
	private UserService userService;

	/**
	 * Creates a new GoogleUserAccountDaoImpl
	 * 
	 * @param dao
	 */
	public GoogleUserAccountDaoImpl(DomainDao dao) {
		LOGGER = Logger.getLogger(GoogleUserAccountDaoImpl.class.getName());
		domainDao = dao;
		// Configure all of the different Provisioning services
		userService = new UserService(USER_SERVICE_ARG);
	}

	/**
	 * Creates a new user with a person
	 * 
	 * @param p
	 * @return
	 * @throws AppsForYourDomainException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public UserEntry createUser(Person p) throws AppsForYourDomainException,
			ServiceException, IOException, Exception {

		// prepares the google apps service
		prepareUserService(p.getDomain().getName());
		LOGGER.log(Level.INFO, "Creating user '"
				+ p.getGoogleAccount()
				+ "'. Given Name: '"
				+ p.getGivenName()
				+ "' Family Name: '"
				+ p.getFamilyName()
				+ (p.getQuotaLimitInMB() != null ? "' Quota Limit: '"
						+ p.getQuotaLimitInMB() + "'." : "."));

		URL insertUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION);
		return userService.insert(insertUrl, p.getUserEntry());
	}

	/**
	 * Creates a new user with an email account.
	 * 
	 * @param username
	 *            The username of the new user.
	 * @param givenName
	 *            The given name for the new user.
	 * @param familyName
	 *            the family name for the new user.
	 * @param password
	 *            The password for the new user.
	 * @return A UserEntry object of the newly created user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry createUser(String username, String givenName,
			String familyName, String password, String domain)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {
		return createUser(username, givenName, familyName, password, domain,
				null);
	}

	/**
	 * Creates a new user with an email account.
	 * 
	 * @param username
	 *            The username of the new user.
	 * @param givenName
	 *            The given name for the new user.
	 * @param familyName
	 *            the family name for the new user.
	 * @param password
	 *            The password for the new user.
	 * @param quotaLimitInMb
	 *            User's quota limit in megabytes. This field is only used for
	 *            domains with custom quota limits.
	 * @return A UserEntry object of the newly created user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry createUser(String username, String givenName,
			String familyName, String password, String domain,
			Integer quotaLimitInMb) throws AppsForYourDomainException,
			ServiceException, IOException, Exception {
		prepareUserService(domain);
		LOGGER.log(Level.INFO, "Creating user '"
				+ username
				+ "'. Given Name: '"
				+ givenName
				+ "' Family Name: '"
				+ familyName
				+ (quotaLimitInMb != null ? "' Quota Limit: '" + quotaLimitInMb
						+ "'." : "."));
		UserEntry entry = new UserEntry();
		Login login = new Login();
		login.setUserName(username);
		login.setPassword(password);
		entry.addExtension(login);
		Name name = new Name();
		name.setGivenName(givenName);
		name.setFamilyName(familyName);
		entry.addExtension(name);
		if (quotaLimitInMb != null) {
			Quota quota = new Quota();
			quota.setLimit(quotaLimitInMb);
			entry.addExtension(quota);
		}
		URL insertUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION);
		return userService.insert(insertUrl, entry);
	}

	/**
	 * Retrieves a user.
	 * 
	 * @param username
	 *            The user you wish to retrieve.
	 * @return A UserEntry object of the retrieved user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry retrieveUser(String username, String domain)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {
		LOGGER.log(Level.INFO, "Retrieving user '" + username + "'.");
		prepareUserService(domain);
		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		return userService.getEntry(retrieveUrl, UserEntry.class);
	}

	/**
	 * Retrieves all users in domain. This method may be very slow for domains
	 * with a large number of users. Any changes to users, including creations
	 * and deletions, which are made after this method is called may or may not
	 * be included in the Feed which is returned.
	 * 
	 * @return A UserFeed object of the retrieved users.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserFeed retrieveAllUsers(String domain)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {

		LOGGER.log(Level.INFO, "Retrieving all users.");
		prepareUserService(domain);
		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/");
		UserFeed allUsers = new UserFeed();
		UserFeed currentPage;
		Link nextLink;

		do {
			currentPage = userService.getFeed(retrieveUrl, UserFeed.class);
			allUsers.getEntries().addAll(currentPage.getEntries());
			nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
			if (nextLink != null) {
				retrieveUrl = new URL(nextLink.getHref());
			}
		} while (nextLink != null);
		return allUsers;
	}

	/**
	 * Retrieves one page (100) of users in domain. Any changes to users,
	 * including creations and deletions, which are made after this method is
	 * called may or may not be included in the Feed which is returned. If the
	 * optional startUsername parameter is specified, one page of users is
	 * returned which have usernames at or after the startUsername as per ASCII
	 * value ordering with case-insensitivity. A value of null or empty string
	 * indicates you want results from the beginning of the list.
	 * 
	 * @param startUsername
	 *            The starting point of the page (optional).
	 * @return A UserFeed object of the retrieved users.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserFeed retrievePageOfUsers(String startUsername, String domain)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {

		LOGGER.log(Level.INFO,
				"Retrieving one page of users"
						+ (startUsername != null ? " starting at "
								+ startUsername : "") + ".");
		prepareUserService(domain);
		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/");
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
		query.setStartUsername(startUsername);
		return userService.query(query, UserFeed.class);
	}

	/**
	 * Updates a user.
	 * 
	 * @param username
	 *            The user to update.
	 * @param userEntry
	 *            The updated UserEntry for the user.
	 * @return A UserEntry object of the newly updated user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry updateUser(String username, UserEntry userEntry,
			String domain) throws AppsForYourDomainException, ServiceException,
			IOException, Exception {
		LOGGER.log(Level.INFO, "Updating user '" + username + "'.");
		prepareUserService(domain);
		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
	}

	/**
	 * Deletes a user.
	 * 
	 * @param username
	 *            The user you wish to delete.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public void deleteUser(String username, String domain)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {

		LOGGER.log(Level.INFO, "Deleting user '" + username + "'.");
		prepareUserService(domain);
		URL deleteUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		userService.delete(deleteUrl);
	}

	/**
	 * Suspends a user. Note that executing this method for a user who is
	 * already suspended has no effect.
	 * 
	 * @param username
	 *            The user you wish to suspend.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry suspendUser(String username, String domain)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {

		LOGGER.log(Level.INFO, "Suspending user '" + username + "'.");
		prepareUserService(domain);

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		UserEntry userEntry = userService
				.getEntry(retrieveUrl, UserEntry.class);
		userEntry.getLogin().setSuspended(true);

		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
	}

	/**
	 * Restores a user. Note that executing this method for a user who is not
	 * suspended has no effect.
	 * 
	 * @param username
	 *            The user you wish to restore.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry restoreUser(String username, String domain)
			throws AppsForYourDomainException, ServiceException, IOException,
			Exception {

		LOGGER.log(Level.INFO, "Restoring user '" + username + "'.");
		prepareUserService(domain);

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		UserEntry userEntry = userService
				.getEntry(retrieveUrl, UserEntry.class);
		userEntry.getLogin().setSuspended(false);

		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
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
	private void prepareUserService(String domain)
			throws AuthenticationException, Exception {
		if (!domain.equals(this.domainName)) {
			this.domainName = domain;
			domainUrlBase = APPS_FEEDS_URL_BASE + domain + "/";
			LOGGER.log(Level.INFO,
					"Preparing userService with domainUrlBase = "
							+ domainUrlBase);

			Login login = domainDao.getGoogleDomainAdminLogin(domain);
			LOGGER.log(Level.INFO, "Credentials " + login.getUserName() + "@"
					+ domain + " - " + login.getPassword());

			userService.setUserCredentials(login.getUserName() + "@" + domain,
					login.getPassword());
		}

	}

}