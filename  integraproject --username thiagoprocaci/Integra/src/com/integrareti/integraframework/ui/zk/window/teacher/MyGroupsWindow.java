package com.integrareti.integraframework.ui.zk.window.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.controller.teacher.MyGroupsController;
import com.integrareti.integraframework.service.google.sso.SingleSignOn;
import com.integrareti.integraframework.ui.zk.form.AcsForm;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AnnotateDataBinderWindow;

/**
 * 
 * Shows the groups that a {@link Person} owns and some other details
 * 
 * @created 10/03/2008
 * @author Thiago Athouguia Gama *
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MyGroupsWindow extends AnnotateDataBinderWindow {

	private MyGroupsController myGroupsController;
	private Person person;
	private AcsForm acsForm;

	/**
	 * Gets MyGroupsController
	 */
	@Override
	public void doOnCreate() {
		myGroupsController = (MyGroupsController) SpringUtil
				.getBean("myGroupsController");
		person = (Person) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		acsForm = new AcsForm(person.getDomain().getName());
		this.appendChild(acsForm);
	}

	/**
	 * Binds the selected group in the window
	 */
	@Override
	public void doBeforeBind() {
		Integer groupId = Integer.parseInt(Executions.getCurrent()
				.getParameter("groupId"));
		Group g = null;
		try {
			g = myGroupsController.getMyGroupById(groupId);
			StringBuilder owners = new StringBuilder();
			for (Iterator<Person> it = g.getOwners().iterator(); it.hasNext();) {
				owners.append(it.next().getName());
				if (it.hasNext())
					owners.append(", ");
			}
			getBindObjects().put("groupOwners", owners.toString());
			getBindObjects().put("myGroup", g);
		} catch (Exception e) {
			addHtmlWarning("warning", "Ocorreu um erro ao carregar os grupos",
					"", HtmlWarning.ERROR);
			e.printStackTrace();
		}		
	}

	/**
	 * Make an Action based on the selected item index os the
	 * <code>Listbox</code> If 1 open a google email window with the selected
	 * people email addresses in the To field If 1 open a google email window
	 * with the group email lists addresses in the To field
	 */
	@SuppressWarnings("unchecked")
	public void onActionSelect() {
		Listbox lbxActions = (Listbox) getFellow("lbxActions");
		Listbox lbxParticipants = (Listbox) getFellow("lbxParticipants");
		if (lbxActions.getSelectedIndex() == 1) {
			Set<Listitem> selecteditems = lbxParticipants.getSelectedItems();
			if (!selecteditems.isEmpty()) {
				List<String> addresses = new ArrayList<String>(selecteditems
						.size());
				for (Iterator<Listitem> it = selecteditems.iterator(); it
						.hasNext();) {
					addresses
							.add((((Person) ((Listitem) it.next()).getValue()))
									.getEmail());
				}
				composeMail(addresses);
			}

		} else if (lbxActions.getSelectedIndex() == 3) {
			List<String> addresses = new ArrayList<String>();
			Set<EmailList> lists = ((Group) getBindObjects().get("myGroup"))
					.getEmailLists();
			for (EmailList emailList : lists)
				addresses.add(emailList.getEmailAddress());
			composeMail(addresses);
		}
		lbxActions.setSelectedIndex(0);
	}

	/**
	 * Invokes a javascrip function to open a new window of gmail with the To
	 * field of the email filled
	 * 
	 * @param recipients
	 */
	private void composeMail(List<String> recipients) {
		StringBuilder sendTo = new StringBuilder();
		Iterator<String> it = recipients.iterator();
		while (it.hasNext()) {
			sendTo.append(it.next());
			if (it.hasNext())
				sendTo.append(", ");
		}
		String relayState = AcsForm.GOOGLE_MAIL_BASE_URL
				+ person.getDomain().getName()
				+ "/?view=cm&cmid=1&tf=1&AuthEventSource=SSO&fs=1&to=" + sendTo;
		String SAMLResponse = SingleSignOn.getInstance().getAuthSSO(
				person.getDomain().getName(), person.getGoogleAccount());
		String javaScript = "window.open('','popWin',\"height=550, width=650,toolbar=no,scrollbars=auto,location=0,status=1,resizable=1,menubar=no\");	";
		Clients.evalJavaScript(javaScript);
		acsForm.setSamlResponseTextAreaValue(SAMLResponse);
		acsForm.setRelayState(relayState);
		acsForm.setTarget("popWin");
		acsForm.submitForm();
	}
}
