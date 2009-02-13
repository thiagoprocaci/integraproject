package com.integrareti.integraframework.test;

import java.io.IOException;
import java.util.List;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListFeed;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.dao.google.GoogleEmailListDaoImpl;

public class TestGoogleEmailListDao extends BasicIntegraTestCase {
	private GoogleEmailListDaoImpl dao;

	protected void setUp() throws Exception {
		super.setUp();
		dao = (GoogleEmailListDaoImpl) getBean("googleEmailListDao");
	}

	public void testDeleteAllEmailLists() throws AppsForYourDomainException, ServiceException, IOException, Exception {
		EmailListFeed emailListFeed = dao.retrieveAllEmailLists("ice.ufjf.br");
		List<EmailListEntry> list = emailListFeed.getEntries();
		boolean b = false;
		for (EmailListEntry emailListEntry : list)
			dao.deleteEmailList(emailListEntry.getEmailList().getName(), "ice.ufjf.br");
		b = true;
		assertEquals(true, b);
	}
}
