package com.integrareti.integraframework.controller.adm.group.log;

import java.util.Date;
import java.util.List;

import com.integrareti.integraframework.business.log.GroupLog;
import com.integrareti.integraframework.service.integra.IntegraGroupLogServiceInterface;

/**
 * This class gives to groupLog.zul (view) the access to business components
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class GroupLogController {

	private IntegraGroupLogServiceInterface groupLogService;
	
	/**
	 * Creates a new GroupLogController
	 * @param groupLogService
	 */
	public GroupLogController(IntegraGroupLogServiceInterface groupLogService){
		this.groupLogService = groupLogService;
	}
	
	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period
	 */
	public List<GroupLog> getGroupLogByPeriod(Date begin,Date end) throws Exception{
		return groupLogService.getByPeriod(begin, end);
	}
	
	/**
	 * 
	 * @param begin
	 * @param end
	 * @return Returns groupLogs by period with errors
	 */
	public List<GroupLog> getGroupLogByPeriodWithErrors(Date begin,Date end) throws Exception{
		return groupLogService.getByPeriodWithErrors(begin, end);
	}
	
}
