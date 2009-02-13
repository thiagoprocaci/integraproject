package com.integrareti.integraframework.business;

import java.util.HashSet;
import java.util.Set;

/**
 * Describes an entity that represents a domain at google
 * 
 * @version 1.0
 * @created 22-May-2007 14:55:55
 * @author Thiago Athouguia Gama
 */
@SuppressWarnings("serial")
public class Domain implements Identifiable<Integer> {
	private Integer id;
	private String name;
	private Set<Group> groups;
	private Set<Person> members;
	private Set<EmailList> emailLists;
	private Set<Unit> units;
	private String googleDomainAdmin;
	private String googleDomainPassword;

	/**
	 * Creates a new domain
	 */
	public Domain() {
		groups = new HashSet<Group>();
		members = new HashSet<Person>();
		emailLists = new HashSet<EmailList>();
		units = new HashSet<Unit>();
	}

	/**
	 * @return Returns groups
	 */
	public Set<Group> getGroups() {
		return groups;
	}

	/**
	 * Sets groups
	 * 
	 * @param domainGroups
	 */
	public void setGroups(Set<Group> domainGroups) {
		this.groups = domainGroups;
	}

	/**
	 * Adds a new group
	 * 
	 * @param group
	 */
	public void addGroup(Group group) {
		this.groups.add(group);
	}

	/**
	 * @return Returns the members of the domain
	 */
	public Set<Person> getMembers() {
		return members;
	}

	/**
	 * Sets the members of the domain
	 * 
	 * @param domainMembers
	 */
	public void setMembers(Set<Person> domainMembers) {
		this.members = domainMembers;
	}

	/**
	 * Adds a new member
	 * 
	 * @param person
	 */
	public void addMembers(Person person) {
		this.members.add(person);
	}

	/**
	 * @return Returns the name of the domain
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the domain
	 * 
	 * @param domainName
	 */
	public void setName(String domainName) {
		this.name = domainName;
	}

	/**
	 * @return Returns id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets id
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the emailLists of the domain
	 */
	public Set<EmailList> getEmailLists() {
		return emailLists;
	}

	/**
	 * Sets the emailLists of the domain
	 * 
	 * @param emailLists
	 */
	public void setEmailLists(Set<EmailList> emailLists) {
		this.emailLists = emailLists;
	}

	/**
	 * Adds a new emailList
	 * 
	 * @param emailList
	 */
	public void addEmailList(EmailList emailList) {
		this.emailLists.add(emailList);
	}

	/**
	 * 
	 * @return returns the units
	 */
	public Set<Unit> getUnits() {
		return units;
	}

	/**
	 * Sets the units
	 * 
	 * @param units
	 */
	public void setUnits(Set<Unit> units) {
		this.units = units;
	}

	/**
	 * Adds a new Unit
	 * 
	 * @param unit
	 */
	public void addUnit(Unit unit) {
		this.units.add(unit);
	}

	/**
	 * 
	 * @return Returns googleDomainAdmin
	 */
	public String getGoogleDomainAdmin() {
		return googleDomainAdmin;
	}

	/**
	 * Sets googleDomainAdmin
	 * 
	 * @param googleDomainAdmin
	 */
	public void setGoogleDomainAdmin(String googleDomainAdmin) {
		this.googleDomainAdmin = googleDomainAdmin;
	}

	/**
	 * 
	 * @return Returns googleDomainPassword
	 */
	public String getGoogleDomainPassword() {
		return googleDomainPassword;
	}

	/**
	 * Sets googleDomainPassword
	 * 
	 * @param googleDomainPassword
	 */
	public void setGoogleDomainPassword(String googleDomainPassword) {
		this.googleDomainPassword = googleDomainPassword;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
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
		final Domain other = (Domain) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}