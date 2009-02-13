package com.integrareti.integraframework.ui.zk.form;

import org.zkoss.zhtml.Form;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.util.Clients;

/**
 * This class describes the form that will be submit to google.
 * 
 * This form has two components (TextAreas - samlResponseTextArea and
 * relayStateTextArea )
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class AcsForm extends Form {
	// public constants
	public static final String GOOGLE_LOGIN_ACTION = "https://www.google.com/a/";
	public static final String GOOGLE_MAIL_BASE_URL = "https://mail.google.com/a/";
	public static final String GOOGLE_CALENDAR_BASE_URL = "http://calendar.google.com/a/";
	public static final String GOOGLE_DOCS_BASE_URL = "http://docs.google.com/a/";
	public static final String GOOGLE_PARTNER_PAGE_BASE_URL = "http://partnerpage.google.com/";
	public static final String GOOGLE_SITES_BASE_URL = "http://sites.google.com/a/";
	private Textarea samlResponseTextArea;
	private Textarea relayStateTextArea;

	/**
	 * Creates a new AcsForm
	 */
	public AcsForm(String domainName) {
		this.setVisible(false);
		this.setDynamicProperty("method", "POST");
		setFormAction(domainName);
		samlResponseTextArea = new Textarea();
		relayStateTextArea = new Textarea();
		samlResponseTextArea.setDynamicProperty("name", "SAMLResponse");
		relayStateTextArea.setDynamicProperty("name", "RelayState");
		this.appendChild(samlResponseTextArea);
		this.appendChild(relayStateTextArea);
	}

	/**
	 * Sets form action
	 * 
	 * @param domainName
	 */
	public void setFormAction(String domainName) {
		this.setDynamicProperty("action", GOOGLE_LOGIN_ACTION + domainName + "/acs");
	}

	/**
	 * Sets the value of samlResponseTextArea
	 * 
	 * @param value
	 */
	public void setSamlResponseTextAreaValue(String value) {
		samlResponseTextArea.setDynamicProperty("value", value);
	}

	/**
	 * Sets the value of RelayStateTextArea
	 * 
	 * @param relayState
	 */
	public void setRelayState(String relayState) {
		relayStateTextArea.setDynamicProperty("value", relayState);
	}

	/**
	 * Sets the value of RelayStateTextArea
	 * 
	 * @param value
	 */
	public void setRelayStateByDomain(String domainName) {
		if (domainName == null)
			throw new IllegalStateException("The domain name must be specified at relayState");
		String value = GOOGLE_MAIL_BASE_URL + domainName;
		relayStateTextArea.setDynamicProperty("value", value);
	}

	/**
	 * Sets form target
	 * 
	 * @param target
	 */
	public void setTarget(String target) {
		this.setDynamicProperty("target", target);
	}

	/**
	 * Submits this form
	 * 
	 * @param samlResponse
	 * @param relayState
	 *            (googleBaseURL + domainName)
	 */
	public void submitForm(String samlResponse, String relayState) {
		setSamlResponseTextAreaValue(samlResponse);
		setRelayState(relayState);
		submitForm();
	}

	/**
	 * Submits this form in another window
	 * 
	 * @param samlResponse
	 * @param relayState
	 *            (googleBaseURL + domainName)
	 */
	public void submitFormInAnotherWin(String samlResponse, String relayState) {
		setSamlResponseTextAreaValue(samlResponse);
		setRelayState(relayState);
		setTarget("_blank");
		Clients.submitForm(this);
	}

	/**
	 * Submits this form
	 */
	public void submitForm() {
		verifyState();
		Clients.submitForm(this);
	}

	/**
	 * Checks the form
	 */
	private void verifyState() {
		if (relayStateTextArea.getDynamicProperty("value") == null || samlResponseTextArea.getDynamicProperty("value") == null) {
			throw new IllegalStateException("Be sure relayState and samlResponse are not null. Respestively the are: " + relayStateTextArea.getDynamicProperty("value") + " , " + samlResponseTextArea.getDynamicProperty("value"));
		}
	}
}
