package com.integrareti.integraframework.business.log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Identifiable;
import com.integrareti.integraframework.business.task.SystemGroupTask;

/**
 * This entity describes the log of tasks and errors during any database
 * operation with {@link Group}
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class GroupLog implements Identifiable<Integer> {

	private Integer id;
	private String groupName;
	private String groupDescription;
	private Set<SystemGroupTask> tasks;
	private Domain domain;
	private Calendar beginTime;
	private Calendar endTime;

	/**
	 * Creates a new GroupLog
	 * 
	 * @param groupName
	 * @param groupDescription
	 * @param tasks
	 */
	public GroupLog(String groupName, String groupDescription, Set<SystemGroupTask> tasks, Domain domain) {
		this.groupName = groupName;
		this.groupDescription = groupDescription;
		this.tasks = tasks;
		this.domain = domain;
		beginTime = new GregorianCalendar();
	}
	
	/**
	 * Creates a new GroupLog
	 * 
	 * @param groupName
	 * @param groupDescription
	 * @param tasks
	 */
	public GroupLog(String groupName, String groupDescription, Domain domain) {
		this.groupName = groupName;
		this.groupDescription = groupDescription;		
		this.domain = domain;
		this.tasks = new HashSet<SystemGroupTask>(0);
		beginTime = new GregorianCalendar();
	}

	/**
	 * Creates a new GroupLog
	 */
	public GroupLog() {
		tasks = new HashSet<SystemGroupTask>(0);
		beginTime = new GregorianCalendar();
	}

	/**
	 * @return Returns id
	 */
	@Override
	public Integer getId() {
		return this.id;
	}

	/**
	 * Sets id
	 * 
	 * @param id
	 * 
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return Returns groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Sets groupName
	 * 
	 * @param groupName
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 
	 * @return Returns description
	 */
	public String getGroupDescription() {
		return groupDescription;
	}

	/**
	 * Sets description
	 * 
	 * @param groupDescription
	 */
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	/**
	 * 
	 * @return Returns tasks
	 */
	public Set<SystemGroupTask> getTasks() {
		return tasks;
	}

	/**
	 * Sets tasks
	 * 
	 * @param tasks
	 */
	public void setTasks(Set<SystemGroupTask> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Adds a new task
	 * @param task
	 */
	public void addTask(SystemGroupTask task) {
		this.tasks.add(task);
	}

	/**
	 * Removes a task
	 * @param task
	 */
	public void removeTask(SystemGroupTask task){
		this.tasks.remove(task);
	}

	/**
	 * 
	 * @return Returns domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * Sets domain
	 * @param domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * 
	 * @return Returns beginTime
	 */
	public Calendar getBeginTime() {
		return beginTime;
	}

	/**
	 * Sets beginTime
	 * @param beginTime
	 */
	public void setBeginTime(Calendar beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * 
	 * @return Returns endTime
	 */
	public Calendar getEndTime() {
		return endTime;
	}

	/**
	 * Sets endTime
	 * @param endTime
	 */
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

}
