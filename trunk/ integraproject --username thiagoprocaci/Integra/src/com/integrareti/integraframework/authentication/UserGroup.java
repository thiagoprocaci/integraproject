package com.integrareti.integraframework.authentication;

import com.integrareti.integraframework.business.Identifiable;

/**
 * 
 * Describes a user Group
 * 
 * @version 1.0
 * @created 02-Oct-2007 15:27:37
 */
@SuppressWarnings("serial")
public class UserGroup implements Identifiable<Integer> {
	// PUBLIC CONSTANTS ------------------------------
	public static final String PROFESSOR_GROUP = "PROFESSOR";
	public static final String STUDENT_GROUP = "STUDENT";
	public static final String EMPLOYEE_GROUP = "EMPLOYEE";
	public static final String GOOGLE_ADMIN_GROUP = "GOOGLEADMIN";
	public static final String DOMAIN_ADMIN_GROUP = "ADMIN";
	// --------------------------------------------------
	private Integer id;
	private String name;

	/**
	 * Creates a new UserGroup
	 * 
	 * @param name
	 */
	public UserGroup(String name) {
		this.name = name;
	}

	/**
	 * Creates a new UserGroup
	 */
	public UserGroup() {
	}

	/**
	 * 
	 * @return returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UserGroup other = (UserGroup) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}