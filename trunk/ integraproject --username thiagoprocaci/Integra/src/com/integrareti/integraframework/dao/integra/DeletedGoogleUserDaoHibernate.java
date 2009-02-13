package com.integrareti.integraframework.dao.integra;

import java.util.Calendar;
import java.util.List;

import com.integrareti.integraframework.business.DeletedGoogleUser;

/**
 * This class offers methods manipulates DeletedGoogleUser`s datas
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class DeletedGoogleUserDaoHibernate extends GenericDaoHibernate<DeletedGoogleUser, Integer> implements DeletedGoogleUserDao {
	/**
	 * @return Returns all DeletedGoogleUser by domains
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DeletedGoogleUser> getAllByDomainName(String domainName) throws Exception {
		return getHibernateTemplate().find("select d from DeletedGoogleUser d where d.domain.name = ?", domainName);
	}

	/**
	 * 
	 * @return Returns all DeletedGoogleUser until five days ago
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DeletedGoogleUser> getDeletedGoogleUserUntilFiveDaysAgoByDomainName(String domainName) throws Exception {
		Calendar fiveDaysAgo = Calendar.getInstance();
		fiveDaysAgo.add(Calendar.DATE, -5);
		return getHibernateTemplate().find("select d from DeletedGoogleUser d where d.exclusionDate between  ? and ? ", new Calendar[] { fiveDaysAgo, Calendar.getInstance() });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDeletedGoogleAccountUntilFiveDaysAgoByDomainName(String domainName) throws Exception {
		Calendar fiveDaysAgo = Calendar.getInstance();
		fiveDaysAgo.add(Calendar.DATE, -5);
		return getHibernateTemplate().find("select d.deletedGoogleAccount from DeletedGoogleUser d where d.exclusionDate between  ? and ? ", new Calendar[] { fiveDaysAgo, Calendar.getInstance() });
	}
}
