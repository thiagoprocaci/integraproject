package com.integrareti.integraframework.service.integra;

import com.google.gdata.data.appsforyourdomain.Login;
import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.dao.integra.DomainDao;
import com.integrareti.integraframework.valueobject.SectorVO;

/**
 * This class offers services to manipulates a domain
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class IntegraDomainServiceImpl extends
		IntegraServiceImpl<Domain, Integer> implements
		IntegraDomainServiceInterface {

	public DomainDao domainDao;

	/**
	 * Creates a new IntegraDomainServiceImpl
	 * 
	 * @param domainDao
	 * @param personDao
	 * 
	 */
	public IntegraDomainServiceImpl(DomainDao domainDao) {
		super(domainDao);
		this.domainDao = domainDao;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns the admin login of the domain
	 * @throws Exception 
	 */
	@Override
	public Login getGoogleDomainAdminLogin(String domainName)
			throws Exception {
		return domainDao.getGoogleDomainAdminLogin(domainName);
	}

	/**
	 * 
	 * @param sectorVO
	 * @return Returns the domain by a sector
	 * @throws Exception 
	 */
	@Override
	public Domain getDomain(SectorVO sectorVO) throws Exception {
		Domain domain = null;
		domain = domainDao.getDomainByUnitName(sectorVO.getFatherSector());
		if (domain != null) 
			return domain;
		domain = domainDao.getDomainByUnitName(sectorVO.getSector());
		return domain;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns domain by name
	 * @throws Exception
	 */
	@Override
	public Domain getDomainByName(String domainName) throws Exception {		
		return domainDao.getDomainByName(domainName);
	}

}
