package com.integrareti.integraframework.service.integra;

import java.util.List;

import com.integrareti.integraframework.business.DeletedGoogleUser;

/**
 * This interface offers services to manipulates a DeletedGoogleUser
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public interface IntegraDeletedGoogleUserInterface extends IntegraServiceInterface<DeletedGoogleUser, Integer> {
	/**
	 * 
	 * @param domainName
	 * @return Returns all DeletedGoogleUser until five days ago
	 * @throws Exception
	 */
	public List<DeletedGoogleUser> getDeletedGoogleUserUntilFiveDaysAgoByDomainName(String domainName) throws Exception;

	/**
	 * 
	 * @return Returns all deleted Google account until five days ago
	 */
	public List<String> getDeletedGoogleAccountUntilFiveDaysAgoByDomainName(String domainName) throws Exception;
}
