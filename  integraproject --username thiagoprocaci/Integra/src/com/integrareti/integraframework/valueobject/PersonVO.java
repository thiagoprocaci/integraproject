package com.integrareti.integraframework.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.integrareti.integraframework.business.Domain;

/**
 * This class describes a value object of Person
 * 
 * @author Thiago
 * 
 */
@SuppressWarnings("serial")
public class PersonVO implements Serializable {

	private Integer id = null;
	private String familyName = null;
	private String givenName = null;
	private String deptoOrCourse = null;
	private String registry = null;
	private SectorVO sector = null;
	private String password = null;
	private String googleAccount = null;
	private List<GroupVO> subjects = new ArrayList<GroupVO>(0);
	private List<String> userPositionGroups = new ArrayList<String>(0);
	private Domain domain = null;
	
	/**
	 * Creates a new PersonVO
	 * 
	 * @param name
	 * @param position
	 * @param department
	 * @param course
	 * @param subjects
	 */
	public PersonVO(Integer id, String givenName, String familyName,
			String password, String registry, String googleAccount,
			String deptoOrCourse, List<GroupVO> subjects,
			List<String> userGroups, SectorVO sector, Domain domain) {
		this.givenName = givenName;
		this.familyName = familyName;
		this.deptoOrCourse = deptoOrCourse;
		this.subjects = subjects;
		this.registry = registry;
		this.password = password;
		this.googleAccount = googleAccount;
		this.id = id;
		this.sector = sector;
		this.userPositionGroups = userGroups;
		this.domain = domain;
	}

	/**
	 * Creates an empty PersonVO
	 */
	public PersonVO() {

	}

	/**
	 * 
	 * @return returns the deptoOrCourse
	 */
	public String getDeptoOrCourse() {
		return deptoOrCourse;
	}

	/**
	 * Sets the deptoOrCourse
	 * 
	 * @param course
	 */
	public void setDeptoOrCourse(String depto_or_course) {
		this.deptoOrCourse = depto_or_course;
	}

	/**
	 * 
	 * @return returns the subjects
	 */
	public List<GroupVO> getSubjects() {
		return subjects;
	}

	/**
	 * Sets the subjects
	 * 
	 * @param subjects
	 */
	public void setSubjects(List<GroupVO> subjects) {
		this.subjects = subjects;
	}

	/**
	 * Adds an object in the sujects list
	 * 
	 * @param o
	 */
	public void addSubject(GroupVO o) {
		subjects.add(o);
	}

	/**
	 * 
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * Sets the familyName
	 * 
	 * @param familyName
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	/**
	 * 
	 * @return the givenName
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * Sets the givenName
	 * 
	 * @param givenName
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/**
	 * 
	 * @return the registry
	 */
	public String getRegistry() {
		return registry;
	}

	/**
	 * Sets the registry
	 * 
	 * @param registry
	 */
	public void setRegistry(String registry) {
		this.registry = registry;
	}

	/**
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return returns the googleAccount
	 */
	public String getGoogleAccount() {
		return googleAccount;
	}

	/**
	 * Sets the googleAccount
	 * 
	 * @param googleAccount
	 */
	public void setGoogleAccount(String googleAccount) {
		this.googleAccount = googleAccount;
	}

	/**
	 * 
	 * @return the id
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
	 * @return the sector
	 */
	public SectorVO getSector() {
		return sector;
	}

	/**
	 * Sets the sector
	 * 
	 * @param sector
	 */
	public void setSector(SectorVO sector) {
		this.sector = sector;
	}

	/**
	 * 
	 * @return returns the userGroups
	 */
	public List<String> getUserPositionGroups() {
		return userPositionGroups;
	}

	/**
	 * Sets the userGroups
	 * 
	 * @param userGroups
	 */
	public void setUserPositionGroups(List<String> userGroups) {
		this.userPositionGroups = userGroups;
	}

	/**
	 * Adds a userGroup
	 * 
	 * @param userGroup
	 */
	public void addUserGroup(String userGroup) {
		userPositionGroups.add(userGroup);
	}
	
	/**
	 * 
	 * @return Returns name
	 */
	public String getName() {
		if (getGivenName() != null && getFamilyName() != null)
			return getGivenName() + " " + getFamilyName();
		if (getGivenName() != null && getFamilyName() == null)
			return getGivenName();
		return null;
	}

	/**
	 * Sets the name of this personVO. Separates the givenName of the familyName
	 * 
	 * @param name
	 */
	public void setName(String name) {
		if (name != null) {
			Scanner nameScanner = new Scanner(name).useDelimiter("\\s* \\s*");
			String givenName = null;
			String familyName = null;
			int count = 0;
			while (nameScanner.hasNext()) {
				String s = nameScanner.next();
				if (count == 0) {
					givenName = s;
					count++;
				} else {
					if (familyName == null) 
						familyName = s;
					 else 
						familyName += " " + s;
				}
			}
			this.setGivenName(givenName);
			this.setFamilyName(familyName);
		}
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

}
