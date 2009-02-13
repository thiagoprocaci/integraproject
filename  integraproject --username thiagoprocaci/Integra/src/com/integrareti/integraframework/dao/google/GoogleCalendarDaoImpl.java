/* Copyright (c) 2006 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.integrareti.integraframework.dao.google;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.acl.AclNamespace;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.calendar.AccessLevelProperty;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.data.calendar.WebContent;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Recurrence;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.dao.integra.DomainDao;

/**
 * Demonstrates basic Calendar Data API operations using the Java client
 * library:
 * 
 * (1) Retrieving the list of all the user's calendars; (2) Retrieving all
 * events on a single calendar; (3) Performing a full-text query on a calendar;
 * (4) Performing a date-range query on a calendar; (5) Creating a
 * single-occurrence event; (6) Creating a recurring event; (7) Creating a quick
 * add event; (8) Creating a web content event; (9) Updating events; (10) Adding
 * reminders and extended properties; (11) Deleting events; (12) Retrieving
 * access control lists (ACLs); (13) Adding users to access control lists; (14)
 * Updating users on access control lists; (15) Removing users from access
 * control lists.
 */
public class GoogleCalendarDaoImpl extends GoogleDomainDao {
	// private constants
	private static final String METAFEED_URL_BASE = "http://www.google.com/calendar/feeds/";
	private static final String ACL_FEED_URL_SUFFIX = "/acl/full";
	private static final String EVENT_FEED_URL_SUFFIX = "/private/full";
	private static final String OWNCALENDARS_FEED_URL_SUFFIX = "/owncalendars/full/";
	private static String METAFEED_URL = null;
	private static String EVENT_FEED_URL = null;
	private static String ACL_FEED_URL = null;
	private CalendarService service;

	public GoogleCalendarDaoImpl(DomainDao domainDao) throws AuthenticationException {
		LOGGER = Logger.getLogger(GoogleCalendarDaoImpl.class.getName());
		this.domainDao = domainDao;
		service = new CalendarService("Interareti-integra-1.0");
	}

	/**
	 * Prints the titles of calendars in the feed specified by the given URL.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @param feedUrl
	 *            The URL of a calendar feed to retrieve.
	 * @throws Exception
	 */
	public void printUserCalendars(String domainName) throws Exception {
		prepareCalendarService(domainName, null);
		URL feedUrl = new URL(METAFEED_URL);
		// Send the request and receive the response:
		CalendarFeed resultFeed = service.getFeed(feedUrl, CalendarFeed.class);
		for (CalendarEntry calEntry : resultFeed.getEntries()) {
			Link link = calEntry.getLink(AclNamespace.LINK_REL_ACCESS_CONTROL_LIST, Link.Type.ATOM);
			if (link != null) {
				AclFeed aclFeed = service.getFeed(new URL(link.getHref()), AclFeed.class);
				for (AclEntry aclEntry : aclFeed.getEntries()) {
					System.out.println("\t\tScope: Type=" + aclEntry.getScope().getType() + " (" + aclEntry.getScope().getValue() + ")");
					System.out.println("\t\tRole: " + aclEntry.getRole().getValue());
				}
				;
			}
		}
	}

	/**
	 * Prints a list of all the user's calendars.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @throws Exception
	 */
	public CalendarFeed getUserCalendars(String domainName, String calendarId) throws Exception {
		prepareCalendarService(domainName, calendarId);
		URL feedUrl = new URL(METAFEED_URL);
		// Send the request and receive the response:
		return service.getFeed(feedUrl, CalendarFeed.class);
	}

	/**
	 * Prints the titles of all events on the calendar specified by
	 * {@code feedUri}.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @throws Exception
	 */
	public CalendarEventFeed getAllEvents(String domainName, String calendarId) throws Exception {
		prepareCalendarService(domainName, calendarId);
		// Set up the URL and the object that will handle the connection:
		URL feedUrl = new URL(EVENT_FEED_URL);
		// Send the request and receive the response:
		return service.getFeed(feedUrl, CalendarEventFeed.class);
	}

	/**
	 * Prints the titles of all events matching a full-text query.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @param query
	 *            The text for which to query.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public CalendarEventFeed getEventByTextQuery(String query, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		prepareCalendarService(domainName, calendarId);
		Query myQuery = new Query(new URL(EVENT_FEED_URL));
		myQuery.setFullTextQuery(query);
		return service.query(myQuery, CalendarEventFeed.class);
	}

	/**
	 * Prints the titles of all events in a specified date/time range.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @param startTime
	 *            Start time (inclusive) of events to print.
	 * @param endTime
	 *            End time (exclusive) of events to print.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public CalendarEventFeed getEventsByDateRangeQuery(DateTime startTime, DateTime endTime, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		prepareCalendarService(domainName, calendarId);
		URL feedUrl = new URL(EVENT_FEED_URL);
		CalendarQuery myQuery = new CalendarQuery(feedUrl);
		myQuery.setMinimumStartTime(startTime);
		myQuery.setMaximumStartTime(endTime);
		// Send the request and receive the response:
		return service.query(myQuery, CalendarEventFeed.class);
	}

	/**
	 * Helper method to create either single-instance or recurring events. For
	 * simplicity, some values that might normally be passed as parameters (such
	 * as author name, email, etc.) are hard-coded.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @param eventTitle
	 *            Title of the event to create.
	 * @param eventContent
	 *            Text content of the event to create.
	 * @param recurData
	 *            Recurrence value for the event, or null for single-instance
	 *            events.
	 * @param isQuickAdd
	 *            True if eventContent should be interpreted as the text of a
	 *            quick add event.
	 * @param wc
	 *            A WebContent object, or null if this is not a web content
	 *            event.
	 * @return The newly-created CalendarEventEntry.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	private CalendarEventEntry createEvent(String eventTitle, String eventContent, Person author, String recurData, boolean isQuickAdd, WebContent wc, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		prepareCalendarService(domainName, calendarId);
		URL postUrl = new URL(EVENT_FEED_URL);
		CalendarEventEntry myEntry = new CalendarEventEntry();
		myEntry.setTitle(new PlainTextConstruct(eventTitle));
		myEntry.setContent(new PlainTextConstruct(eventContent));
		myEntry.setQuickAdd(isQuickAdd);
		myEntry.setWebContent(wc);
		if (author != null) {
			myEntry.getAuthors().add(author);
		}
		// If a recurrence was requested, add it. Otherwise, set the
		// time (the current date and time) and duration (30 minutes)
		// of the event.
		if (recurData == null) {
			Calendar calendar = new GregorianCalendar();
			DateTime startTime = new DateTime(calendar.getTime(), TimeZone.getDefault());
			calendar.add(Calendar.MINUTE, 30);
			DateTime endTime = new DateTime(calendar.getTime(), TimeZone.getDefault());
			When eventTimes = new When();
			eventTimes.setStartTime(startTime);
			eventTimes.setEndTime(endTime);
			myEntry.addTime(eventTimes);
		} else {
			Recurrence recur = new Recurrence();
			recur.setValue(recurData);
			myEntry.setRecurrence(recur);
		}
		// Send the request and receive the response:
		return service.insert(postUrl, myEntry);
	}

	/**
	 * Creates a single-occurrence event.
	 * 
	 * 
	 * @param eventTitle
	 *            Title of the event to create.
	 * @param eventContent
	 *            Text content of the event to create.
	 * @return The newly-created CalendarEventEntry.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public CalendarEventEntry createSingleEvent(String eventTitle, String eventContent, Person author, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		return createEvent(eventTitle, eventContent, author, null, false, null, domainName, calendarId);
	}

	/**
	 * Creates a quick add event.
	 * 
	 * 
	 * @param quickAddContent
	 *            The quick add text, including the event title, date and time.
	 * @return The newly-created CalendarEventEntry.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public CalendarEventEntry createQuickAddEvent(String quickAddContent, Person author, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		return createEvent(null, quickAddContent, author, null, true, null, domainName, calendarId);
	}

	/**
	 * Creates a web content event.
	 * 
	 * @param title
	 *            The title of the web content event.
	 * @param type
	 *            The MIME type of the web content event, e.g. "image/gif"
	 * @param url
	 *            The URL of the content to display in the web content window.
	 * @param icon
	 *            The icon to display in the main Calendar user interface.
	 * @param width
	 *            The width of the web content window.
	 * @param height
	 *            The height of the web content window.
	 * @return The newly-created CalendarEventEntry.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public CalendarEventEntry createWebContentEvent(String title, String type, String url, Person author, String icon, String width, String height, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		WebContent wc = new WebContent();
		wc.setHeight(height);
		wc.setWidth(width);
		wc.setTitle(title);
		wc.setType(type);
		wc.setUrl(url);
		wc.setIcon(icon);
		return createEvent(title, null, author, null, false, wc, domainName, calendarId);
	}

	/**
	 * Creates a new recurring event.
	 * 
	 * @param eventTitle
	 *            Title of the event to create.
	 * @param eventContent
	 *            Text content of the event to create.
	 * @return The newly-created CalendarEventEntry.
	 * @throws Exception
	 */
	public CalendarEventEntry createRecurringEvent(CalendarService service, String eventTitle, String eventContent, Person author, String domainName, String calendarId) throws Exception {
		// Specify a recurring event that occurs every Tuesday from May 1,
		// 2007 through September 4, 2007. Note that we are using iCal (RFC
		// 2445)
		// syntax; see http://www.ietf.org/rfc/rfc2445.txt for more information.
		String recurData = "DTSTART;VALUE=DATE:20070501\r\n" + "DTEND;VALUE=DATE:20070502\r\n" + "RRULE:FREQ=WEEKLY;BYDAY=Tu;UNTIL=20070904\r\n";
		return createEvent(eventTitle, eventContent, author, recurData, false, null, domainName, calendarId);
	}

	/**
	 * Updates the title of an existing calendar event.
	 * 
	 * @param entry
	 *            The event to update.
	 * @param newTitle
	 *            The new title for this event.
	 * @return The updated CalendarEventEntry object.
	 * @throws Exception
	 */
	public CalendarEventEntry updateTitle(CalendarEventEntry entry, String newTitle, String domainName) throws Exception {
		prepareCalendarService(domainName, null);
		entry.setTitle(new PlainTextConstruct(newTitle));
		return entry.update();
	}

	/**
	 * Adds a reminder to a calendar event.
	 * 
	 * @param entry
	 *            The event to update.
	 * @param numMinutes
	 *            Reminder time, in minutes.
	 * @return The updated EventEntry object.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public CalendarEventEntry addReminder(CalendarEventEntry entry, int numMinutes, String domainName) throws ServiceException, IOException, Exception {
		prepareCalendarService(domainName, null);
		Reminder reminder = new Reminder();
		reminder.setMinutes(numMinutes);
		entry.getReminder().add(reminder);
		return entry.update();
	}

	/**
	 * Adds an extended property to a calendar event.
	 * 
	 * @param entry
	 *            The event to update.
	 * @return The updated EventEntry object.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public CalendarEventEntry addExtendedProperty(CalendarEventEntry entry, Map<String, String> properties, String domainName) throws ServiceException, IOException, Exception {
		// Add an extended property "id" with value 1234 to the EventEntry
		// entry.
		// We specify the complete schema URL to avoid namespace collisions with
		// other applications that use the same property name. ->
		// property.setName("http://www.example.com/schemas/2005#mycal.id");
		prepareCalendarService(domainName, null);
		Iterator<String> itNames = properties.keySet().iterator(), itValues = properties.values().iterator();
		ExtendedProperty property;
		while (itNames.hasNext()) {
			property = new ExtendedProperty();
			property.setName(itNames.next());
			property.setValue(itValues.next());
			entry.addExtension(property);
		}
		return entry.update();
	}

	/**
	 * Prints the access control lists for each of the user's calendars.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public AclFeed getAclList(CalendarEntry calEntry, String domainName) throws ServiceException, IOException, Exception {
		prepareCalendarService(domainName, null);
		Link link = calEntry.getLink(AclNamespace.LINK_REL_ACCESS_CONTROL_LIST, Link.Type.ATOM);
		if (link != null) {
			return service.getFeed(new URL(link.getHref()), AclFeed.class);
		}
		return null;
	}

	/**
	 * Adds a user in the read-only role to the calendar's access control list.
	 * Note that this method will not run by default.
	 * 
	 * @param userEmail
	 *            The email address of the user with whom to share the calendar.
	 * @param role
	 *            The access privileges to grant this user.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public AclEntry addAccessControl(String userEmail, AclRole role, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		prepareCalendarService(domainName, calendarId);
		AclEntry entry = new AclEntry();
		entry.setScope(new AclScope(AclScope.Type.USER, userEmail));
		entry.setRole(role);
		URL url = new URL(ACL_FEED_URL);
		return service.insert(url, entry);
	}

	/**
	 * Updates a user to have new access permissions over a calendar. Note that
	 * this method will not run by default.
	 * 
	 * @param userEmail
	 *            The email address of the user to update.
	 * @param newRole
	 *            The new access privileges to grant this user.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public AclEntry updateAccessControl(String userEmail, AclRole newRole, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		prepareCalendarService(domainName, calendarId);
		URL url = new URL(ACL_FEED_URL);
		AclFeed aclFeed = service.getFeed(url, AclFeed.class);
		for (AclEntry aclEntry : aclFeed.getEntries()) {
			if (userEmail.equals(aclEntry.getScope().getValue())) {
				aclEntry.setRole(newRole);
				return aclEntry.update();
			}
		}
		return null;
	}

	/**
	 * Deletes a user from a calendar's access control list, preventing that
	 * user from accessing the calendar. Note that this method will not run by
	 * default.
	 * 
	 * @param userEmail
	 *            The email address of the user to remove from the ACL.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	public AclEntry deleteAccessControl(String userEmail, String domainName, String calendarId) throws ServiceException, IOException, Exception {
		prepareCalendarService(domainName, calendarId);
		URL url = new URL(ACL_FEED_URL);
		AclFeed aclFeed = service.getFeed(url, AclFeed.class);
		for (AclEntry aclEntry : aclFeed.getEntries()) {
			if (userEmail.equals(aclEntry.getScope().getValue())) {
				aclEntry.delete();
				return aclEntry;
			}
		}
		return null;
	}

	/**
	 * Creates a new calendar
	 * 
	 * @param title
	 * @param summary
	 * @param timeZone
	 * @param hidden
	 * @param color
	 * @param location
	 * @return
	 */
	public CalendarEntry createCalendar(String title, String summary, TimeZoneProperty timeZone, HiddenProperty hidden, ColorProperty color, Where location, String domainName) throws Exception {
		prepareCalendarService(domainName, null);
		// Create the calendar
		CalendarEntry calendar = new CalendarEntry();
		calendar.setTitle(new PlainTextConstruct(title));
		calendar.setSummary(new PlainTextConstruct(summary));
		calendar.setTimeZone(timeZone);
		calendar.setHidden(hidden);
		calendar.setColor(color);
		calendar.addLocation(location);
		calendar.setAccessLevel(AccessLevelProperty.OWNER);
		// Insert the calendar
		URL postUrl = null;
		postUrl = new URL("http://www.google.com/calendar/feeds/default/owncalendars/full");
		CalendarEntry cal = service.insert(postUrl, calendar);
		System.out.println("depois------ " + URLDecoder.decode(cal.getId(), "UTF-8"));
		return cal;
	}

	/**
	 * Prepare calendar service - credentials settings
	 */
	private void prepareCalendarService(String domainName, String calendarId) throws AuthenticationException, Exception {
		if (domainName != null) {
			Login login = domainDao.getGoogleDomainAdminLogin(domainName);
			LOGGER.log(Level.INFO, "Credentials " + login.getUserName() + "@" + domainName + " - " + login.getPassword());
			String user = login.getUserName() + "@" + domainName;
			service.setUserCredentials(user, login.getPassword());
			// The URL for the metafeed of the specified user
			METAFEED_URL = METAFEED_URL_BASE + user + OWNCALENDARS_FEED_URL_SUFFIX;
			if (calendarId == null)
				ACL_FEED_URL = METAFEED_URL + ACL_FEED_URL_SUFFIX;
			else
				ACL_FEED_URL = METAFEED_URL_BASE + calendarId;
			System.out.println(ACL_FEED_URL);
			EVENT_FEED_URL = METAFEED_URL_BASE + user + EVENT_FEED_URL_SUFFIX;
		}
	}
}
