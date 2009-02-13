package com.integrareti.integraframework.test;

import junit.framework.TestCase;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.business.task.SystemGroupTask;

@SuppressWarnings("all")
public class TestSystemGroupTask extends TestCase {
	
	private Person person;
	private Group group;
	private EmailList emailList;
	private SystemGroupTask task;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		person = new Person();
		person.setRegistry("200435031");		
		group = new Group();
		group.setName("group");		
		emailList = new EmailList();
		emailList.setName("emailList");		
		task = new SystemGroupTask();
	}
	
	
	public void testDecriptionSystemGroupTask(){
		System.out.println(task.getSaveGroupDescription(group));
		System.out.println(task.getDeleteGroupDescription(group));
		System.out.println(task.getAddParticipantsToGroupDescription(group, emailList, person));
		System.out.println(task.getRemoveParticipantFromGroupDescription(group, emailList, person));
		System.out.println(task.getAddEmailListToGroupDescription(group, emailList));
		System.out.println(task.getDeleteListFromGroupDescription(group, emailList));
	}
	
	

}
