package com.integrareti.integraframework.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Offers methods to manipulates calendar and dates
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class CalendarUtil {
	/**
	 * 
	 * @param date
	 * @return Returns a calendar with the first instant of a date
	 */
	public static Calendar getFirstInstant(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * 
	 * @param date
	 * @return Returns a calendar with the last instant of a date
	 */
	public static Calendar getLastInstant(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c;
	}
}
