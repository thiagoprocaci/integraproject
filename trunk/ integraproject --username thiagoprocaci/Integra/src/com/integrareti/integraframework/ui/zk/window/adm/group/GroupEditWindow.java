package com.integrareti.integraframework.ui.zk.window.adm.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.controller.DomainController;
import com.integrareti.integraframework.controller.adm.group.GroupManagerController;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AnnotateDataBinderWindow;
import com.integrareti.integraframework.valueobject.NameVO;

/**
 * Class that manipulates groupEditWindow.zul
 * 
 * @author Thiago
 * 
 */
@SuppressWarnings("serial")
public class GroupEditWindow extends AnnotateDataBinderWindow {
	// controller
	private GroupManagerController groupManagerController;
	private DomainController domainController;
	// bind - group
	private List<Group> groups;
	private Group selectedGroup;
	private List<Component> gridBindComponents;
	private Listbox groupsListBox;
	// bind - person
	private Person ownerSearched;
	private Person personSearched;
	private List<Component> personBindComponents;
	private List<Component> ownerBindComponents;
	private Set<NameVO> allNameVOsParticipantsAdded;
	private Set<NameVO> allNameVOsParticipantsNotAdded;
	private Set<NameVO> allNameVOsOwnersAdded;
	private Set<NameVO> allNameVOsOwnersNotAdded;
	// boxes
	private Vbox vBoxParticipant;
	private Vbox vBoxOwner;
	private Hbox hBoxButtons, hbxSearch;
	// group descritor grid
	private Grid groupDescriptorGrid;
	// textBoxes
	private Textbox searchGroupTextbox;
	private Textbox searchFilterTextBox;
	private Textbox participantSearchTextBox;
	private Textbox ownerSearchTextBox;
	private Textbox tbxGroupName;
	// label
	private Label lblGroupName;
	private Label lblGroupPrefix;
	// others
	private Set<Person> participantsAdded;
	private Set<Person> participantsRemoved;
	private Set<Person> ownersAdded;
	private Set<Person> ownersRemoved;
	private Person participantAdded;
	private Person participantRemoved;
	private Person ownerAdded;
	private Person ownerRemoved;
	private boolean newGroup;
	private Button btnAtAutomatica;

	@Override
	public void doOnCreate() {
		// getting controller from context
		groupManagerController = (GroupManagerController) SpringUtil.getBean("groupManagerController");
		domainController = (DomainController) SpringUtil.getBean("domainController");
		newGroup = (Executions.getCurrent().getParameter("new") != null);
		groups = null;
		// initializing the viewer components
		groupsListBox = (Listbox) getFellow("lbxGroups");
		lblGroupName = (Label) getFellow("lblGroupName");
		// textbox - group name (code)
		tbxGroupName = (Textbox) getFellow("tbxGroupName");
		// textBoxes - search
		searchFilterTextBox = (Textbox) getFellow("keyWordTextBox");
		searchGroupTextbox = (Textbox) getFellow("descriptionTextBox");
		participantSearchTextBox = (Textbox) getFellow("searchParticipant");
		ownerSearchTextBox = (Textbox) getFellow("searchOwner");
		groupDescriptorGrid = (Grid) getFellow("groupDescriptorGrid");
		gridBindComponents = new ArrayList<Component>(3);
		gridBindComponents.add((Textbox) getFellow("groupDescription"));
		gridBindComponents.add(lblGroupName);
		gridBindComponents.add((Label) getFellow("groupNumberParticipants"));
		gridBindComponents.add((Label) getFellow("groupNumberEmailLists"));
		gridBindComponents.add((Label) ((Window) getFellow("deleteWinConfimation")).getFellow("lbldeleteWin"));
		personBindComponents = new ArrayList<Component>(2);
		personBindComponents.add((Label) getFellow("lblPersonRegistry"));
		personBindComponents.add((Label) getFellow("lblPersonName"));
		ownerBindComponents = new ArrayList<Component>(2);
		ownerBindComponents.add((Label) getFellow("lblOwnerRegistry"));
		ownerBindComponents.add((Label) getFellow("lblOwnerName"));
		// vBoxes
		vBoxParticipant = (Vbox) getFellow("participantDataVBox");
		vBoxOwner = (Vbox) getFellow("ownerDataVBox");
		// hBoxes
		hBoxButtons = (Hbox) getFellow("hBoxButtons");
		hbxSearch = (Hbox) getFellow("hbxSearch");
		// label
		lblGroupPrefix = (Label) getFellow("lblGrupoPrefix");
		// Buttons
		btnAtAutomatica = (Button) getFellow("btnAtAutomatica");
		if (newGroup) {
			this.setTitle("Novo Grupo");
			btnAtAutomatica.setVisible(false);
			lblGroupName.setVisible(false);
			tbxGroupName.setVisible(true);
			hbxSearch.setVisible(false);
			hBoxButtons.setVisible(true);
			// delete button. it`s not necessary on create new group
			getFellow("btnExcluir").setVisible(false);
			// label that will be added to group name
			lblGroupPrefix.setVisible(true);
			groupDescriptorGrid.setVisible(true);
			selectedGroup = new Group();
			selectedGroup.setManuallyCreated(true);
			selectedGroup.setActive(true);
			try {
				selectedGroup.setDomain(domainController.getDomainByName(((Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDomain().getName()));
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
			}
			// collections
			participantsAdded = new HashSet<Person>();
			participantsRemoved = new HashSet<Person>();
			ownersAdded = new HashSet<Person>();
			ownersRemoved = new HashSet<Person>();
			allNameVOsParticipantsAdded = new HashSet<NameVO>(0);
			allNameVOsParticipantsNotAdded = new HashSet<NameVO>(0);
			allNameVOsOwnersAdded = new HashSet<NameVO>(0);
			allNameVOsOwnersNotAdded = new HashSet<NameVO>(0);
		} else {
			selectedGroup = null;
			// delete button. it`s necessary on edit group
			getFellow("btnExcluir").setVisible(true);
		}
	}

	@Override
	public void doBeforeBind() {
		getBindObjects().put("groups", groups);
		getBindObjects().put("group", selectedGroup);
		getBindObjects().put("searchedPerson", personSearched);
		getBindObjects().put("searchedOwner", ownerSearched);
		getBindObjects().put("allNameVOsParticipantsAdded", allNameVOsParticipantsAdded);
		getBindObjects().put("allNameVOsParticipantsNotAdded", allNameVOsParticipantsNotAdded);
		getBindObjects().put("allNameVOsOwnersAdded", allNameVOsOwnersAdded);
		getBindObjects().put("allNameVOsOwnersNotAdded", allNameVOsOwnersNotAdded);
	}

	/**
	 * Sets the page to create a new group
	 * 
	 * @param e
	 */
	public void onNewGroup(Event e) {
		includeNewGroupPage();
	}

	/**
	 * Sets the page to edit a group
	 * 
	 * @param e
	 */
	public void onEditGroup(Event e) {
		includeEditGroupPage();
	}

	/**
	 * Searches a groups at database
	 */
	public void searchGroups() {
		// getting the description
		String description = searchGroupTextbox.getText().trim();
		// getting the filter
		String filter = searchFilterTextBox.getText().trim();
		if (StringUtils.hasText(description)) {
			if (StringUtils.hasText(filter)) {
				// search with filter
				Scanner sc = new Scanner(filter).useDelimiter("\\s* \\s*");
				List<String> list = new ArrayList<String>(3);
				while (sc.hasNext())
					list.add(sc.next());
				try {
					groups = groupManagerController.getGroupsByDescriptionAndName(description, list);
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
			} else
				try {
					// search with no filter - only description
					groups = groupManagerController.getGroupsByDescription(description);
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
			if (groups.isEmpty()) {
				// results not found
				groupsListBox.setVisible(false);
				addHtmlWarning("warning", "Nenhum resultado encontrado", "", HtmlWarning.WARNING);
			} else {
				addHtmlWarning("warning", "Foram encontrados " + groups.size() + " resultado(s)", "", HtmlWarning.INFORMATION);
				groupsListBox.setVisible(true);
			}
		} else {
			if (StringUtils.hasText(filter)) {
				// search only with filter
				Scanner sc = new Scanner(filter).useDelimiter("\\s* \\s*");
				List<String> list = new ArrayList<String>(3);
				while (sc.hasNext())
					list.add(sc.next());
				try {
					groups = groupManagerController.getGroupsByPiecesOfNames(list);
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
				if (groups.isEmpty()) {
					// results not found
					groupsListBox.setVisible(false);
					addHtmlWarning("warning", "Nenhum resultado encontrado", "", HtmlWarning.WARNING);
				} else {
					addHtmlWarning("warning", "Foram encontrados " + groups.size() + " resultado(s)", "", HtmlWarning.INFORMATION);
					groupsListBox.setVisible(true);
				}
			} else {
				// search fails - description is empty
				groups = null;
				groupsListBox.setVisible(false);
				addHtmlWarning("warning", "Informe pelo menos a descrição para a busca", "", HtmlWarning.WARNING);
			}
		}
		// bind variable
		selectedGroup = null;
		participantSearchTextBox.setRawValue(null);
		ownerSearchTextBox.setRawValue(null);
		groupDescriptorGrid.setVisible(false);
		vBoxParticipant.setVisible(false);
		vBoxOwner.setVisible(false);
		hBoxButtons.setVisible(false);
		// rebind components
		List<Component> list = new ArrayList<Component>();
		list.addAll(personBindComponents);
		list.addAll(ownerBindComponents);
		list.add(groupsListBox);
		updateBoundComponents(list);
	}

	/**
	 * Executed on check groups list box
	 */
	public void onSelectGroupsListBox() {
		// getting the selected group at listBox
		selectedGroup = (Group) groupsListBox.getSelectedItem().getValue();
		List<Component> components = new ArrayList<Component>();
		components.addAll(gridBindComponents);
		if (selectedGroup == null) {
			groupDescriptorGrid.setVisible(false);
			hBoxButtons.setVisible(false);
		} else {
			groupManagerController.reattach(selectedGroup);
			groupDescriptorGrid.setVisible(true);
			hBoxButtons.setVisible(true);
			vBoxOwner.setVisible(false);
			vBoxParticipant.setVisible(false);
			participantSearchTextBox.setRawValue(null);
			ownerSearchTextBox.setRawValue(null);
			// bind variables
			participantAdded = null;
			participantRemoved = null;
			ownerAdded = null;
			ownerRemoved = null;
			personSearched = null;
			ownerSearched = null;
			// collections
			participantsAdded = new HashSet<Person>();
			participantsRemoved = new HashSet<Person>();
			ownersAdded = new HashSet<Person>();
			ownersRemoved = new HashSet<Person>();
			allNameVOsParticipantsAdded = new HashSet<NameVO>(0);
			allNameVOsParticipantsNotAdded = new HashSet<NameVO>(0);
			allNameVOsOwnersAdded = new HashSet<NameVO>(0);
			allNameVOsOwnersNotAdded = new HashSet<NameVO>(0);
			components.addAll(personBindComponents);
			components.addAll(ownerBindComponents);
		}
		// rebind
		updateBoundComponents(components);
	}

	/**
	 * Searches a person to add in a group
	 * 
	 */
	public void addPersonSearch() {
		// getting the person registry
		String registry = participantSearchTextBox.getText().trim();
		participantAdded = null;
		participantRemoved = null;
		personSearched = null;
		if (StringUtils.hasText(registry)) {
			try {
				participantAdded = groupManagerController.getPersonByRegistry(registry);
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
				return;
			}
			if (participantAdded != null) { // found
				try {
					participantAdded.setName(groupManagerController.getPersonName(registry, participantAdded.getUserGroups()));
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
				vBoxParticipant.setVisible(true);
				clearHtmlWarnings("warning");
			} else { // not found
				addHtmlWarning("warning", "Usuário não encontrado: " + registry, "", HtmlWarning.WARNING);
				vBoxParticipant.setVisible(false);
			}
		} else {
			// search fails - registry is empty
			addHtmlWarning("warning", "Para adicionar um participante, é necessário informar a matrícula do usuário", "", HtmlWarning.WARNING);
			vBoxParticipant.setVisible(false);
		}
		// rebind
		personSearched = participantAdded;
		updateBoundComponents(personBindComponents);
	}

	/**
	 * Searches a person to remove from a group
	 */
	public void removePersonSearch() {
		// getting the person registry
		String registry = participantSearchTextBox.getText().trim();
		participantAdded = null;
		participantRemoved = null;
		personSearched = null;
		if (StringUtils.hasText(registry)) {
			boolean wasFound = false;
			for (Person person : selectedGroup.getParticipants()) {
				if (person.getRegistry().equals(registry)) {
					participantRemoved = person;
					wasFound = true;
					try {
						participantRemoved.setName(groupManagerController.getPersonName(registry, participantRemoved.getUserGroups()));
					} catch (Exception e) {
						e.printStackTrace();
						showDataBaseMessageError();
						return;
					}
					break;
				}
			}
			if (!wasFound) { // needs to search more
				for (Person person : participantsAdded) {
					if (person.getRegistry().equals(registry)) {
						participantRemoved = person;
						if (!StringUtils.hasText(participantRemoved.getName())) {
							try {
								participantRemoved.setName(groupManagerController.getPersonName(registry, participantRemoved.getUserGroups()));
							} catch (Exception e) {
								e.printStackTrace();
								showDataBaseMessageError();
								return;
							}
						}
						wasFound = true;
						break;
					}
				}
			}
			if (!wasFound) { // not found
				addHtmlWarning("warning", "O usuário " + registry + " não faz parte do grupo " + selectedGroup.getName(), "", HtmlWarning.WARNING);
				vBoxParticipant.setVisible(false);
			} else {
				vBoxParticipant.setVisible(true);
				clearHtmlWarnings("warning");
			}
		} else { // search fails - registry is empty
			addHtmlWarning("warning", "Para remover um participante, é necessário informar a matrícula do usuário", "", HtmlWarning.WARNING);
			vBoxParticipant.setVisible(false);
		}
		// rebind
		personSearched = participantRemoved;
		updateBoundComponents(personBindComponents);
	}

	/**
	 * Confirm adding or removing of a participant in a group
	 */
	public void confirmParticipantToGroup() {
		if (participantAdded != null) {
			participantsAdded.add(participantAdded);
			participantsRemoved.remove(participantAdded);
			participantAdded = null;
		}
		if (participantRemoved != null) {
			participantsRemoved.add(participantRemoved);
			participantsAdded.remove(participantRemoved);
			ownersAdded.remove(participantRemoved);
			if (selectedGroup.getOwners().contains(participantRemoved))
				ownersRemoved.add(participantRemoved);
			participantRemoved = null;
		}
		participantSearchTextBox.setRawValue(null);
		vBoxParticipant.setVisible(false);
		clearHtmlWarnings("warning");
	}

	/**
	 * Cancel adding or removing of a participant in a group
	 */
	public void cancelParticipantToGroup() {
		participantSearchTextBox.setRawValue(null);
		vBoxParticipant.setVisible(false);
	}

	/**
	 * Searches a owner to add in a group
	 */
	public void addOwnerSearch() {
		// getting owner registry
		String registry = ownerSearchTextBox.getText().trim();
		ownerAdded = null;
		ownerRemoved = null;
		ownerSearched = null;
		if (StringUtils.hasText(registry)) {
			try {
				ownerAdded = groupManagerController.getPersonByRegistry(registry);
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
				return;
			}
			if (ownerAdded != null) { // found
				try {
					ownerAdded.setName(groupManagerController.getPersonName(registry, ownerAdded.getUserGroups()));
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
				vBoxOwner.setVisible(true);
				clearHtmlWarnings("warning");
			} else { // not found
				addHtmlWarning("warning", "Usuário não encontrado: " + registry, "", HtmlWarning.WARNING);
				vBoxOwner.setVisible(false);
			}
		} else { // registry is empty
			addHtmlWarning("warning", "Para adicionar um responsável, é necessário informar a matrícula do usuário", "", HtmlWarning.WARNING);
			vBoxOwner.setVisible(false);
		}
		// rebind
		ownerSearched = ownerAdded;
		updateBoundComponents(ownerBindComponents);
	}

	/**
	 * Searches a owner to remove from a group
	 */
	public void removeOwnerSearch() {
		String registry = ownerSearchTextBox.getText().trim();
		ownerAdded = null;
		ownerRemoved = null;
		ownerSearched = null;
		if (StringUtils.hasText(registry)) {
			boolean wasFound = false;
			for (Person person : selectedGroup.getOwners()) {
				if (person.getRegistry().equals(registry)) {
					ownerRemoved = person;
					wasFound = true;
					try {
						ownerRemoved.setName(groupManagerController.getPersonName(registry, ownerRemoved.getUserGroups()));
					} catch (Exception e) {
						e.printStackTrace();
						showDataBaseMessageError();
						return;
					}
					break;
				}
			}
			if (!wasFound) { // needs to search more
				for (Person person : ownersAdded) {
					if (person.getRegistry().equals(registry)) {
						ownerRemoved = person;
						if (!StringUtils.hasText(ownerRemoved.getName())) {
							try {
								ownerRemoved.setName(groupManagerController.getPersonName(registry, ownerRemoved.getUserGroups()));
							} catch (Exception e) {
								e.printStackTrace();
								showDataBaseMessageError();
								return;
							}
						}
						wasFound = true;
						break;
					}
				}
			}
			if (!wasFound) { // not found
				addHtmlWarning("warning", "O usuário " + registry + " não faz parte dos responsáveis do grupo " + selectedGroup.getName(), "", HtmlWarning.WARNING);
				vBoxOwner.setVisible(false);
			} else {
				vBoxOwner.setVisible(true);
				clearHtmlWarnings("warning");
			}
		} else {
			addHtmlWarning("warning", "Para remover um participante, é necessário informar a matrícula do usuário", "", HtmlWarning.WARNING);
			vBoxOwner.setVisible(false);
		}
		// rebind
		ownerSearched = ownerRemoved;
		updateBoundComponents(ownerBindComponents);
	}

	/**
	 * Confirm adding or removing of a owner in a group
	 */
	public void confirmOwnerToGroup() {
		if (ownerAdded != null) {
			ownersAdded.add(ownerAdded);
			ownersRemoved.remove(ownerAdded);
			participantsAdded.add(ownerAdded);
			ownerAdded = null;
		}
		if (ownerRemoved != null) {
			ownersRemoved.add(ownerRemoved);
			ownersAdded.remove(ownerRemoved);
			ownerRemoved = null;
		}
		ownerSearchTextBox.setRawValue(null);
		vBoxOwner.setVisible(false);
		clearHtmlWarnings("warning");
	}

	/**
	 * Cancel the adding or removing of a owner in a group
	 */
	public void cancelOwnerToGroup() {
		ownerSearchTextBox.setRawValue(null);
		vBoxOwner.setVisible(false);
	}

	/**
	 * Saves group
	 */
	public void saveGroup() {
		if (selectedGroup != null) {
			// checking description
			if (!StringUtils.hasText(selectedGroup.getDescription())) {
				addHtmlWarning("warning", "Especifique uma descrição para o grupo", "", HtmlWarning.WARNING);
				return;
			}
			if (newGroup) {
				// checking name
				if (!StringUtils.hasText(tbxGroupName.getValue())) {
					addHtmlWarning("warning", "Especifique o codigo do grupo", "", HtmlWarning.WARNING);
					return;
				}
				boolean isValid = false;
				try {
					selectedGroup.setName(lblGroupPrefix.getValue() + tbxGroupName.getValue());
					isValid = groupManagerController.isValidGroupName(selectedGroup.getName());
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
				// checking name
				if (!isValid) {
					addHtmlWarning("warning", "Não é possível criar um grupo com esse código.", "", HtmlWarning.WARNING);
					return;
				}
			}
			Map<String, Group> map = null;
			try {
				map = groupManagerController.saveGroup(selectedGroup, participantsAdded, participantsRemoved, ownersAdded, ownersRemoved);
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
				return;
			}
			if (map != null && !map.isEmpty()) {
				List<String> errorlist = new ArrayList<String>();
				Iterator<String> keyIterator = map.keySet().iterator();
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					errorlist.add(map.get(key).getName());
				}
				addHtmlWarning("warning", "Erro ao salvar os grupo(s):", errorlist, HtmlWarning.ERROR);
			} else
				addHtmlWarning("warning", "Grupo " + selectedGroup.getName() + " salvo com sucesso", "", HtmlWarning.INFORMATION);
			doAfterSave(selectedGroup);
		}
	}

	/**
	 * Deletes a group
	 */
	public void deleteGroup() {
		if (selectedGroup != null) {
			Map<String, Group> map = groupManagerController.deleteGroup(selectedGroup);
			if (!map.isEmpty())
				addHtmlWarning("warning", "Erro ao excluir o grupo: " + selectedGroup.getName(), "", HtmlWarning.ERROR);
			else
				addHtmlWarning("warning", "Grupo " + selectedGroup.getName() + " excluido com sucesso", "", HtmlWarning.INFORMATION);
			doAfterDelete(selectedGroup);
		}
	}

	/**
	 * Auto update - searches at siga the participants
	 */
	public void groupUpDate() {
		if (selectedGroup != null) {
			Map<String, Group> map = null;
			try {
				map = groupManagerController.upDateParticipants(selectedGroup);
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
				return;
			}
			if (!map.isEmpty())
				addHtmlWarning("warning", "Erro ao atualizar o grupo: " + selectedGroup.getName(), "", HtmlWarning.ERROR);
			else
				addHtmlWarning("warning", "Grupo " + selectedGroup.getName() + " atualizado com sucesso", "", HtmlWarning.INFORMATION);
			doAfterSave(selectedGroup);
		}
	}

	/**
	 * 
	 * @return Selected groud
	 */
	public Group getSelectedGroup() {
		return selectedGroup;
	}

	/**
	 * Sets selected group
	 * 
	 * @param selectedGroup
	 */
	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	// begin - popupWin participants methods
	/**
	 * Sets visible false popupWin participants - close the popup window
	 */
	public void popupWinParticipantsOnCancel() {
		Window popupWinParticipants = (Window) getFellow("popupWinParticipants");
		popupWinParticipants.getFellow("warning").setVisible(false);
		popupWinParticipants.doEmbedded();
		popupWinParticipants.setVisible(false);
		((Textbox) popupWinParticipants.getFellow("popupWinParticipantsTextBox")).setRawValue(null);
	}

	/**
	 * Initialize the popupWin participants- "onAppear window"
	 */
	public void initPopupWinParticipants() {
		// getting the added people to selected group
		try {
			// getting the names of the participants
			Set<Person> all = new HashSet<Person>();
			all.addAll(selectedGroup.getParticipants());
			all.addAll(participantsAdded);
			groupManagerController.setPeopleName(all);
		} catch (Exception e) {
			e.printStackTrace();
			showDataBaseMessageError();
			return;
		}
		allNameVOsParticipantsAdded = new HashSet<NameVO>(0);
		allNameVOsParticipantsNotAdded = new HashSet<NameVO>(0);
		for (Person person : selectedGroup.getParticipants())
			if (!participantsRemoved.contains(person))
				allNameVOsParticipantsAdded.add(new NameVO(person.getName(), person.getRegistry()));
		for (Person person : participantsAdded)
			allNameVOsParticipantsAdded.add(new NameVO(person.getName(), person.getRegistry()));
		Window popupWinParticipants = (Window) getFellow("popupWinParticipants");
		popupWinParticipants.setWidth("700px");
		List<Component> components = new ArrayList<Component>(2);
		components.add(popupWinParticipants.getFellow("participantsAdded"));
		components.add(popupWinParticipants.getFellow("participantsNotAdded"));
		updateBoundComponents(components);
		popupWinParticipants.doHighlighted();
	}

	/**
	 * Searches a participant by name at siga - popupWin participants
	 */
	public void popupWinParticipantsSearch() {
		Window popupWinParticipants = (Window) getFellow("popupWinParticipants");
		String text = ((Textbox) popupWinParticipants.getFellow("popupWinParticipantsTextBox")).getText().trim();
		allNameVOsParticipantsNotAdded = new HashSet<NameVO>(0);
		Label warning = ((Label) popupWinParticipants.getFellow("warning"));
		if (StringUtils.hasText(text)) {
			try {
				allNameVOsParticipantsNotAdded.addAll(groupManagerController.getNameAndRegistryByName(text));
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
				return;
			}
			if (allNameVOsParticipantsNotAdded != null && allNameVOsParticipantsAdded != null)
				for (NameVO nameVO : allNameVOsParticipantsAdded)
					if (allNameVOsParticipantsNotAdded.contains(nameVO))
						allNameVOsParticipantsNotAdded.remove(nameVO);
			updateBoundComponent(popupWinParticipants.getFellow("participantsNotAdded"));
			if (allNameVOsParticipantsNotAdded.isEmpty()) {
				warning.setValue("Nenhum resultado encontrado");
				warning.setVisible(true);
			} else
				warning.setVisible(false);
		} else {
			warning.setValue("Informe um nome para busca");
			warning.setVisible(true);
		}
	}

	/**
	 * Add a participant to a group - popupWin Participants
	 */
	@SuppressWarnings("unchecked")
	public void popupWinParticipantsAddPerson() {
		Window popupWinParticipants = (Window) getFellow("popupWinParticipants");
		Listbox listBox = (Listbox) popupWinParticipants.getFellow("participantsNotAdded");
		Set<Listitem> selectedItens = listBox.getSelectedItems();
		if (selectedItens != null && !selectedItens.isEmpty()) {
			if (allNameVOsParticipantsAdded == null)
				allNameVOsParticipantsAdded = new HashSet<NameVO>();
			// getting the selected nameVOs
			for (Listitem listitem : selectedItens) {
				allNameVOsParticipantsAdded.add((NameVO) listitem.getValue());
				allNameVOsParticipantsNotAdded.remove((NameVO) listitem.getValue());
			}
			List<Component> components = new ArrayList<Component>(2);
			components.add(popupWinParticipants.getFellow("participantsNotAdded"));
			components.add(popupWinParticipants.getFellow("participantsAdded"));
			updateBoundComponents(components);
		}
	}

	/**
	 * Removes a participant from a group - popupWinParticipants
	 */
	@SuppressWarnings("unchecked")
	public void popupWinParticipantsRemovePerson() {
		Window popupWinParticipants = (Window) getFellow("popupWinParticipants");
		Listbox listBox = (Listbox) popupWinParticipants.getFellow("participantsAdded");
		Set<Listitem> selectedItens = listBox.getSelectedItems();
		if (selectedItens != null && !selectedItens.isEmpty()) {
			if (allNameVOsParticipantsNotAdded == null)
				allNameVOsParticipantsNotAdded = new HashSet<NameVO>();
			for (Listitem listitem : selectedItens) {
				allNameVOsParticipantsAdded.remove((NameVO) listitem.getValue());
				allNameVOsParticipantsNotAdded.add((NameVO) listitem.getValue());
			}
			List<Component> components = new ArrayList<Component>(2);
			components.add(popupWinParticipants.getFellow("participantsAdded"));
			components.add(popupWinParticipants.getFellow("participantsNotAdded"));
			updateBoundComponents(components);
		}
	}

	/**
	 * On ok action popupWin participants
	 * 
	 */
	public void popupWinParticipantsOnOk() {
		participantsRemoved = new HashSet<Person>();
		participantsAdded = new HashSet<Person>();
		for (Person person : selectedGroup.getParticipants()) {
			if (!allNameVOsParticipantsAdded.contains(new NameVO(null, person.getRegistry()))) {
				participantsRemoved.add(person);
				ownersAdded.remove(person);
				if (selectedGroup.getOwners().contains(person))
					ownersRemoved.add(person);
			}
		}
		for (NameVO nameVO : allNameVOsParticipantsAdded) {
			boolean contains = false;
			for (Person person : selectedGroup.getParticipants())
				if (person.getRegistry().equals(nameVO.getRegistry())) {
					contains = true;
					break;
				}
			if (!contains) {
				try {
					participantsAdded.add(groupManagerController.getPersonByRegistry(nameVO.getRegistry()));
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
			}
		}
		for (Iterator<Person> iterator = ownersAdded.iterator(); iterator.hasNext();) {
			Person person = iterator.next();
			if (!allNameVOsParticipantsAdded.contains(new NameVO(null, person.getRegistry())))
				iterator.remove();
		}
		popupWinParticipantsOnCancel();
	}

	// end - popupWin participants methods
	// begin - popupWin Owners methods
	/**
	 * Sets visible false popupWin owners - close the popup window
	 */
	public void popupWinOwnersOnCancel() {
		Window popupWinOwners = (Window) getFellow("popupWinOwners");
		popupWinOwners.getFellow("warning").setVisible(false);
		popupWinOwners.doEmbedded();
		popupWinOwners.setVisible(false);
		((Textbox) popupWinOwners.getFellow("popupWinOwnersTextBox")).setRawValue(null);
	}

	/**
	 * Initialize the popupWin Owners - "onAppear window"
	 */
	public void initPopupWinOwners() {
		// getting the added people to selected group
		try {
			// setting the owners name
			Set<Person> all = new HashSet<Person>();
			all.addAll(selectedGroup.getOwners());
			all.addAll(ownersAdded);
			groupManagerController.setPeopleName(all);
		} catch (Exception e) {
			e.printStackTrace();
			showDataBaseMessageError();
			return;
		}
		allNameVOsOwnersAdded = new HashSet<NameVO>();
		allNameVOsOwnersNotAdded = new HashSet<NameVO>();
		for (Person person : selectedGroup.getOwners()) {
			if (!ownersRemoved.contains(person))
				allNameVOsOwnersAdded.add(new NameVO(person.getName(), person.getRegistry()));
		}
		for (Person person : ownersAdded)
			allNameVOsOwnersAdded.add(new NameVO(person.getName(), person.getRegistry()));
		Window popupWinOwners = (Window) getFellow("popupWinOwners");
		popupWinOwners.setWidth("700px");
		List<Component> components = new ArrayList<Component>(2);
		components.add(popupWinOwners.getFellow("ownersAdded"));
		components.add(popupWinOwners.getFellow("ownersNotAdded"));
		updateBoundComponents(components);
		popupWinOwners.doHighlighted();
	}

	/**
	 * Searches a person by name at siga - popupWin owners
	 */
	public void popupWinOwnersSearch() {
		Window popupWinOwners = (Window) getFellow("popupWinOwners");
		String text = ((Textbox) popupWinOwners.getFellow("popupWinOwnersTextBox")).getText().trim();
		Label warning = ((Label) popupWinOwners.getFellow("warning"));
		allNameVOsOwnersNotAdded = new HashSet<NameVO>();
		if (StringUtils.hasText(text)) {
			try {
				allNameVOsOwnersNotAdded.addAll(groupManagerController.getNameAndRegistryByName(text));
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
				return;
			}
			if (allNameVOsOwnersNotAdded != null && allNameVOsOwnersAdded != null)
				for (NameVO nameVO : allNameVOsOwnersAdded)
					if (allNameVOsOwnersNotAdded.contains(nameVO))
						allNameVOsOwnersNotAdded.remove(nameVO);
			updateBoundComponent(popupWinOwners.getFellow("ownersNotAdded"));
			if (allNameVOsOwnersNotAdded.isEmpty()) {
				warning.setValue("Nenhum resultado encontrado");
				warning.setVisible(true);
			} else
				warning.setVisible(false);
		} else {
			warning.setValue("Informe um nome para busca");
			warning.setVisible(true);
		}
	}

	/**
	 * Add a owners to a group - popupWin Owners
	 */
	@SuppressWarnings("unchecked")
	public void popupWinOwnersAddPerson() {
		Window popupWinOwners = (Window) getFellow("popupWinOwners");
		Listbox listBox = (Listbox) popupWinOwners.getFellow("ownersNotAdded");
		Set<Listitem> selectedItens = listBox.getSelectedItems();
		if (selectedItens != null && !selectedItens.isEmpty()) {
			if (allNameVOsOwnersAdded == null)
				allNameVOsOwnersAdded = new HashSet<NameVO>();
			// getting the selected nameVOs
			for (Listitem listitem : selectedItens) {
				allNameVOsOwnersAdded.add((NameVO) listitem.getValue());
				allNameVOsOwnersNotAdded.remove((NameVO) listitem.getValue());
			}
			List<Component> components = new ArrayList<Component>(2);
			components.add(popupWinOwners.getFellow("ownersNotAdded"));
			components.add(popupWinOwners.getFellow("ownersAdded"));
			updateBoundComponents(components);
		}
	}

	/**
	 * Removes a owner from a group - popupWin Owners
	 */
	@SuppressWarnings("unchecked")
	public void popupWinOwnersRemovePerson() {
		Window popupWinOwners = (Window) getFellow("popupWinOwners");
		Listbox listBox = (Listbox) popupWinOwners.getFellow("ownersAdded");
		Set<Listitem> selectedItens = listBox.getSelectedItems();
		if (selectedItens != null && !selectedItens.isEmpty()) {
			if (allNameVOsOwnersNotAdded == null)
				allNameVOsOwnersNotAdded = new HashSet<NameVO>();
			for (Listitem listitem : selectedItens) {
				allNameVOsOwnersAdded.remove((NameVO) listitem.getValue());
				allNameVOsOwnersNotAdded.add((NameVO) listitem.getValue());
			}
			List<Component> components = new ArrayList<Component>(2);
			components.add(popupWinOwners.getFellow("ownersAdded"));
			components.add(popupWinOwners.getFellow("ownersNotAdded"));
			updateBoundComponents(components);
		}
	}

	/**
	 * On ok action popupWin owners
	 * 
	 */
	public void popupWinOwnersOnOk() {
		ownersRemoved = new HashSet<Person>();
		ownersAdded = new HashSet<Person>();
		for (Person person : selectedGroup.getOwners())
			if (!allNameVOsOwnersAdded.contains(new NameVO(null, person.getRegistry())))
				ownersRemoved.add(person);
		for (NameVO nameVO : allNameVOsOwnersAdded) {
			boolean contain = false;
			for (Person person : selectedGroup.getOwners())
				if (person.getRegistry().equals(nameVO.getRegistry()))
					contain = true;
			if (!contain) {
				try {
					Person p = groupManagerController.getPersonByRegistry(nameVO.getRegistry());
					ownersAdded.add(p);
					participantsAdded.add(p);
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
			}
		}
		popupWinOwnersOnCancel();
	}

	// end - popupWin Owner methods
	/**
	 * After save action
	 * 
	 * @param selectedGroup
	 */
	private void doAfterSave(Group selectedGroup) {
		groupDescriptorGrid.setVisible(false);
		groupsListBox.setVisible(false);
		hBoxButtons.setVisible(false);
		Events.sendEvent(new Event("onGroupUpdate", Path.getComponent("//main/menuWindow/myGroups")));
	}

	/**
	 * After delete action
	 */
	private void doAfterDelete(Group deletedGroup) {
		groups.remove(deletedGroup);
		if (groups.isEmpty())
			groupsListBox.setVisible(false);
		groupsListBox.setVisible(false);
		groupDescriptorGrid.setVisible(false);
		hBoxButtons.setVisible(false);
		Window deleteWinConfimation = ((Window) getFellow("deleteWinConfimation"));
		deleteWinConfimation.doEmbedded();
		deleteWinConfimation.setVisible(false);
		Events.sendEvent(new Event("onGroupUpdate", Path.getComponent("//main/menuWindow/myGroups")));
	}

	/**
	 * Shows a dataBase message error
	 */
	private void showDataBaseMessageError() {
		addHtmlWarning("warning", "O sistema identificou uma falha de banco de dados. Tente novamente mais tarde ou aguarde por reparos", "", HtmlWarning.ERROR);
	}

	/**
	 * Includes new group page
	 */
	private void includeNewGroupPage() {
		((Include) Executions.getCurrent().getDesktop().getPage("main").getFellow("include")).setSrc("/zul/secure/adm/group/groupEditWindow.zul?new=1");
	}

	/**
	 * Includes edit group page
	 */
	private void includeEditGroupPage() {
		((Include) Executions.getCurrent().getDesktop().getPage("main").getFellow("include")).setSrc("/zul/secure/adm/group/groupEditWindow.zul");
	}
}
