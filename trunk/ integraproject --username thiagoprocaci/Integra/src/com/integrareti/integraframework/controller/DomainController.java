package com.integrareti.integraframework.controller;

import java.util.List;

import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.service.integra.IntegraDomainServiceInterface;

/**
 * This class gives to groupEditWindow, groupImportWindow (view) the access to
 * bussiness
 * 
 * @author Thiago
 * 
 */
public class DomainController {
	private IntegraDomainServiceInterface domainService;

	/**
	 * Creates a new DomainController
	 * 
	 * @param domainService
	 */
	public DomainController(IntegraDomainServiceInterface domainService) {
		this.domainService = domainService;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns a domain by name
	 */
	public Domain getDomainByName(String domainName) throws Exception {
		List<Domain> domains = domainService.getAllByDomainName(domainName);
		if (domains.isEmpty())
			return null;
		return domains.get(0);
	}
}
