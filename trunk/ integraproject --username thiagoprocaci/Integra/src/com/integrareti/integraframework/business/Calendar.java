package com.integrareti.integraframework.business;

import java.io.Serializable;
import java.util.List;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.data.extensions.Where;

/**
 * Describes an entity that represents a calendar
 * 
 * @version 1.0
 * @created 06-Aug-2007 18:07:56
 * @author Thiago Athouguia Gama
 */
@SuppressWarnings("serial")
public class Calendar implements Identifiable<Integer>, Serializable {
	private Group group;
	private Integer id;
	private CalendarEntry calendarEntry;

	/**
	 * Creates a new Calendar
	 */
	public Calendar() {
		calendarEntry = new CalendarEntry();
	}

	/**
	 * @return Returns the title of the calendar
	 */
	public String getTitle() {
		TextConstruct tc = getCalendarEntry().getTitle();
		return tc.getPlainText();
	}

	/**
	 * Sets the title of the calendar
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		getCalendarEntry().setTitle(new PlainTextConstruct(title));
	}

	/**
	 * @return Returns the summary of the calendar
	 */
	public String getSummary() {
		return getCalendarEntry().getSummary().getPlainText();
	}

	/**
	 * Sets the summary of the calendar
	 * 
	 * @param summary
	 */
	public void setSummary(String summary) {
		getCalendarEntry().setSummary(new PlainTextConstruct(summary));
	}

	/**
	 * @return Returns the calendar timeZone property
	 */
	public TimeZoneProperty getTimeZone() {
		return getCalendarEntry().getTimeZone();
	}

	/**
	 * 
	 * @return Returns the color property
	 */
	public ColorProperty getColor() {
		return getCalendarEntry().getColor();
	}

	/**
	 * Sets the color property
	 * 
	 * @param color
	 */
	public void setColor(ColorProperty color) {
		getCalendarEntry().setColor(color);
	}

	/**
	 * @return Returns the calendarEntry
	 */
	public CalendarEntry getCalendarEntry() {
		return calendarEntry;
	}

	/**
	 * Sets the calendarEntry
	 * 
	 * @param calendarEntry
	 */
	public void setCalendarEntry(CalendarEntry calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	/**
	 * @return Return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Sets the group
	 * 
	 * @param group
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * 
	 * @return Returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return Returns the group's domain
	 */
	public Domain getDomain() {
		return getGroup().getDomain();
	}

	/**
	 * Sets the calendar timeZone property
	 * 
	 * @param timeZone
	 */
	public void getTimeZone(TimeZoneProperty timeZone) {
		getCalendarEntry().setTimeZone(timeZone);
	}

	/**
	 * 
	 * @return
	 */
	public HiddenProperty isHidden() {
		return getCalendarEntry().getHidden();
		// TODO: Terminar de comentar o calendário
	}

	/**
	 * 
	 * @param hidden
	 */
	public void setHidden(HiddenProperty hidden) {
		getCalendarEntry().setHidden(hidden);
		// TODO: Terminar de comentar o calendário
	}

	/**
	 * 
	 * @return
	 */
	public List<Where> getLocations() {
		return getCalendarEntry().getLocations();
		// TODO: Terminar de comentar o calendário
	}

	/**
	 * 
	 * @param where
	 */
	public void addLocation(Where where) {
		getCalendarEntry().addLocation(where);
		// TODO: Terminar de comentar o calendário
	}
}