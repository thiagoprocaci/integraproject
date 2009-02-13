package com.integrareti.integraframework.test;

import java.util.ArrayList;
import java.util.List;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.dao.integra.GroupDao;
import com.integrareti.integraframework.dao.integra.GroupDaoJDBC;

public class TestGroupDao extends BasicIntegraTestCase {

	private GroupDao groupDao;
	private GroupDao groupDaoJDBC;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		groupDao = (GroupDao) getBean("groupDao");
		groupDaoJDBC = (GroupDao) getBean("groupDaoJDBC");
	}

	public void testPerformance() {
		boolean b = true;
		try {
			System.out.println("Hibernate");
			double time = System.currentTimeMillis();
			for (int i = 0; i < 1; i++) 
				groupDao.getGroupIdByName(i + "ss");
			System.out.println((System.currentTimeMillis() - time) / 1000);			
			System.out.println("JDBC");
			time = System.currentTimeMillis();
			((GroupDaoJDBC) groupDaoJDBC).openConnection();
			for (int i = 0; i < 1; i++) 
				groupDaoJDBC.getGroupIdByName(i + "ss");			
			((GroupDaoJDBC) groupDaoJDBC).closeConnection();
			System.out.println((System.currentTimeMillis() - time) / 1000);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}

	
	public void testGetByPiecesOfNames(){
		boolean b = true;
		List<String> list = new ArrayList<String>();
		list.add("fis");
		list.add("2007");
		try {
			groupDao.getGroupsByPiecesOfNames(list);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}
	
	public void testDeleteAll() {
		boolean b = true;
		try {
			List<Group> all = groupDao.getAll();
			for (Group group : all)
				groupDao.delete(group);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}
		assertEquals(true, b);
	}

}
