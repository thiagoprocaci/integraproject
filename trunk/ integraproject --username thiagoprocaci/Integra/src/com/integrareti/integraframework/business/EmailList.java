package com.integrareti.integraframework.business;

import java.util.HashSet;
import java.util.Set;

import com.google.gdata.data.appsforyourdomain.provisioning.EmailListEntry;
import com.integrareti.integraframework.util.StringUtil;

/**
 * Describes an entity that represents an email List
 * 
 * @version 1.0
 * @created 27-Jul-2007 16:42:51
 * @author Thiago Athouguia Gama
 */

@SuppressWarnings("serial")
public class EmailList implements Identifiable<Integer> {

	public static Integer LIMIT_RECIPIENTS = 2000;

	private Integer id;
	private Group group;
	private Domain domain;
	private EmailListEntry emailListEntry;
	private Set<Person> recipients;

	/**
	 * Creates a new EmailList
	 */
	public EmailList() {
		emailListEntry = new EmailListEntry();
		com.google.gdata.data.appsforyourdomain.EmailList emailList = new com.google.gdata.data.appsforyourdomain.EmailList();
		emailListEntry.addExtension(emailList);
		recipients = new HashSet<Person>();
	}

	/**
	 * @return Returns the recipients of the email list
	 */
	public Set<Person> getRecipients() {
		return recipients;
	}

	/**
	 * Sets the recipients of the email list
	 * 
	 * @param recipients
	 */
	public void setRecipients(Set<Person> recipients) {
		this.recipients = recipients;
	}

	/**
	 * Adds a person in recipients set
	 * 
	 * @param person
	 */
	public void addRecipient(Person person) {
		this.recipients.add(person);
	}

	/**
	 * Removes a recipient
	 * 
	 * @param person
	 */
	public void removeRecipient(Person person) {
		this.recipients.remove(person);
	}

	/**
	 * @return Returns the group of the email list
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Sets the group
	 * 
	 * @param group
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @return Returns the id
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
	 * @return Returns the email list name
	 */
	public String getName() {
		return getEmailListEntry().getEmailList().getName();
	}

	/**
	 * Sets the email list name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		getEmailListEntry().getEmailList().setName(StringUtil.changeAccent(name));
	}

	/**
	 * @return Returns the domain of the email list
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
	 * @return Returns the emailListEntry
	 */
	public EmailListEntry getEmailListEntry() {
		return emailListEntry;
	}

	/**
	 * Sets the emailListEntry
	 * 
	 * @param emailListEntry
	 */
	public void setEmailListEntry(EmailListEntry emailListEntry) {
		this.emailListEntry = emailListEntry;
	}

	/**
	 * A email list can have only 2000 participants
	 * 
	 * @return Returns the number of free spaces in this email list. The number
	 *         of participants that can be added
	 * 
	 */
	public Integer getEmailListFreeSpace() {
		return EmailList.LIMIT_RECIPIENTS - recipients.size();
	}

	/**
	 * 
	 * @return Returns email list address
	 */
	public String getEmailAddress() {
		String address = getName() + "@" + getDomain().getName();
		if (address != null)
			address = address.toLowerCase();
		return address;
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
		final EmailList other = (EmailList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName() + "@" + getDomain().getName();
	}

}