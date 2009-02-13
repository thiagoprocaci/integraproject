package com.integrareti.integraframework.ui.zk.window.adm.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.integrareti.integraframework.authentication.UserGroup;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.controller.adm.user.UserEditController;
import com.integrareti.integraframework.exceptions.DeleteUserException;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AnnotateDataBinderWindow;

/**
 * Class that manipulates userEditWindow.zul
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class UserEditWindow extends AnnotateDataBinderWindow {

	// controller
	private UserEditController userEditController;

	// bind variables
	private List<Person> searchedPeople;
	private List<Group> groups;
	private List<Group> groupsSearched;
	private Person selectedPerson;
	private String userGroups;
	private boolean admin;

	// view components
	private Listbox lbxUsers;
	private Listbox lbxGroups;
	private List<Component> personBindComponents;
	private Grid userGrid;
	private Vbox vBoxButtons;

	// business variables
	private Set<Group> removedGroups;
	private Set<Group> addedGroups;

	@Override
	public void doOnCreate() {
		userEditController = (UserEditController) SpringUtil
				.getBean("userEditController");
		// bind variables
		searchedPeople = null;
		userGroups = null;
		groups = null;
		groupsSearched = null;
		selectedPerson = null;
		admin = false;
		// view components
		lbxUsers = (Listbox) getFellow("lbxUsers");
		userGrid = (Grid) getFellow("userGrid");
		lbxGroups = (Listbox) getFellow("lbxGroups");
		vBoxButtons = (Vbox) getFellow("vBoxButtons");
		personBindComponents = new ArrayList<Component>(0);
		personBindComponents.add(getFellow("name"));
		personBindComponents.add(getFellow("registry"));
		personBindComponents.add(getFellow("email"));
		personBindComponents.add(getFellow("lblUserGroups"));
		personBindComponents.add(getFellow("lbxGroups"));
		personBindComponents.add(getFellow("cbxAdmin"));
		personBindComponents.add(getFellow("deleteWinConfimation").getFellow(
				"lbldeleteWin"));
		// business variables
		removedGroups = new HashSet<Group>(0);
		addedGroups = new HashSet<Group>(0);
	}

	@Override
	public void doBeforeBind() {
		getBindObjects().put("person", selectedPerson);
		getBindObjects().put("users", searchedPeople);
		getBindObjects().put("userGroups", userGroups);
		getBindObjects().put("groups", groups);
		getBindObjects().put("admin", admin);
		getBindObjects().put("groupsSearched", groupsSearched);
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
	 * Searches person
	 */
	public void searchPerson() {
		clearHtmlWarnings("warning");
		String registry = ((Textbox) getFellow("registryTextBox")).getText()
				.trim();
		if (StringUtils.hasText(registry)) {
			try {
				selectedPerson = userEditController
						.getPersonByRegistry(registry);
				if (selectedPerson != null) {
					searchedPeople = null;
					groups = userEditController.getPersonGroups(selectedPerson);
					upDateUserGroups();
					updateBoundComponents(personBindComponents);
					lbxUsers.setVisible(false);
					userGrid.setVisible(true);
					lbxGroups.setVisible(true);
					vBoxButtons.setVisible(true);
				} else {
					lbxUsers.setVisible(false);
					userGrid.setVisible(false);
					lbxGroups.setVisible(false);
					vBoxButtons.setVisible(false);
					addHtmlWarning("warning", "Nenhum resultado encontrado",
							"", HtmlWarning.WARNING);
				}
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
			}
		} else {
			String name = ((Textbox) getFellow("nameTextBox")).getText().trim();
			if (StringUtils.hasText(name)) {
				try {
					searchedPeople = userEditController.getPersonByName(name);
					if (!searchedPeople.isEmpty()) {
						if (searchedPeople.size() != 1) {
							updateBoundComponent(lbxUsers);							
							lbxUsers.setVisible(true);
							userGrid.setVisible(false);
							lbxGroups.setVisible(false);
							vBoxButtons.setVisible(false);
						} else {
							selectedPerson = searchedPeople.get(0);
							groups = userEditController.getPersonGroups(selectedPerson);
							upDateUserGroups();
							updateBoundComponents(personBindComponents);
							lbxUsers.setVisible(false);
							userGrid.setVisible(true);
							lbxGroups.setVisible(true);
							vBoxButtons.setVisible(true);
						}
					} else {
						lbxUsers.setVisible(false);
						userGrid.setVisible(false);
						lbxGroups.setVisible(false);
						vBoxButtons.setVisible(false);
						addHtmlWarning("warning",
								"Nenhum resultado encontrado", "",
								HtmlWarning.WARNING);
					}
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
				}
			} else {
				lbxUsers.setVisible(false);
				userGrid.setVisible(false);
				lbxGroups.setVisible(false);
				vBoxButtons.setVisible(false);
				addHtmlWarning("warning", "Informe o nome ou a matricula", "",
						HtmlWarning.WARNING);
			}
		}
		// clear textboxs
		((Textbox) getFellow("registryTextBox")).setRawValue(null);
		((Textbox) getFellow("nameTextBox")).setRawValue(null);
	}

	/**
	 * Executed on check lbxUsers
	 */
	public void onSelectListBoxUsers() {
		// getting the selected person at listBox
		selectedPerson = (Person) lbxUsers.getSelectedItem().getValue();
		searchedPeople = null;
		try {
			groups = userEditController.getPersonGroups(selectedPerson);
		} catch (Exception e) {
			e.printStackTrace();
			showDataBaseMessageError();
			return;
		}
		upDateUserGroups();
		updateBoundComponents(personBindComponents);
		userGrid.setVisible(true);
		lbxGroups.setVisible(true);
		vBoxButtons.setVisible(true);
	}

	/**
	 * Searches groups
	 */
	public void searchGroups() {
		String clue = ((Textbox) getFellow("groupSearchTextBox")).getText()
				.trim();
		// no text
		if (!StringUtils.hasText(clue)) {
			addHtmlWarning("warning",
					"Informe alguma dica para a busca de grupos", "",
					HtmlWarning.WARNING);
			return;
		}
		try {
			groupsSearched = userEditController.getGroupsByClue(clue);
			for (Group group : groups)
				if (groupsSearched.contains(group)) {
					groupsSearched.remove(group);
					if (groupsSearched.isEmpty())
						break;
				}
			if (groupsSearched.isEmpty())
				addHtmlWarning("warning", "Nenhum resultado encontrado", "",
						HtmlWarning.WARNING);
			else
				initPopupWinGroups();
		} catch (Exception e) {
			e.printStackTrace();
			showDataBaseMessageError();
			return;
		}

	}

	/**
	 * Deletes a person
	 */
	public void delete() {
		try {
			userEditController.delete(selectedPerson);
			addHtmlWarning("warning", "Usuário excluido com sucesso", "",
					HtmlWarning.INFORMATION);
		} catch (Exception e) {
			if (e instanceof DeleteUserException)
				addHtmlWarning("warning", "Você não pode se excluir", "",
						HtmlWarning.WARNING);
			else {
				e.printStackTrace();
				showDataBaseMessageError();
			}
		}
		lbxUsers.setVisible(false);
		userGrid.setVisible(false);
		lbxGroups.setVisible(false);
		vBoxButtons.setVisible(false);
		Window win = (Window) getFellow("deleteWinConfimation");
		win.doEmbedded();
		win.setVisible(false);
		Events.sendEvent(new Event("onGroupUpdate", Path
				.getComponent("//main/menuWindow/myGroups")));
	}

	/**
	 * Saves person
	 */
	public void save() {
		if (((Checkbox) getFellow("cbxAdmin")).isChecked()) {
			if (!selectedPerson.getUserGroups().contains(
					new UserGroup(UserGroup.DOMAIN_ADMIN_GROUP))) {
				try {
					selectedPerson
							.getUserGroups()
							.addAll(
									userEditController
											.getUserGroupByName(UserGroup.DOMAIN_ADMIN_GROUP));
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
			}
		} else
			selectedPerson.getUserGroups().remove(
					new UserGroup(UserGroup.DOMAIN_ADMIN_GROUP));
		try {
			Map<String, Group> errors = userEditController.save(selectedPerson,
					removedGroups, addedGroups);
			if (!errors.isEmpty())
				showDataBaseMessageError();
		} catch (Exception e) {
			e.printStackTrace();
			showDataBaseMessageError();
		}
		lbxUsers.setVisible(false);
		userGrid.setVisible(false);
		lbxGroups.setVisible(false);
		vBoxButtons.setVisible(false);
		Events.sendEvent(new Event("onGroupUpdate", Path
				.getComponent("//main/menuWindow/myGroups")));
	}

	/**
	 * Removes user from groupss
	 */
	@SuppressWarnings("unchecked")
	public void removeGroups() {
		try {
			Set<Listitem> listItens = lbxGroups.getSelectedItems();
			List<Group> personGroups = userEditController
					.getPersonGroups(selectedPerson);
			for (Listitem listitem : listItens) {
				Group g = (Group) listitem.getValue();
				groups.remove(g);
				addedGroups.remove(g);
				if (personGroups.contains(g))
					removedGroups.add(g);

			}
		} catch (Exception e) {
			e.printStackTrace();
			showDataBaseMessageError();
		}
		updateBoundComponent(lbxGroups);
	}

	// begin - popupWin groups methods

	/**
	 * Sets visible false popupWin groups - close the popup window
	 */
	public void popupWinGroupsOnCancel() {
		Window popupWin = (Window) getFellow("popupWinGroups");
		popupWin.doEmbedded();
		popupWin.setVisible(false);
	}

	/**
	 * Initialize the popupWin groups - "onAppear window"
	 */
	public void initPopupWinGroups() {
		Window popupWin = (Window) getFellow("popupWinGroups");
		popupWin.setWidth("700px");
		updateBoundComponent(popupWin.getFellow("lbxGroupsSearched"));
		popupWin.doHighlighted();
	}

	/**
	 * On ok action popupWin groups
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void popupWinGroupsOnOk() {
		Window popupWin = (Window) getFellow("popupWinGroups");
		try {
			List<Group> personGroups = userEditController
					.getPersonGroups(selectedPerson);
			Set<Listitem> listItens = ((Listbox) popupWin
					.getFellow("lbxGroupsSearched")).getSelectedItems();
			for (Listitem listitem : listItens) {
				Group g = (Group) listitem.getValue();
				groups.add(g);
				removedGroups.remove(g);
				if (!personGroups.contains(g))
					addedGroups.add(g);
			}
			updateBoundComponent(lbxGroups);
		} catch (Exception e) {
			showDataBaseMessageError();
			e.printStackTrace();
		}
		popupWinGroupsOnCancel();
	}

	// end - popupWin groups methods

	/**
	 * updates the bind variable - userGroups
	 */
	private void upDateUserGroups() {
		admin = false;
		StringBuilder builder = new StringBuilder();
		for (Iterator<UserGroup> iterator = selectedPerson.getUserGroups()
				.iterator(); iterator.hasNext();) {
			UserGroup userGroup = iterator.next();
			builder.append(userGroup.getName());
			if (iterator.hasNext())
				builder.append(", ");
			if (userGroup.getName().equals(UserGroup.DOMAIN_ADMIN_GROUP))
				admin = true;
		}
		userGroups = builder.toString();
	}

	/**
	 * Shows a dataBase message error
	 */
	private void showDataBaseMessageError() {
		addHtmlWarning(
				"warning",
				"O sistema identificou uma falha de banco de dados. Tente novamente mais tarde ou aguarde por reparos",
				"", HtmlWarning.ERROR);
	}

}
