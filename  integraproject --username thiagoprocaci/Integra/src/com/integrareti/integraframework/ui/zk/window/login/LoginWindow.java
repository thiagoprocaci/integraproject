package com.integrareti.integraframework.ui.zk.window.login;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.controller.login.LoginController;
import com.integrareti.integraframework.service.google.sso.SingleSignOn;
import com.integrareti.integraframework.ui.zk.form.AcsForm;

/**
 * Class that manipulates login.zul
 * 
 * @author Thiago
 * 
 */
@SuppressWarnings("serial")
public class LoginWindow extends Window {
	private LoginController loginController;
	private String relayState;
	private AcsForm acsForm;
	private Textbox usernameTB, passwordTB;
	private Label warnning;
	private boolean accessDenied;

	/**
	 * Executed on create window
	 */
	public void onCreate() {
		loginController = (LoginController) SpringUtil.getBean("loginController");
		relayState = getDesktop().getExecution().getParameter("RelayState");
		accessDenied = getDesktop().getExecution().getParameter("accessDenied") != null;
		usernameTB = (Textbox) getFellow("username");
		passwordTB = (Textbox) getFellow("password");
		warnning = (Label) getFellow("warnning");
		acsForm = new AcsForm(getDesktop().getExecution().getParameter("domain"));
		this.appendChild(acsForm);
		if (accessDenied) {
			warnning.setValue("Acesso negado");
			warnning.setVisible(true);
		}
	}

	/**
	 * Do login
	 */
	public void login() {
		Clients.showBusy("Executando login...", true);
		Person loggedPerson = null;
		try {
			loggedPerson = loginController.integraLogin(usernameTB.getText(), passwordTB.getText());
		} catch (WrongValueException e1) {
			Clients.showBusy(null, false);
			warnning.setValue("Login falhou");
			return;
		} catch (Exception e1) {
			Clients.showBusy(null, false);
			warnning.setValue("Login falhou");
			e1.printStackTrace();
			return;
		}
		if (loggedPerson == null) {
			Clients.showBusy(null, false);
			warnning.setValue("Login inválido");
			warnning.setVisible(true);
			passwordTB.setRawValue(null);
		} else {
			if (relayState != null) {
				acsForm.submitForm(SingleSignOn.getInstance().getAuthSSO(loggedPerson.getDomain().getName(), loggedPerson.getGoogleAccount()), relayState);
			} else {
				Clients.submitForm("loginForm");
			}
		}
	}
}
