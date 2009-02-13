package com.integrareti.integraframework.dao.integra;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.integrareti.integraframework.business.log.GroupLog;
import com.integrareti.integraframework.util.CalendarUtil;

/**
 * This interface offers methods to manipulates groupLog entities
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class GroupLogDaoHibernate extends GenericDaoHibernate<GroupLog, Integer> implements GroupLogDao {
	/**
	 * @return Returns groupLog by domain
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GroupLog> getAllByDomainName(String domainName) throws Exception {
		List<GroupLog> list = (List<GroupLog>) getHibernateTemplate().find("from GroupLog g where g.domain.name =?", domainName);
		return list;
	}

	/**
	 * Saves a GroupLog
	 */
	@Override
	public void save(GroupLog row) throws Exception {
		row.setEndTime(new GregorianCalendar());
		super.save(row);
	}

	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GroupLog> getByPeriod(Date begin, Date end) throws Exception {
		Calendar beginTime = CalendarUtil.getFirstInstant(begin);
		Calendar endTime = CalendarUtil.getLastInstant(end);
		List<GroupLog> list = (List<GroupLog>) getHibernateTemplate().find("from GroupLog g where g.beginTime between  ? and ? order by g.beginTime desc", new Calendar[] { beginTime, endTime });
		return list;
	}

	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period with errors
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GroupLog> getByPeriodWithErrors(Date begin, Date end) throws Exception {
		Calendar beginTime = CalendarUtil.getFirstInstant(begin);
		Calendar endTime = CalendarUtil.getLastInstant(end);
		List<GroupLog> list = (List<GroupLog>) getHibernateTemplate().find("select g from GroupLog g, SystemGroupTask t , SystemGroupError e where (g.beginTime between ? and ?) and t in elements(g.tasks) and e in elements(t.errors) order by g.beginTime desc", new Calendar[] { beginTime, endTime });
		return list;
	}
}
