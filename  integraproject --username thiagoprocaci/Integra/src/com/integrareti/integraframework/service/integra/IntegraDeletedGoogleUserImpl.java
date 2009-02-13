package com.integrareti.integraframework.service.integra;

import java.util.List;

import com.integrareti.integraframework.business.DeletedGoogleUser;
import com.integrareti.integraframework.dao.integra.DeletedGoogleUserDao;

/**
 * This class offers services to manipulates a DeletedGoogleUser
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class IntegraDeletedGoogleUserImpl extends
		IntegraServiceImpl<DeletedGoogleUser, Integer> implements
		IntegraDeletedGoogleUserInterface {

	private DeletedGoogleUserDao deletedGoogleUserDao;
	
	public IntegraDeletedGoogleUserImpl(
			DeletedGoogleUserDao deletedGoogleUserDao) {
		super(deletedGoogleUserDao);
		this.deletedGoogleUserDao = deletedGoogleUserDao;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns all DeletedGoogleUser until five days ago
	 * @throws Exception
	 */
	@Override
	public List<DeletedGoogleUser> getDeletedGoogleUserUntilFiveDaysAgoByDomainName(
			String domainName) throws Exception {
		return deletedGoogleUserDao.getDeletedGoogleUserUntilFiveDaysAgoByDomainName(domainName);
	}

	/**
	 * 
	 * @return Returns all deleted Google account until five days ago
	 */
	@Override
	public List<String> getDeletedGoogleAccountUntilFiveDaysAgoByDomainName(
			String domainName) throws Exception {
		return deletedGoogleUserDao.getDeletedGoogleAccountUntilFiveDaysAgoByDomainName(domainName);
	}
	
}
