package com.integrareti.integraframework.business.error;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.integrareti.integraframework.business.Identifiable;

/**
 * This entity describes error that can occur during any system operation
 * 
 * @author Thiago Procaci
 * 
 */
@SuppressWarnings("serial")
public class SystemError implements Identifiable<Integer> {
	
	private Integer id;
	private Calendar time;
	private String description;
	private String cause;	


	/**
	 * Creates a new SystemError
	 * @param time
	 * @param cause
	 */
	public SystemError(String cause,String description) {
		this.time = new GregorianCalendar();
		this.description = description;
		this.cause = cause;
	}

	/**
	 * Creates a new SystemError
	 */
	public SystemError(){
		this.time = new GregorianCalendar();
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
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @return Returns time
	 */
	public Calendar getTime() {
		return time;
	}
	
	/**
	 * Sets time
	 * @param time
	 */
	public void setTime(Calendar time) {
		this.time = time;
	}
	
	/**
	 * 
	 * @return Returns cause
	 */
	public String getCause() {
		return cause;
	}
	
	/**
	 * Sets cause
	 * @param cause
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}

	/**
	 * @return Returns id
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * Sets id
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
}
