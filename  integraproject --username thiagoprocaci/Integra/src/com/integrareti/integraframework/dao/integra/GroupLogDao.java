package com.integrareti.integraframework.dao.integra;

import java.util.Date;
import java.util.List;

import com.integrareti.integraframework.business.log.GroupLog;

/**
 * This interface offers methods to manipulates groupLog entities
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public interface GroupLogDao extends GenericDao<GroupLog, Integer> {
	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period
	 */
	public List<GroupLog> getByPeriod(Date begin, Date end) throws Exception;

	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period with errors
	 */
	public List<GroupLog> getByPeriodWithErrors(Date begin, Date end) throws Exception;
}
