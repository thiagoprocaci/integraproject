package com.integrareti.integraframework.business.task;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.integrareti.integraframework.business.Identifiable;

/**
 * This entity describes a task that system have to do
 * 
 * @author Thiago Procaci
 * 
 */
@SuppressWarnings("serial")
public class SystemTask implements Identifiable<Integer> {

	private Integer id;
	private String name;
	private String description;
	private Calendar createTime;

	/**
	 * Creates a new SystemTask
	 * 
	 * @param name
	 * @param description
	 */
	public SystemTask(String name, String description) {
		this.name = name;
		this.description = description;
		this.createTime = new GregorianCalendar();
	}

	/**
	 * Creates a new SystemTask
	 * 
	 */
	public SystemTask() {
		this.createTime = new GregorianCalendar();
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
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return Returns name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return Returns description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return Returns createTime
	 */
	public Calendar getCreateTime() {
		return createTime;
	}

	/**
	 * Sets createTime
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}

}
