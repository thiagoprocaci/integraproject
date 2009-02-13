package com.integrareti.integraframework.test;

import java.util.ArrayList;
import java.util.List;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;

public class TestMailList {
	public static void main(String[] args) {
		new TestMailList();
	}

	public TestMailList() {
	}

	/**
	 * Adds participants to groups emailList
	 * 
	 * @param groups
	 */
	public List<Group> addParticipants(List<Group> groups) {
		for (Group group : groups) {
			List<EmailList> emailLists = new ArrayList<EmailList>();
			emailLists.addAll(group.getEmailLists());
			if (!emailLists.isEmpty()) {
				int i = 0;
				EmailList emailList = emailLists.get(i);
				for (Person person : group.getParticipants()) {
					if (emailList.getRecipients().size() == 2000) {
						i++;
						emailList = emailLists.get(i);
					}
					emailList.addRecipient(person);
				}
			}
		}
		return groups;
	}
}
