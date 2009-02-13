package com.integrareti.integraframework.test;

import com.integrareti.integraframework.dao.integra.DomainDao;

public class TestDomainSevice extends BasicIntegraTestCase {

	private DomainDao domainDao;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		domainDao = (DomainDao) getBean("domainDao");
	}
	
	public void testDomainService() {
		try {
			System.out.println(domainDao.getDomainByName("ice.ufjf.br").getName());
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

}
