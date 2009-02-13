package com.integrareti.integraframework.business;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.extensions.EventEntry;

/**
 * Describes an entity that represents an event
 * 
 * @version 1.0
 * @created 06-Aug-2007 18:07:58
 * @author Thiago Athouguia Gama
 */
public class Event{

	private EventEntry eventEntry;

	/**
	 * Creates a new event
	 */
	public Event() {
		eventEntry = new EventEntry();
	}

	/**
	 * @return Returns the event title
	 */
	public String getTitle() {
		return getEventEntry().getTitle().getPlainText();
	}

	/**
	 * Sets the event title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		getEventEntry().setTitle(new PlainTextConstruct(title));
	}

	/**
	 * @return Returns the eventEntry
	 */
	public EventEntry getEventEntry() {
		return eventEntry;
	}

	/**
	 * Sets the eventEntry
	 * 
	 * @param eventEntry
	 */
	public void setEventEntry(EventEntry eventEntry) {
		this.eventEntry = eventEntry;
	}

}