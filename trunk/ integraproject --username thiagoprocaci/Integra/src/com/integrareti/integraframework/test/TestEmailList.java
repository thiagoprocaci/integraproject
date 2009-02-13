package com.integrareti.integraframework.test;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.service.google.GoogleEmailListServiceInterface;

public class TestEmailList extends BasicIntegraTestCase {

	private GoogleEmailListServiceInterface service;

	// private GoogleUserAccountServiceInterface userService;

	protected void setUp() throws Exception {
		super.setUp();
		service = (GoogleEmailListServiceInterface) getBean("googleEmailListService");
		//userService = (GoogleUserAccountServiceInterface) getBean("personService");

	}

	public void testSaveEmailList() {
		EmailList eml;
		try {
			eml = service.getEmailListByNameAndDomain("teste2", "ice.ufjf.br");
			assertNotNull(eml.getEmailListEntry());
			assertNotNull(eml.getDomain().getName());
			assertEquals("ice.ufjf.br", eml.getDomain().getName());
			assertEquals("teste2", eml.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
