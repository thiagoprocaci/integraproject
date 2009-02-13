package com.integrareti.integraframework.valueobject;

import java.io.Serializable;

/**
 * This class describes a VO object of a group
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class GroupVO implements Serializable{

	private String classroom;
	private String subjectCode;
	private String subjectName;
	private String departament;
	
	/**
	 * Creates a new SubjectVO
	 * 
	 * @param name
	 * @param code
	 */
	public GroupVO(String classroom, String subjectCode, String subjectName,String departament) {
		this.classroom = classroom;
		this.subjectCode = subjectCode;
		this.subjectName = subjectName;
		this.departament = departament;
	}

	/**
	 * Creates an empty SubjectVO
	 */
	public GroupVO() {

	}

	/**
	 * 
	 * @return Returns the calssroom
	 */
	public String getClassroom() {
		return classroom;
	}

	/**
	 * Sets the classroom
	 * 
	 * @param classroom
	 */
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	/**
	 * 
	 * @return Returns the subject code
	 */
	public String getSubjectCode() {
		return subjectCode;
	}

	/**
	 * Sets the subject code
	 * 
	 * @param subjectCode
	 */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	/**
	 * 
	 * @return Returns the subject name
	 */
	public String getSubjectName() {
		return subjectName;
	}

	/**
	 * Sets the subjects name
	 * 
	 * @param subjectName
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	/**
	 * 
	 * @return Returns departament
	 */
	public String getDepartament() {
		return departament;
	}

	/**
	 * Sets departament
	 * @param departament
	 */
	public void setDepartament(String departament) {
		this.departament = departament;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((classroom == null) ? 0 : classroom.hashCode());
		result = PRIME * result + ((departament == null) ? 0 : departament.hashCode());
		result = PRIME * result + ((subjectCode == null) ? 0 : subjectCode.hashCode());
		result = PRIME * result + ((subjectName == null) ? 0 : subjectName.hashCode());
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
		final GroupVO other = (GroupVO) obj;
		if (classroom == null) {
			if (other.classroom != null)
				return false;
		} else if (!classroom.equals(other.classroom))
			return false;
		if (departament == null) {
			if (other.departament != null)
				return false;
		} else if (!departament.equals(other.departament))
			return false;
		if (subjectCode == null) {
			if (other.subjectCode != null)
				return false;
		} else if (!subjectCode.equals(other.subjectCode))
			return false;
		if (subjectName == null) {
			if (other.subjectName != null)
				return false;
		} else if (!subjectName.equals(other.subjectName))
			return false;
		return true;
	}



}
