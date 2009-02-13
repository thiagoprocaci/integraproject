package com.integrareti.integraframework.controller.teacher;

import com.integrareti.integraframework.service.siga.SigaService;

/**
 * Controller of IceAppsWindow window
 * @author Thiago Baesso Procaci
 *
 */
public class IceAppsController {

	private SigaService sigaService;
	
	/**
	 * Creates a new IceAppsController
	 * @param sigaService
	 */
	public IceAppsController(SigaService sigaService){
		this.sigaService = sigaService;
	}
	
	/**
	 * 
	 * @param registry
	 * @return Returns person password by registry
	 * @throws Exception 
	 */
	public String getPersonPassword(String registry) throws Exception{
		return sigaService.getPersonPassword(registry);
	}
	
}
