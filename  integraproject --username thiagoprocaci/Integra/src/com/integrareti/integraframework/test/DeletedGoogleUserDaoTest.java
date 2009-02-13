package com.integrareti.integraframework.test;

import java.util.Calendar;
import java.util.List;

import com.integrareti.integraframework.business.DeletedGoogleUser;
import com.integrareti.integraframework.dao.integra.DeletedGoogleUserDao;
import com.integrareti.integraframework.dao.integra.DomainDao;

public class DeletedGoogleUserDaoTest extends BasicIntegraTestCase {
	private DeletedGoogleUserDao deletedGoogleUserDao;
	private DomainDao domainDao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		deletedGoogleUserDao = (DeletedGoogleUserDao) getBean("deletedGoogleUserDao");
		domainDao = (DomainDao) getBean("domainDao");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSave() {
		boolean b = true;
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -5);
			DeletedGoogleUser deletedGoogleUser = new DeletedGoogleUser();
			deletedGoogleUser.setDomain(domainDao.getDomainByName("ice.ufjf.br"));
			deletedGoogleUser.setExclusionDate(cal);
			deletedGoogleUser.setDeletedGoogleAccount("cavuco2");
			deletedGoogleUserDao.save(deletedGoogleUser);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}

	public void testGetUntilFiveDaysAgo() {
		boolean b = true;
		try {
			List<DeletedGoogleUser> list = deletedGoogleUserDao.getDeletedGoogleUserUntilFiveDaysAgoByDomainName("ice.ufjf.br");
			for (DeletedGoogleUser deletedGoogleUser : list)
				System.out.println(deletedGoogleUser.getDeletedGoogleAccount());
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}
}
