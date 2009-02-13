package com.integrareti.integraframework.authentication;

/**
 * Describes a permission of a group
 * 
 * @version 1.0
 * @created 02-Oct-2007 15:27:34
 */
public class GroupPermission {

	private UserGroup userGroup;
	private Transaction transaction;
	private boolean read;
	private boolean write;


	/**
	 * Creates a new GroupPermission
	 */
	public GroupPermission(){

	}
	
	/**
	 *  Creates a new GroupPermission
	 * @param userGroup
	 * @param transaction
	 */
	public GroupPermission(UserGroup userGroup,Transaction transaction){
		this.userGroup = userGroup;
		this.transaction = transaction;
	}
	
	/**
	 * 
	 * @return return userGroup
	 */
	public UserGroup getUserGroup() {
		return userGroup;
	}

	/**
	 * Sets the userGroup
	 * @param userGroup
	 */
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	/**
	 * 
	 * @return read
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * Sets read
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
	 * @param write
	 */
	public void setWrite(boolean write) {
		this.write = write;
	}

	/**
	 * 
	 * @return returns the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * Sets the transaction 
	 * @param transaction
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userGroup == null) ? 0 : userGroup.hashCode());
		result = prime * result
				+ ((transaction == null) ? 0 : transaction.hashCode());
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
		final GroupPermission other = (GroupPermission) obj;
		if (userGroup == null) {
			if (other.userGroup != null)
				return false;
		} else if (!userGroup.equals(other.userGroup))
			return false;
		if (transaction == null) {
			if (other.transaction != null)
				return false;
		} else if (!transaction.equals(other.transaction))
			return false;
		return true;
	}

	

}