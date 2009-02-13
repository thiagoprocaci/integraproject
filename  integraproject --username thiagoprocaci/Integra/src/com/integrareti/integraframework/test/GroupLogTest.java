package com.integrareti.integraframework.test;

import java.util.GregorianCalendar;
import java.util.List;

import com.integrareti.integraframework.business.error.SystemGroupError;
import com.integrareti.integraframework.business.log.GroupLog;
import com.integrareti.integraframework.business.task.SystemGroupTask;
import com.integrareti.integraframework.dao.integra.DomainDao;
import com.integrareti.integraframework.dao.integra.GroupLogDao;

public class GroupLogTest extends BasicIntegraTestCase {
	private GroupLogDao groupLogDao;
	private DomainDao domainDao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		groupLogDao = (GroupLogDao) getBean("groupLogDao");
		domainDao = (DomainDao) getBean("domainDao");
	}

	public void testSave() {
		boolean b = true;
		try {
			SystemGroupTask task = new SystemGroupTask();
			task.setDescription("teste descricao");
			task.setName("teste nome");
			task.addError(new SystemGroupError("teste causa", "teste descricao", task));
			task.addError(new SystemGroupError("teste causa 2", "teste descricao 2", task));
			GroupLog groupLog = new GroupLog();
			groupLog.setGroupDescription("teste com erro");
			groupLog.setBeginTime(new GregorianCalendar());
			groupLog.setEndTime(new GregorianCalendar());
			groupLog.setDomain(domainDao.getDomainByName("ice.ufjf.br"));
			groupLog.setGroupName("teste");
			groupLog.setGroupName("teste");
			task.setGroupLog(groupLog);
			groupLog.addTask(task);
			groupLogDao.save(groupLog);
			task = new SystemGroupTask();
			task.setDescription("teste descricao");
			task.setName("teste nome");
			groupLog = new GroupLog();
			groupLog.setGroupDescription("teste sem erro");
			groupLog.setBeginTime(new GregorianCalendar());
			groupLog.setEndTime(new GregorianCalendar());
			groupLog.setDomain(domainDao.getDomainByName("ice.ufjf.br"));
			groupLog.setGroupName("teste");
			groupLog.setGroupName("teste");
			task.setGroupLog(groupLog);
			groupLog.addTask(task);
			groupLogDao.save(groupLog);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}

	public void testQuery() {
		boolean b = true;
		try {
			List<GroupLog> list = groupLogDao.getByPeriod(new GregorianCalendar().getTime(), new GregorianCalendar().getTime());
			System.out.println("Todos :");
			for (GroupLog groupLog : list)
				System.out.println(groupLog.getGroupDescription());
			list = groupLogDao.getByPeriodWithErrors(new GregorianCalendar().getTime(), new GregorianCalendar().getTime());
			System.out.println("Todos com erro :");
			for (GroupLog groupLog : list)
				System.out.println(groupLog.getGroupDescription());
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}

	public void testGetByDomain() {
		boolean b = true;
		try {
			List<GroupLog> list = groupLogDao.getAllByDomainName("ice.ufjf.br");
			for (GroupLog groupLog : list)
				System.out.println(groupLog.getId());
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}

	public void testeDelete() {
		boolean b = true;
		try {
			List<GroupLog> all = groupLogDao.getAll();
			for (GroupLog groupLog : all)
				groupLogDao.delete(groupLog);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		assertEquals(true, b);
	}
}
