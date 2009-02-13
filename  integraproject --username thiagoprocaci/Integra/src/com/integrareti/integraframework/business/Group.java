package com.integrareti.integraframework.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Describes an entity that represent a group of people, subgroups, group email
 * list.
 * 
 * @version 1.0
 * @created 22-May-2007 14:55:58
 * @author Thiago Athouguia Gama
 */
@SuppressWarnings("serial")
public class Group implements Identifiable<Integer> {

	// variables declaration
	private boolean active;
	private boolean manuallyCreated;
	private Integer id;
	private String name;
	private String description;
	private Set<Person> owners;
	private Set<Person> participants;
	private Set<Group> subGroups;
	private Set<EmailList> emailLists;
	private Domain domain;

	/**
	 * Creates a new groups
	 */
	public Group() {
		owners = new HashSet<Person>();
		participants = new HashSet<Person>();
		subGroups = new HashSet<Group>();
		emailLists = new HashSet<EmailList>();
	}

	/**
	 * 
	 * @return Returns id
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
	 * @return Returns the name of the group
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the group
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the owners of the group
	 * 
	 * @return
	 */
	public Set<Person> getOwners() {
		return owners;
	}

	/**
	 * Sets the owners of the group
	 * 
	 * @param owners
	 */
	public void setOwners(Set<Person> owners) {
		this.owners = owners;
		this.participants.addAll(owners);
	}

	/**
	 * Adds a new owner
	 * 
	 * @param owner
	 */
	public void addOwner(Person owner) {
		this.owners.add(owner);
		addParticipant(owner);
	}

	/**
	 * Removes an owner
	 * 
	 * @param owner
	 */
	public void removerOwner(Person owner) {
		this.owners.remove(owner);
	}

	/**
	 * @return Return the participants of the group
	 */
	public Set<Person> getParticipants() {
		return participants;
	}

	/**
	 * Sets the participants of the group
	 * 
	 * @param participants
	 */
	public void setParticipants(Set<Person> participants) {
		this.participants = participants;
	}

	/**
	 * Adds a new participant
	 * 
	 * @param person
	 */
	public void addParticipant(Person participant) {
		this.participants.add(participant);
	}

	/**
	 * Removes a participant
	 * 
	 * @param participant
	 */
	public void removeParticipant(Person participant) {
		this.participants.remove(participant);
		owners.remove(participant);
	}

	/**
	 * 
	 * @return Returns the subGroups of the group
	 */
	public Set<Group> getSubGroups() {
		return subGroups;
	}

	/**
	 * Sets the subGroups of the group
	 * 
	 * @param subGroups
	 */
	public void setSubGroups(Set<Group> subGroups) {
		this.subGroups = subGroups;
	}

	/**
	 * Adds a new subGroup
	 * 
	 * @param subGroup
	 */
	public void addSubGroup(Group subGroup) {
		this.subGroups.add(subGroup);
	}

	/**
	 * Removes a subGroup
	 * 
	 * @param subGroup
	 */
	public void removeSubGroup(Group subGroup) {
		this.subGroups.remove(subGroup);
	}

	/**
	 * @return Returns the domain of the group
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * Sets the domain of the group
	 * 
	 * @param domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return Returns the email lists of the group
	 */
	public Set<EmailList> getEmailLists() {
		return emailLists;
	}

	/**
	 * Sets the email list of the group
	 * 
	 * @param emailLists
	 */
	public void setEmailLists(Set<EmailList> emailLists) {
		this.emailLists = emailLists;
	}

	/**
	 * Adds a new email list
	 * 
	 * @param emailList
	 */
	public void addEmailList(EmailList emailList) {
		this.emailLists.add(emailList);
	}

	/**
	 * Removes a email list
	 * 
	 * @param emailList
	 */
	public void removeEmailList(EmailList emailList) {
		this.emailLists.remove(emailList);
	}

	/**
	 * 
	 * @return Returns active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets active
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
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
	 * @return manuallyCreated
	 */
	public boolean isManuallyCreated() {
		return manuallyCreated;
	}

	/**
	 * Sets manuallyCreated
	 * 
	 * @param manuallyCreated
	 */
	public void setManuallyCreated(boolean manuallyCreated) {
		this.manuallyCreated = manuallyCreated;
	}

	/**
	 * 
	 * @return Returns all participants registries
	 */
	public List<String> getParticipantsRegistries() {
		List<String> registries = new ArrayList<String>();
		for (Person person : this.participants)
			registries.add(person.getRegistry());
		return registries;
	}

	/**
	 * 
	 * @param peopleToBeAdded
	 * @return returns the number of emailLists needed to be created
	 */
	public Integer getNumberOfNeededEmailLists(Integer peopleToBeAdded) {
		int freeSpace = 0;
		int numberOfLists = 0;
		for (EmailList emailList : this.emailLists)
			freeSpace = freeSpace + emailList.getEmailListFreeSpace();
		if (freeSpace < peopleToBeAdded) {
			freeSpace = peopleToBeAdded - freeSpace;
			numberOfLists = 1;
			while (freeSpace > EmailList.LIMIT_RECIPIENTS) {
				numberOfLists++;
				freeSpace -= EmailList.LIMIT_RECIPIENTS;
			}
		}
		return numberOfLists;
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
		final Group other = (Group) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {	
		return getName() + " - (" + getParticipants().size() + ")" ;
	}
}