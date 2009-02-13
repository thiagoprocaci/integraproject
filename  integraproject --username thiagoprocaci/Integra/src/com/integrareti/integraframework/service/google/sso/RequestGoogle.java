package com.integrareti.integraframework.service.google.sso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.integrareti.integraframework.exceptions.SamlException;
import com.integrareti.integraframework.util.RequestUtil;
import com.integrareti.integraframework.util.SsoUtil;

/**
 * Class that builds a google request
 * 
 * @author Thiago Baêsso Procaci
 * 
 */
public class RequestGoogle {

	// private constants
	private static final RequestGoogle INSTANCE = new RequestGoogle();
	private static final String AUTHN_REQUEST_TEMPLATE = "AuthnRequestTemplate.xml";
	private static final String PROVIDER_NAME = "google.com";

	/**
	 * Empty Contructor
	 */
	private RequestGoogle() {

	}

	/**
	 * 
	 * @return Returns a instance of RequestGoogle
	 */
	public static RequestGoogle getInstance() {
		return INSTANCE;
	}

	/**
	 * @throws SamlException
	 * @return Returns a new RequestGoogle
	 */
	public String getRequestGoogle(String domainName) throws SamlException {

		// creates a new request
		String acsURI = "https://www.google.com/a/" + domainName + "/acs";
		// Create the AuthnRequest XML from above parameters
		String authnRequest = createAuthnRequest(acsURI, PROVIDER_NAME);
		// Compute URL to forward AuthnRequest to the Identity Provider
		String samlRequest = encodeAuthnRequest(authnRequest);
		return samlRequest;
	}

	/**
	 * Generates a SAML AuthnRequest XML by replacing the specified ACS URL and
	 * provider name in the SAML AuthnRequest template file. Returns the string
	 * format of the XML file.
	 * 
	 * @param acsURL
	 * @param providerName
	 * @return
	 * @throws SamlException
	 */
	private String createAuthnRequest(String acsURL, String providerName)
			throws SamlException {

		String file_path = "";
		try {
			file_path = "/"+ URLDecoder.decode(getClass().getResource(
			"/com/integrareti/integraframework/service/google/sso/template/").toString()
			+ AUTHN_REQUEST_TEMPLATE, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		file_path = file_path.replace("file:/", "");
		
		String authnRequest = SsoUtil.readFileContents(file_path);
		authnRequest = authnRequest.replace("<PROVIDER_NAME>", providerName);
		authnRequest = authnRequest.replace("<ACS_URL>", acsURL);
		authnRequest = authnRequest.replace("<AUTHN_ID>", SsoUtil.createID());
		authnRequest = authnRequest.replace("<ISSUE_INSTANT>", SsoUtil
				.getDateAndTime());
		return authnRequest;
	}

	/**
	 * Encodes the authnRequest
	 * 
	 * @param authnRequest
	 * @return
	 * @throws SamlException
	 */
	private String encodeAuthnRequest(String authnRequest) throws SamlException {
		try {
			return RequestUtil.encodeMessage(authnRequest);
		} catch (UnsupportedEncodingException e) {
			throw new SamlException(
					"Error encoding SAML Request into URL: Check encoding scheme - "
							+ e.getMessage());
		} catch (IOException e) {
			throw new SamlException(
					"Error encoding SAML Request into URL: Check encoding scheme - "
							+ e.getMessage());
		}
	}

}
