package com.integrareti.integraframework.dao.integra;

import java.util.List;

import com.google.gdata.data.appsforyourdomain.Login;
import com.integrareti.integraframework.business.Domain;

/**
 * This interface offers methods to manipulates the domain's data
 * 
 * @author Thiago
 * 
 */
public interface DomainDao extends GenericDao<Domain, Integer> {
	/**
	 * 
	 * @param unitName
	 * @return Returns a domain by unit name
	 * @throws Exception
	 */
	public Domain getDomainByUnitName(String unitName) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @return Returns a domain by name
	 * @throws Exception
	 */
	public Domain getDomainByName(String domainName) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @return Returns the admin login of the domain
	 * @throws Exception
	 */
	public Login getGoogleDomainAdminLogin(String domainName) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @return Returns all domains by name
	 * @throws Exception
	 */
	public List<Domain> getAllByDomainName(String domainName) throws Exception;
}
