package com.integrareti.integraframework.service.integra;

import java.util.Date;
import java.util.List;

import com.integrareti.integraframework.business.log.GroupLog;
import com.integrareti.integraframework.dao.integra.GroupLogDao;

/**
 * This class offers services to manipulate groupLog
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class IntegraGroupLogServiceImpl extends IntegraServiceImpl<GroupLog, Integer> implements IntegraGroupLogServiceInterface {
	private GroupLogDao groupLogDao;

	/**
	 * Creates a new IntegraGroupLogServiceImpl
	 * 
	 * @param groupLogDao
	 */
	public IntegraGroupLogServiceImpl(GroupLogDao groupLogDao) {
		super(groupLogDao);
		this.groupLogDao = groupLogDao;
	}

	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period
	 */
	@Override
	public List<GroupLog> getByPeriod(Date begin, Date end) throws Exception {
		return groupLogDao.getByPeriod(begin, end);
	}

	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period with errors
	 */
	@Override
	public List<GroupLog> getByPeriodWithErrors(Date begin, Date end) throws Exception {
		return groupLogDao.getByPeriodWithErrors(begin, end);
	}
}
