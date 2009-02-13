package com.integrareti.integraframework.ui.zk.window.signin;

import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zkplus.spring.SpringUtil;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainErrorCode;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.integrareti.integraframework.controller.account.GoogleAccountController;
import com.integrareti.integraframework.service.google.sso.SingleSignOn;
import com.integrareti.integraframework.ui.zk.form.AcsForm;
import com.integrareti.integraframework.ui.zk.window.AnnotateDataBinderWindow;
import com.integrareti.integraframework.valueobject.PersonVO;

/**
 * Class that manipulates googleAccount.zul
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class CreateAccountWindow extends AnnotateDataBinderWindow {
	private GoogleAccountController googleAccountController;
	private List<String> list;
	private PersonVO personVO;
	private String selectedUsername;

	@Override
	public void doOnCreate() {
		googleAccountController = (GoogleAccountController) SpringUtil.getBean("googleAccountController");
		personVO = (PersonVO) Sessions.getCurrent().getAttribute("personVO");
		try {
			// setting domain
			personVO.setDomain(googleAccountController.getDomain(personVO.getSector()));
			this.list = googleAccountController.validUsernames(googleAccountController.getPossiblesUsernames(personVO), personVO.getDomain().getName());
		} catch (Exception e) {
			setPermanentWarnning("Erro. Tente novamente mais tarde");
			e.printStackTrace();
		}
	}

	@Override
	public void doBeforeBind() {
		getBindObjects().put("usernames", list);
		getBindObjects().put("person", personVO);
	}

	/**
	 * Saves a person with a google account
	 */
	public void save() {
		if (selectedUsername == null)
			throw new WrongValueException(getFellow("usernamesWindow").getFellow("usernames"), "Escolha um nome de usuário.");
		personVO.setGoogleAccount(getSelectedUsername());
		try {
			googleAccountController.save(personVO);
		} catch (AppsForYourDomainException e) {
			if (e.getErrorCode().equals(AppsForYourDomainErrorCode.EntityExists))
				setPermanentWarnning("Ocorreu um erro [" + e.getErrorCode() + "]. O usuário selecionado já foi cadastrado.");
			return;
		} catch (Exception e) {
			setPermanentWarnning("Erro ao tentar salvar. Tente novamente mais tarde");
			e.printStackTrace();
			return;
		}
		AcsForm acsFrom = (AcsForm) getRoot().getFellow("acsFrom");
		try {
			acsFrom.setSamlResponseTextAreaValue(SingleSignOn.getInstance().getAuthSSO(personVO.getDomain().getName(), selectedUsername));
		} catch (Exception e) {
			setPermanentWarnning("Erro. Tente novamente mais tarde");
			e.printStackTrace();
			return;
		}
		acsFrom.submitForm();
	}

	/**
	 * Sets the selectedUsername
	 * 
	 * @param selectedUsername
	 */
	public void setSelectedUsername(String selectedUsername) {
		this.selectedUsername = selectedUsername;
	}

	/**
	 * 
	 * @return Returns the selected username
	 */
	public String getSelectedUsername() {
		return this.selectedUsername;
	}

	/**
	 * 
	 * @return Retuns usernames
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUsernames() {
		return (List<String>) getBindObjects().get("usernames");
	}
}
