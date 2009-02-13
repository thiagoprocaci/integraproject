package com.integrareti.integraframework.dao.google;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.appsforyourdomain.AppsForYourDomainQuery;
import com.google.gdata.client.appsforyourdomain.NicknameService;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.Nickname;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameFeed;
import com.google.gdata.util.ServiceException;

public class GoogleNicknameDaoImpl {
	private static final Logger LOGGER = Logger
			.getLogger(AppsForYourDomainClient.class.getName());

	private static final String APPS_FEEDS_URL_BASE = "https://www.google.com/a/feeds/";

	private static final String SERVICE_VERSION = "2.0";

	private String domainUrlBase;

	private NicknameService nicknameService;

	/**
	 * Constructs an AppsForYourDomainClient for the given domain using the
	 * given admin credentials.
	 * 
	 * @param adminEmail
	 *            An admin user's email address such as admin@domain.com
	 * @param adminPassword
	 *            The admin's password
	 * @param domain
	 *            The domain to administer
	 */
	public GoogleNicknameDaoImpl(String adminEmail, String adminPassword,
			String domain) throws Exception {

		domainUrlBase = APPS_FEEDS_URL_BASE + domain + "/";

		// Configure all of the different Provisioning services

		nicknameService = new NicknameService(
				"gdata-sample-AppsForYourDomain-NicknameService");
		nicknameService.setUserCredentials(adminEmail, adminPassword);

	}

	/**
	 * Creates a nickname for the username.
	 * 
	 * @param username
	 *            The user for which we want to create a nickname.
	 * @param nickname
	 *            The nickname you wish to create.
	 * @return A NicknameEntry object of the newly created nickname.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameEntry createNickname(String username, String nickname)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Creating nickname '" + nickname
				+ "' for user '" + username + "'.");

		NicknameEntry entry = new NicknameEntry();
		Nickname nicknameExtension = new Nickname();
		nicknameExtension.setName(nickname);
		entry.addExtension(nicknameExtension);

		Login login = new Login();
		login.setUserName(username);
		entry.addExtension(login);

		URL insertUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION);
		return nicknameService.insert(insertUrl, entry);
	}

	/**
	 * Retrieves a nickname.
	 * 
	 * @param nickname
	 *            The nickname you wish to retrieve.
	 * @return A NicknameEntry object of the newly created nickname.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameEntry retrieveNickname(String nickname)
			throws AppsForYourDomainException, ServiceException, IOException {
		LOGGER.log(Level.INFO, "Retrieving nickname '" + nickname + "'.");

		URL retrieveUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION
				+ "/" + nickname);
		return nicknameService.getEntry(retrieveUrl, NicknameEntry.class);
	}

	/**
	 * Retrieves all nicknames for the given username.
	 * 
	 * @param username
	 *            The user for which you want all nicknames.
	 * @return A NicknameFeed object with all the nicknames for the user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameFeed retrieveNicknames(String username)
			throws AppsForYourDomainException, ServiceException, IOException {
		LOGGER.log(Level.INFO, "Retrieving nicknames for user '" + username
				+ "'.");

		URL feedUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION);
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(feedUrl);
		query.setUsername(username);
		return nicknameService.query(query, NicknameFeed.class);
	}

	/**
	 * Retrieves one page (100) of nicknames in domain. Any changes to
	 * nicknames, including creations and deletions, which are made after this
	 * method is called may or may not be included in the Feed which is
	 * returned. If the optional startNickname parameter is specified, one page
	 * of nicknames is returned which have names at or after startNickname as
	 * per ASCII value ordering with case-insensitivity. A value of null or
	 * empty string indicates you want results from the beginning of the list.
	 * 
	 * @param startNickname
	 *            The starting point of the page (optional).
	 * @return A NicknameFeed object of the retrieved nicknames.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameFeed retrievePageOfNicknames(String startNickname)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO,
				"Retrieving one page of nicknames"
						+ (startNickname != null ? " starting at "
								+ startNickname : "") + ".");

		URL retrieveUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION
				+ "/");
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
		query.setStartNickname(startNickname);
		return nicknameService.query(query, NicknameFeed.class);
	}

	/**
	 * Retrieves all nicknames in domain. This method may be very slow for
	 * domains with a large number of nicknames. Any changes to nicknames,
	 * including creations and deletions, which are made after this method is
	 * called may or may not be included in the Feed which is returned.
	 * 
	 * @return A NicknameFeed object of the retrieved nicknames.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameFeed retrieveAllNicknames()
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Retrieving all nicknames.");

		URL retrieveUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION
				+ "/");
		NicknameFeed allNicknames = new NicknameFeed();
		NicknameFeed currentPage;
		Link nextLink;

		do {
			currentPage = nicknameService.getFeed(retrieveUrl,
					NicknameFeed.class);
			allNicknames.getEntries().addAll(currentPage.getEntries());
			nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
			if (nextLink != null) {
				retrieveUrl = new URL(nextLink.getHref());
			}
		} while (nextLink != null);

		return allNicknames;
	}

	/**
	 * Deletes a nickname.
	 * 
	 * @param nickname
	 *            The nickname you wish to delete.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public void deleteNickname(String nickname)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Deleting nickname '" + nickname + "'.");

		URL deleteUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION
				+ "/" + nickname);
		nicknameService.delete(deleteUrl);
	}

}
