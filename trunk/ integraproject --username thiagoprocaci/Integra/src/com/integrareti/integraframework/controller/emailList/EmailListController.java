package com.integrareti.integraframework.controller.emailList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.google.GoogleEmailListServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraGroupServiceInterface;

/**
 * This class gives to main list (view) the access to bussiness
 * components
 * @author Thiago
 * 
 */
public class EmailListController {

	private GoogleEmailListServiceInterface googleEmailListServiceInterface;
	private IntegraGroupServiceInterface integraGroupServiceInterface;

	/**
	 * Creates a new EmailListController
	 * 
	 * @param googleEmailListServiceInterface
	 */
	public EmailListController(GoogleEmailListServiceInterface googleEmailListServiceInterface,
			IntegraGroupServiceInterface integraGroupServiceInterface) {
		this.googleEmailListServiceInterface = googleEmailListServiceInterface;
		this.integraGroupServiceInterface = integraGroupServiceInterface;
	}

	/**
	 * Creates emailList
	 * 
	 * @param groups
	 * @return Recipients not added
	 */
	public Map<String, EmailList> createEmailLists(List<Group> groups) {
		//TODO seems like this task is more suitable for the bussines class
		
		//forcing to initialize the lazy participants
		for (Group group : groups) {
			integraGroupServiceInterface.reattach(group);
			integraGroupServiceInterface.initialize(group.getParticipants());			
		}
		
		int x = 0;
		String errorString = "";
		Map<String, EmailList> errorLists = new HashMap<String, EmailList>();
		for (Group group : groups) {
			int numberLists = 1;
			int participants = group.getParticipants().size();
			// each list can have at most 2000 users
			while (participants > EmailList.LIMIT_RECIPIENTS) {
				numberLists++;
				participants -= EmailList.LIMIT_RECIPIENTS;
			}
			for (int i = 0; i < numberLists; i++) {
				EmailList emailList = new EmailList();
				emailList.setDomain(group.getDomain());
				emailList.setGroup(group);
				if (i == 0)
					emailList.setName(group.getName());
				else {
					x = i + 1;
					emailList.setName(group.getName() + x);
				}
				try {
					googleEmailListServiceInterface.save(emailList);
					group.addEmailList(emailList);
					integraGroupServiceInterface.save(group);
				} catch (AppsForYourDomainException e) {
					errorString += "Invalid email list name. Entity exists only on Google. [GooErr: "
						+ ((AppsForYourDomainException) e)
								.getErrorCode() + "].";
					errorLists.put(errorString, emailList);
					errorString = "";
					e.printStackTrace();
				}catch (AuthenticationException e) {
					errorString += e.getMessage() + ".";					
					errorLists.put(emailList.getName()+": "+errorString, emailList);
					errorString = "";					
					e.printStackTrace();
				}catch (Exception e) {					
					errorString += e.getMessage() + ".";					
					errorLists.put(errorString, emailList);
					errorString = "";					
					e.printStackTrace();
				}
			}
		}
		return errorLists;
	}
	


	/**
	 * Adds participants to groups emailList.
	 * The emailLists for each group must be defined before
	 * @param groups
	 * @throws ServiceException 
	 */
	public Map<String, Person> addParticipants(List<Group> groups) {
		Map<String, Person> errors = new HashMap<String, Person>();
		for (Group group : groups) {
			List<EmailList> emailLists = new ArrayList<EmailList>();
			emailLists.addAll(group.getEmailLists());
			if (!emailLists.isEmpty()) {
				int i = 0;
				EmailList emailList = emailLists.get(i);
				for (Person person : group.getParticipants()) {
					if (emailList.getRecipients().size() == EmailList.LIMIT_RECIPIENTS) {
						i++;
						emailList = emailLists.get(i);
					}
					try {
						googleEmailListServiceInterface.addRecipient(emailList, person);
					} catch (AppsForYourDomainException e) {
						errors.put(e.getErrorCode().toString(), person);
						e.printStackTrace();
					}catch (Exception e) {
						errors.put(e.getMessage().toString(), person);
						e.printStackTrace();
					}
				}
			}
		}
		return errors;
	}

	
}
