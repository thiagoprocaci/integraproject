package com.integrareti.integraframework.ui.zk.window.adm.user;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.controller.account.GoogleAccountController;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AnnotateDataBinderWindow;
import com.integrareti.integraframework.valueobject.PersonVO;

/**
 * Class that manipulates userCreateWindow.zul
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class UserCreateWindow extends AnnotateDataBinderWindow {

	// controller
	private GoogleAccountController googleAccountController;

	// bind variables
	private List<String> usernames;
	private PersonVO personVO;
	private String selectedUsername;
	private List<Domain> domains;

	// view compoments
	private Grid googleAccountGrid;
	private Window usernamesWindow;
	private Button btnSave;
	private Listbox domainListBox;
	private List<Component> components;

	@Override
	public void doOnCreate() {
		googleAccountController = (GoogleAccountController) SpringUtil
				.getBean("googleAccountController");
		googleAccountGrid = (Grid) getFellow("googleAccountGrid");
		usernamesWindow = (Window) getFellow("usernamesWindow");
		btnSave = (Button) getFellow("btnSave");
		domainListBox = (Listbox) getFellow("domainListBox");
		components = new ArrayList<Component>(4);
		components.add(getFellow("name"));
		components.add(getFellow("course"));
		components.add(getFellow("registry"));
		components.add(usernamesWindow.getFellow("usernames"));
		try {
			domains = googleAccountController.getAllDomains();
		} catch (Exception e) {
			addHtmlWarning("warning", "Erro ao tentar buscar domínios", "",
					HtmlWarning.ERROR);
			e.printStackTrace();
		}
	}

	@Override
	public void doBeforeBind() {
		getBindObjects().put("usernames", usernames);
		getBindObjects().put("person", personVO);
		getBindObjects().put("domains", domains);
	}

	/**
	 * Sets the page to create a new user
	 * 
	 * @param e
	 */
	public void onNewUser(Event e) {
		((Include) Executions.getCurrent().getDesktop().getPage("main")
				.getFellow("include"))
				.setSrc("/zul/secure/adm/users/userCreateWindow.zul");
	}

	/**
	 * Sets the page to edit a user
	 * 
	 * @param e
	 */
	public void onEditUser(Event e) {
		((Include) Executions.getCurrent().getDesktop().getPage("main")
				.getFellow("include"))
				.setSrc("/zul/secure/adm/users/userEditWindow.zul");
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
	 * @return Returns selected domain
	 */
	public Domain getSelectedDomain() {
		Listitem listItem = domainListBox.getSelectedItem();
		return (Domain) listItem.getValue();
	}

	/**
	 * 
	 * @return Retuns usernames
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUsernames() {
		return (List<String>) getBindObjects().get("usernames");
	}

	/**
	 * Searches an user
	 */
	@SuppressWarnings("unchecked")
	public void searchUser() {
		String registry = ((Textbox) getFellow("registryTextBox")).getText()
				.trim();
		personVO = null;
		// checking if person is saved
		try {
			if (googleAccountController.isPersonSaved(registry) != null) {
				addHtmlWarning("warning", "Usuário já cadastrado", "",
						HtmlWarning.WARNING);
				googleAccountGrid.setVisible(false);
				usernamesWindow.setVisible(false);
				btnSave.setVisible(false);
			} else {
				// checking person exist
				personVO = googleAccountController
						.getPersonBasicsData(registry);
				if (personVO == null) {
					addHtmlWarning("warning", "Usuário não encontrado", "",
							HtmlWarning.WARNING);
					googleAccountGrid.setVisible(false);
					usernamesWindow.setVisible(false);
					btnSave.setVisible(false);
				} else {
					// random password
					domainListBox.setSelectedIndex(0);
					personVO.setPassword(new GregorianCalendar().getTime()
							.toString());
					personVO.setDomain(googleAccountController
							.getDomain(personVO.getSector()));
					if (personVO.getDomain() == null)
						personVO.setDomain((Domain) domainListBox
								.getSelectedItem().getValue());
					else {
						// setting the domain at domainListBox
						List<Listitem> list = domainListBox.getItems();
						for (Listitem listitem : list) {
							if (personVO.getDomain().equals(
									(Domain) listitem.getValue())) {
								domainListBox.setSelectedItem(listitem);
								break;
							}
						}
					}
					usernames = googleAccountController.validUsernames(
							googleAccountController
									.getPossiblesUsernames(personVO), personVO
									.getDomain().getName());

					clearHtmlWarnings("warning");
					updateBoundComponents(components);
					googleAccountGrid.setVisible(true);
					usernamesWindow.setVisible(true);
					btnSave.setVisible(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			addHtmlWarning("warning",
					"Ocorreu um erro ao tentar acessar o banco de dados", "",
					HtmlWarning.ERROR);
			googleAccountGrid.setVisible(false);
			usernamesWindow.setVisible(false);
			btnSave.setVisible(false);
		}
	}

	/**
	 * Sets the valids usernames by domain
	 */
	public void onChangeDomainListBox() {
		// not implemented - only ice domaiin
	}

	/**
	 * Creates a new user
	 */
	public void save() {
		if (selectedUsername == null)
			addHtmlWarning("warning", "Escolha um nome de usuário.", "",
					HtmlWarning.WARNING);
		else {
			personVO.setGoogleAccount(selectedUsername);
			personVO.setDomain(getSelectedDomain());
			try {
				googleAccountController.save(personVO);
				addHtmlWarning("warning", "Usuário criado com sucesso", "",
						HtmlWarning.INFORMATION);
				personVO = null;
			} catch (Exception e) {
				addHtmlWarning("warning",
						"Erro ao tentar salvar. Tente novamente mais tarde",
						"", HtmlWarning.ERROR);
				e.printStackTrace();
			}
			googleAccountGrid.setVisible(false);
			usernamesWindow.setVisible(false);
			btnSave.setVisible(false);
		}
	}

}
