package com.integrareti.integraframework.service.google.sso;

import com.integrareti.integraframework.exceptions.SamlException;

/**
 * Builds samlResponse
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class SingleSignOn {

	private static final SingleSignOn INSTANCE = new SingleSignOn();

	/**
	 * Empty constructor
	 */
	private SingleSignOn() {}

	/**
	 * 
	 * 
	 * @return Returns an instance of SingleSignOn
	 */
	public static SingleSignOn getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * 
	 * @param domainName
	 * @param username
	 *            (googleAccount)
	 * @param password
	 *            (googlePassword)
	 * @return Returns samlResponse
	 */
	public String getAuthSSO(String domainName, String username) {
		try {
			RequestGoogle requestGoogle = RequestGoogle.getInstance();
			String SAMLReqst = requestGoogle.getRequestGoogle(domainName);
			ProcessResponseGoogle processResponseGoogle = ProcessResponseGoogle
					.getInstance();
			String samlResponse = processResponseGoogle.getSAMLResponse(
					SAMLReqst, username);
			return samlResponse;
		} catch (SamlException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
