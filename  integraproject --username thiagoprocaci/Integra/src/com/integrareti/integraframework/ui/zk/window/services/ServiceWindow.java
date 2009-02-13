package com.integrareti.integraframework.ui.zk.window.services;

import org.acegisecurity.context.SecurityContextHolder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Include;

import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.controller.teacher.IceAppsController;
import com.integrareti.integraframework.service.google.sso.SingleSignOn;
import com.integrareti.integraframework.ui.zk.form.AcsForm;
import com.integrareti.integraframework.ui.zk.form.IceForm;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AbstractWindow;

/**
 * Class that manipulates googleServices.zul
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class ServiceWindow extends AbstractWindow {
	private IceAppsController iceAppsController;
	private IceForm iceForm;
	private AcsForm acsForm;
	private String relayState;
	private String domainName;
	private String googleAccount;
	private String registry;

	/**
	 * Executed on create window
	 */
	public void onCreate() {
		relayState = null;
		domainName = ((Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDomain().getName();
		googleAccount = ((Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getGoogleAccount();
		registry = ((Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRegistry();
		acsForm = new AcsForm(domainName);
		appendChild(acsForm);
		iceForm = new IceForm();
		appendChild(iceForm);
		iceAppsController = (IceAppsController) SpringUtil.getBean("iceAppsController");
	}

	/**
	 * Onclick mail
	 */
	public void onClickMail() {
		relayState = AcsForm.GOOGLE_MAIL_BASE_URL + domainName;
		acsForm.submitFormInAnotherWin(SingleSignOn.getInstance().getAuthSSO(domainName, googleAccount), relayState);
	}

	/**
	 * onClick Sites
	 */
	public void onClickSites() {
		relayState = AcsForm.GOOGLE_SITES_BASE_URL + domainName;
		acsForm.submitFormInAnotherWin(SingleSignOn.getInstance().getAuthSSO(domainName, googleAccount), relayState);
	}

	/**
	 * Onclick calendar
	 */
	public void onClickCalendar() {
		relayState = AcsForm.GOOGLE_CALENDAR_BASE_URL + domainName;
		acsForm.submitFormInAnotherWin(SingleSignOn.getInstance().getAuthSSO(domainName, googleAccount), relayState);
	}

	/**
	 * Onclick docs
	 */
	public void onClickDocs() {
		relayState = AcsForm.GOOGLE_DOCS_BASE_URL + domainName;
		acsForm.submitFormInAnotherWin(SingleSignOn.getInstance().getAuthSSO(domainName, googleAccount), relayState);
	}

	/**
	 * Onclick home
	 */
	public void onClickHome() {
		Include inc = (Include) getRoot().getFellow("include");
		inc.setSrc("/html/start.html");
	}

	/**
	 * onClick iceApps
	 */
	public void onClickIceApps() {
		try {
			String pass = iceAppsController.getPersonPassword(registry);
			iceForm.submitForm(registry, pass);
		} catch (Exception e) {
			addHtmlWarning("warning", "Ocorreu um erro ao tentar acessar os aplicativos ICE", "", HtmlWarning.ERROR);
			e.printStackTrace();
		}
	}
}
