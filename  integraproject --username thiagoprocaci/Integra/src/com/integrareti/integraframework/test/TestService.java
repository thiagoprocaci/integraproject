package com.integrareti.integraframework.test;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.google.GoogleEmailListServiceInterface;

public class TestService implements Runnable {
	private GoogleEmailListServiceInterface emlService;
	private Person addPerson;
	private EmailList addEmList;

	public TestService(GoogleEmailListServiceInterface s, Person p, EmailList e) {
		this.addEmList = e;
		this.addPerson = p;
		this.emlService = s;
	}

	public void run() {
		try {
			emlService.addRecipient(addEmList, addPerson);
			System.err.println("lenha!!!!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
