package com.integrareti.integraframework.dao.integra;

import java.util.List;

import com.integrareti.integraframework.business.DeletedGoogleUser;

/**
 * This interface offers methods manipulates DeletedGoogleUser`s datas
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public interface DeletedGoogleUserDao extends GenericDao<DeletedGoogleUser, Integer> {
	/**
	 * 
	 * @return Returns all DeletedGoogleUser until five days ago
	 */
	public List<DeletedGoogleUser> getDeletedGoogleUserUntilFiveDaysAgoByDomainName(String domainName) throws Exception;

	/**
	 * 
	 * @return Returns all deleted Google account until five days ago
	 */
	public List<String> getDeletedGoogleAccountUntilFiveDaysAgoByDomainName(String domainName) throws Exception;
}
