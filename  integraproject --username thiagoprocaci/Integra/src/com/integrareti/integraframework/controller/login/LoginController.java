package com.integrareti.integraframework.controller.login;

import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.google.GoogleUserAccountServiceInterface;
import com.integrareti.integraframework.service.siga.SigaService;

/**
 * This class gives to login (view) the access to business components
 * 
 * @author Thiago
 * 
 */
public class LoginController {
	private SigaService sigaService;
	private GoogleUserAccountServiceInterface personService;

	/**
	 * Creates a new LoginController
	 * 
	 * @param sigaService
	 * @param personService
	 */
	public LoginController(SigaService sigaService, GoogleUserAccountServiceInterface personService) {
		this.sigaService = sigaService;
		this.personService = personService;
	}

	/**
	 * Authentication at integra
	 * 
	 * @param username
	 * @param password
	 * @return Returns
	 * @throws Exception
	 */
	public Person integraLogin(String username, String password) throws Exception {
		Person person = personService.getByRegistry(username);
		if (person != null) {
			if (sigaLogin(username, password))
				return person;
		}
		return null;
	}

	/**
	 * Authentication at siga
	 * 
	 * @param username
	 * @param password
	 * @return true or false
	 * @throws Exception
	 */
	public boolean sigaLogin(String username, String password) throws Exception {
		return sigaService.loginPerson(username, password);
	}
}
