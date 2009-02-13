package com.integrareti.integraframework.ui.zk.window.adm.emaillist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.acegisecurity.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radiogroup;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.controller.adm.group.GroupImportController;
import com.integrareti.integraframework.controller.emailList.EmailListController;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AnnotateDataBinderWindow;

@SuppressWarnings("serial")
public class EmailListAdmWindow extends AnnotateDataBinderWindow {

	private List<Group> groups;
	private GroupImportController groupImportController;
	private EmailListController emailListController;
	private Listbox lbxGroups;
	private int lastSelected;
	private Radiogroup showRG;

	@Override
	public void doOnCreate() {
		groupImportController = (GroupImportController) SpringUtil
				.getBean("groupImportController");
		emailListController = (EmailListController) SpringUtil
				.getBean("emailListController");
		lbxGroups = (Listbox) getFellow("lbxGroups");
		showRG = (Radiogroup) getFellow("showRG");
		lastSelected = 0;
		groups = loadGroupsWithEmailListOrNot(false);

	}

	public void refreshSelected(Component c) {
		Group g = (Group) ((Listitem) c.getParent().getParent()).getValue();
		groupImportController.reatach(g);
		getBindObjects().put("selectedGroup", g);
		updateBoundComponent(getFellow("details"));
	}

	@Override
	public void doBeforeBind() {
		getBindObjects().put("groups", groups);
	}

	@SuppressWarnings("unchecked")
	public void createEmailLists() {
		List<Group> groups = new ArrayList<Group>(lbxGroups.getSelectedItems()
				.size());
		for (Iterator<Listitem> it = lbxGroups.getSelectedItems().iterator(); it
				.hasNext();) {
			Listitem item = (Listitem) it.next();
			Group group = (Group) lbxGroups.getModel().getElementAt(
					item.getIndex());
			groups.add(group);
		}

		Map<String, EmailList> errors = emailListController
				.createEmailLists(groups);
		List<String> errorlist = new ArrayList<String>();
		Iterator<String> keyIterator = errors.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			errorlist.add(key);
		}
		if (errors.size() > 0) {
			addHtmlWarning("warnings",
					"Erro criando as seguntes listas de e-mail", errorlist,
					HtmlWarning.ERROR);
		} else {
			addHtmlWarning("warnings",
					"Lista(s) de email criada(s) com sucesso", "",
					HtmlWarning.INFORMATION);
		}
	}

	public void updateRecipients() {
		List<Group> groups = new ArrayList<Group>(lbxGroups.getSelectedItems()
				.size());
		/*
		 * for (Iterator<Listitem> it =
		 * lbxGroups.getSelectedItems().iterator(); it .hasNext();) { Listitem
		 * item = (Listitem) it.next(); Group group = (Group)
		 * lbxGroups.getModel().getElementAt( item.getIndex()); }
		 */
		Map<String, Person> errors = emailListController
				.addParticipants(groups);
		List<String> errorlist = new ArrayList<String>();
		Iterator<String> keyIterator = errors.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			Person person = (Person) errors.get(key);
			errorlist.add(person.getEmail() + ": " + key);
		}
		if (errors.size() > 0) {
			addHtmlWarning("warnings",
					"Erro incluindo os seguntes recipientes", errorlist,
					HtmlWarning.ERROR);
		} else {
			addHtmlWarning("warnings",
					"Recipiente(s) atualizado(s) com sucesso", "",
					HtmlWarning.INFORMATION);
		}
	}

	public void refreshList(boolean refresh) {
		int currentPage = lbxGroups.getPaginal().getActivePage() + 1;
		int selectedIndex = showRG.getSelectedIndex();
		if (refresh) {
			lastSelected = selectedIndex;
			if (selectedIndex == 0) {
				this.groups = loadGroupsWithEmailListOrNot(false);

			} else if (selectedIndex == 1) {
				this.groups = loadGroupsWithEmailListOrNot(true);
			}
			updateBoundComponent(lbxGroups);
			lbxGroups.getPaginal().setActivePage(currentPage - 1);
		}
	}

	private List<Group> loadGroupsWithEmailListOrNot(boolean with) {
		List<Group> groups = null;
		String domainName = ((Person) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getDomain().getName();
		try {
			if (with)
				groups = groupImportController.getGroups(domainName);
			else
				groups = groupImportController
						.getGroupsWithoutEmailList(domainName);
		} catch (Exception e) {
			addHtmlWarning("warnings", "Erro ao carregar grupos ", e
					.getMessage(), HtmlWarning.ERROR);
			e.printStackTrace();
		}
		return groups;
	}

	public int getLastSelected() {
		return lastSelected;
	}

	public void setLastSelected(int lastSelected) {
		this.lastSelected = lastSelected;
	}

}
