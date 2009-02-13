package com.integrareti.integraframework.service.integra;

import com.google.gdata.data.appsforyourdomain.Login;
import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.valueobject.SectorVO;

/**
 * This interface offers services to manipulates a domain
 * 
 * @author Thiago
 * 
 */
public interface IntegraDomainServiceInterface extends IntegraServiceInterface<Domain, Integer> {
	/**
	 * 
	 * @param domainName
	 * @return Returns the admin login of the domain
	 * @throws Exception
	 */
	public Login getGoogleDomainAdminLogin(String domainName) throws Exception;

	/**
	 * Returns a domain by a sectorVO
	 * 
	 * @param sectorVO
	 * @return
	 * @throws Exception
	 */
	public Domain getDomain(SectorVO sectorVO) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @return Returns domain by name
	 * @throws Exception
	 */
	public Domain getDomainByName(String domainName) throws Exception;
}
