package com.integrareti.integraframework.authentication;

import com.integrareti.integraframework.business.Person;

/**
 * Describe a permission of a person Component mapped inside Person.hbm.xml
 * 
 * @version 1.0
 * @created 02-Oct-2007 15:27:36
 */
public class PersonPermission {
	private Person person;
	private Transaction transaction;
	private boolean read;
	private boolean write;

	/**
	 * Creates a new permission
	 */
	public PersonPermission() {
	}

	/**
	 * Creates a new permission
	 */
	public PersonPermission(Person person, Transaction transaction, boolean read, boolean write) {
		this.person = person;
		this.transaction = transaction;
		this.read = read;
		this.write = write;
	}

	/**
	 * 
	 * @return returns the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * Sets the person
	 * 
	 * @param person
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * 
	 * @return returns the trasaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * Sets the trasaction
	 * 
	 * @param transaction
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * 
	 * @return returns read
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * Sets read
	 * 
	 * @param read
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * 
	 * @return write
	 */
	public boolean isWrite() {
		return write;
	}

	/**
	 * Sets write
	 * 
	 * @param write
	 */
	public void setWrite(boolean write) {
		this.write = write;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + ((transaction == null) ? 0 : transaction.hashCode());
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
		final PersonPermission other = (PersonPermission) obj;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (transaction == null) {
			if (other.transaction != null)
				return false;
		} else if (!transaction.equals(other.transaction))
			return false;
		return true;
	}
}