package com.integrareti.integraframework.dao.integra;

import java.util.Iterator;
import java.util.List;

import com.google.gdata.data.appsforyourdomain.Login;
import com.integrareti.integraframework.business.Domain;

/**
 * This class offers methods to manipulates the domain's data
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class DomainDaoHibernate extends GenericDaoHibernate<Domain, Integer> implements DomainDao {
	/**
	 * 
	 * @param domainName
	 * @return Returns a domain by name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Domain getDomainByName(String domainName) throws Exception {
		List<Domain> list = (List<Domain>) getHibernateTemplate().find("SELECT d FROM Domain d WHERE d.name =?", domainName);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param unitName
	 * @return Returns a domain by unit name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Domain getDomainByUnitName(String unitName) throws Exception {
		List<Domain> list = (List<Domain>) getHibernateTemplate().find("SELECT  d FROM Domain d,Unit u " + " WHERE u in elements(d.units) AND " + "u.name = ?", unitName.toUpperCase());
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns the admin login of the domain
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Login getGoogleDomainAdminLogin(String domainName) throws Exception {
		List<Object[]> list = (List<Object[]>) getHibernateTemplate().find("SELECT d.googleDomainAdmin, d.googleDomainPassword " + " FROM Domain d WHERE d.name = ?", domainName);
		if (list != null && !list.isEmpty()) {
			Login login = new Login();
			for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
				Object[] data = (Object[]) iterator.next();
				login.setUserName(data[0].toString());
				login.setPassword(data[1].toString());
			}
			return login;
		}
		return null;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns all domains by name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Domain> getAllByDomainName(String domainName) throws Exception {
		return getHibernateTemplate().find("FROM Domain d WHERE d.name = ?", domainName);
	}
}
