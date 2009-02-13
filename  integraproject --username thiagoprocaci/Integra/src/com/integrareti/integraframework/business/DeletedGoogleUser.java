package com.integrareti.integraframework.business;

import java.util.Calendar;

/**
 * This entity describes a deleted google user
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class DeletedGoogleUser implements Identifiable<Integer> {

	private Integer id;
	private String deletedGoogleAccount;
	private Calendar exclusionDate;
	private Domain domain;

	/**
	 * @return returns id
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
	 * @return Returns deleted google account
	 */
	public String getDeletedGoogleAccount() {
		return deletedGoogleAccount;
	}

	/**
	 * Sets deleted google account
	 * 
	 * @param deletedGoogleAccount
	 */
	public void setDeletedGoogleAccount(String deletedGoogleAccount) {
		this.deletedGoogleAccount = deletedGoogleAccount;
	}

	/**
	 * 
	 * @return Returns date of exclusion
	 */
	public Calendar getExclusionDate() {
		return exclusionDate;
	}

	/**
	 * Sets exclusionDate
	 * 
	 * @param exclusionDate
	 */
	public void setExclusionDate(Calendar exclusionDate) {
		this.exclusionDate = exclusionDate;
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
	 * 
	 * @param domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * 
	 * @return Returns deleted email
	 */
	public String getEmailDeleted() {
		return deletedGoogleAccount + "@" + domain.getName();
	}

}
