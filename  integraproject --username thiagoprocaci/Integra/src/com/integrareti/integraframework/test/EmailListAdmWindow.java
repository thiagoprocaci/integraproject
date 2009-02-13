package com.integrareti.integraframework.test;

import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.controller.adm.group.GroupImportController;
import com.integrareti.integraframework.controller.emailList.EmailListController;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AbstractWindow;

@SuppressWarnings("all")
public class EmailListAdmWindow extends AbstractWindow {

	private GroupImportController groupImportController;
	private EmailListController emailListController;
	private Listbox lbxGroups;
	
	public void onCreate() {
		groupImportController = (GroupImportController) SpringUtil
				.getBean("groupImportController");
		emailListController = (EmailListController) SpringUtil
				.getBean("emailListController");
		lbxGroups = (Listbox) getFellow("lbxGroups");

		List<Group> groups = loadGroupsWithEmailListOrNot(false);
	
		
		for (Group group : groups) {
			Listitem lIt = new Listitem();
			lIt.appendChild(new Listcell(group.getName()));
			lIt.appendChild(new Listcell(group.getDescription()));
			lIt.appendChild(new Listcell(group.getParticipants().size()+""));
			lIt.appendChild(new Listcell(group.getEmailLists().size()+""));
			lbxGroups.appendChild(lIt);
		}
		
		//setVariable("groups", groups, true);
		//AnnotateDataBinder binder = new AnnotateDataBinder(this);
		//binder.loadAll();
		
	}
	private List<Group> loadGroupsWithEmailListOrNot(boolean with) {
		List<Group> groups = null;
		try {
			if(with){
				groups = groupImportController.getGroups(((Person)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDomain().getName());
			}else{
				groups = groupImportController.getGroupsWithoutEmailList(((Person)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDomain().getName());
			}
		}catch (Exception e){
			addHtmlWarning("warnings", "Erro ao carregar grupos ", e.getMessage(), HtmlWarning.ERROR);
			e.printStackTrace();
		}
		return groups;
	}

}
