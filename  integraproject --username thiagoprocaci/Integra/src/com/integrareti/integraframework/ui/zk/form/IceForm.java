package com.integrareti.integraframework.ui.zk.form;

import org.zkoss.zhtml.Form;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.util.Clients;

/**
 * Class that describes the from that wil be submited to ice website
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class IceForm extends Form {
	private static final String FORM_ACTION = "http://siga.ufjf.br/index.php/common/authmd5";
	private static final String URL_VALUE = "http://www.ice.ufjf.br/integra/valida_login.php";
	private static final String FAIL_URL_VALUE = "http://www.ice.ufjf.br";
	private Textarea user;
	private Textarea pass;
	private Textarea successUrl;
	private Textarea failUrl;

	/**
	 * Creates a new IceForm
	 */
	public IceForm() {
		this.setVisible(false);
		this.setDynamicProperty("method", "POST");
		this.setDynamicProperty("action", FORM_ACTION);
		this.setDynamicProperty("target", "_blank");
		user = new Textarea();
		pass = new Textarea();
		successUrl = new Textarea();
		failUrl = new Textarea();
		user.setDynamicProperty("name", "uid");
		pass.setDynamicProperty("name", "pwd");
		successUrl.setDynamicProperty("name", "return_to_ok");
		failUrl.setDynamicProperty("name", "return_to_fail");
		this.appendChild(user);
		this.appendChild(pass);
		this.appendChild(successUrl);
		this.appendChild(failUrl);
	}

	/**
	 * Submits form
	 * 
	 * @param username
	 * @param password
	 */
	public void submitForm(String username, String password) {
		user.setValue(username);
		pass.setValue(password);
		successUrl.setValue(URL_VALUE);
		failUrl.setValue(FAIL_URL_VALUE);
		Clients.submitForm(this);
	}
}
