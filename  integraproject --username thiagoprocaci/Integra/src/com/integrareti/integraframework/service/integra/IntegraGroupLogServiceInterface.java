package com.integrareti.integraframework.service.integra;

import java.util.Date;
import java.util.List;

import com.integrareti.integraframework.business.log.GroupLog;

/**
 * This interface offers services to manipulate groupLog
 * @author Thiago Baesso Procaci
 *
 */
public interface IntegraGroupLogServiceInterface {

	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period
	 */
	public List<GroupLog> getByPeriod(Date begin,Date end) throws Exception;
	
	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period with errors
	 */
	public List<GroupLog> getByPeriodWithErrors(Date begin,Date end) throws Exception;
	
}
