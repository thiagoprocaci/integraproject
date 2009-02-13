package com.integrareti.integraframework.test;

import com.google.gdata.data.calendar.CalendarAclRole;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.data.extensions.Where;
import com.integrareti.integraframework.dao.google.GoogleCalendarDaoImpl;

public class CalendarTest extends BasicIntegraTestCase {

	private GoogleCalendarDaoImpl googleCalendarDao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		googleCalendarDao = (GoogleCalendarDaoImpl) getBean("googleCalendarDao");
	}

	public void testCreateCalendar() {
		String title = "ave maria";
		String summary = "ave maria";
		TimeZoneProperty timeZone = new TimeZoneProperty();
		ColorProperty color = new ColorProperty("#1B887A");
		HiddenProperty hidden = new HiddenProperty("false");
		Where location = new Where();
		String domainName = "ice.ufjf.br";
		//String calendarId = "ice.ufjf.br_omfjup4069pn4ov0c1n64s795g@group.calendar.google.com";
		//String calendarId2 = "ice.ufjf.br_ncsvmrkl617gn55efkoluc25jg@group.calendar.google.com";
		String calendarId3 = "ice.ufjf.br_2ei151kd448hocdfvb17toi7jc@group.calendar.google.com";
		boolean b = true;
		try {
			googleCalendarDao.createCalendar(title, summary, timeZone, hidden,
					color, location, domainName);
			/*
			 * googleCalendarDao .addAccessControl(
			 * "thiago.procaci@ice.ufjf.br", CalendarAclRole.EDITOR,
			 * "ice.ufjf.br",
			 * "http://www.google.com/calendar/feeds/admgoogle%40ice.ufjf.br/ice.ufjf.br_omfjup4069pn4ov0c1n64s795g%40group.calendar.google.com");
			 * googleCalendarDao .addAccessControl( "thiago.gama@ice.ufjf.br",
			 * CalendarAclRole.READ, "ice.ufjf.br",
			 * "http://www.google.com/calendar/feeds/admgoogle%40ice.ufjf.br/ice.ufjf.br_omfjup4069pn4ov0c1n64s795g%40group.calendar.google.com");
			 */
			// googleCalendarDao.printUserCalendars(domainName);
			googleCalendarDao.addAccessControl("thiago.procaci@ice.ufjf.br",
					CalendarAclRole.EDITOR, domainName, calendarId3);

		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}
}
