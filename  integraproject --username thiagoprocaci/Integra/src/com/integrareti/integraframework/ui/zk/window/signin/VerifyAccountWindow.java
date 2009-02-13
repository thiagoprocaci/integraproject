package com.integrareti.integraframework.ui.zk.window.signin;

import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Textbox;

import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.controller.account.GoogleAccountController;
import com.integrareti.integraframework.controller.login.LoginController;
import com.integrareti.integraframework.ui.zk.form.AcsForm;
import com.integrareti.integraframework.ui.zk.window.AbstractWindow;
import com.integrareti.integraframework.valueobject.PersonVO;

/**
 * Class that manipulates index.zul
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class VerifyAccountWindow extends AbstractWindow {
	private GoogleAccountController createAccountController;
	private PersonVO personVO;
	private Textbox username;
	private Textbox password;
	private Component createWindow;
	private Component verifyWindow;
	private AcsForm acsForm;
	private LoginController loginController;

	/**
	 * Executed on create
	 */
	public void onCreate() {
		// init variables
		createAccountController = (GoogleAccountController) SpringUtil.getBean("googleAccountController");
		loginController = (LoginController) SpringUtil.getBean("loginController");
		personVO = null;
		verifyWindow = getFellow("loginWindow"); // direct child
		username = (Textbox) verifyWindow.getFellow("username");// indirect
		// child
		password = (Textbox) verifyWindow.getFellow("password");
		String domain = getDesktop().getExecution().getParameter("domain");
		if (domain == null)
			throw new RuntimeException("The domain must be set as url parameter 'domain' ");
		acsForm = new AcsForm(domain);
		acsForm.setId("acsFrom");
		appendChild(acsForm);
	}

	/**
	 * Authentication at system
	 */
	public void verify() {
		if (!isFieldsValid()) {
			setPermanentWarnning("Login inválido - Tente novamente");
			password.setRawValue(null);
			return;
		} else
			try {
				if (!loginController.sigaLogin(username.getText().trim(), password.getText())) {
					setPermanentWarnning("Login inválido - Tente novamente");
					password.setRawValue(null);
				} else {
					if (createAccountController.isPersonSaved(username.getText().trim()) != null) {
						setPermanentWarnning("Usuário já cadastrado.");
					} else {
						// login ok
						// getting the person's data
						setPermanentWarnning(null);
						personVO = createAccountController.getPersonBasicsData(username.getText().trim());
						personVO.setRegistry(username.getText().trim());
						personVO.setPassword(password.getText());
						Domain domain = createAccountController.getDomain(personVO.getSector());
						if (domain == null) {
							domain = createAccountController.getDomainByName("ice.ufjf.br");
							setPermanentWarnning("Usuário não tem vinculo com o ICE.");
							return;
						}
						acsForm.setRelayStateByDomain(domain.getName());
						Sessions.getCurrent().setAttribute("personVO", personVO);
						verifyWindow.setVisible(false);
						if (createWindow != null)
							createWindow.detach();
						// first time at system
						createWindow = Executions.createComponents("/zul/signin/createAccountWindow.zul", this, null);
						this.appendChild(createWindow);
					}
				}
			} catch (WrongValueException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * Validades the fields username and password
	 * 
	 * @return true or false
	 */
	private boolean isFieldsValid() {
		if (!StringUtils.hasText(username.getText()))
			return false;
		if (!StringUtils.hasText(password.getText()))
			return false;
		return true;
	}
}
