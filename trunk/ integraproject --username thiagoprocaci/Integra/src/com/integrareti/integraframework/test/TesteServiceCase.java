package com.integrareti.integraframework.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.google.GoogleEmailListServiceInterface;
import com.integrareti.integraframework.service.google.GoogleUserAccountServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraGroupServiceInterface;

@SuppressWarnings("all")
public class TesteServiceCase extends TestCase {
	private ApplicationContext ctx;
	private TaskExecutor te;
	private IntegraGroupServiceInterface gService;
	private GoogleEmailListServiceInterface emlService;
	private GoogleUserAccountServiceInterface usrService;
	private Set<Person> people;
	private EmailList eml;
	private Person addPerson;
	private EmailList addEmList;

	protected void setUp() throws Exception {
		super.setUp();
		ctx = new ClassPathXmlApplicationContext("com/integrareti/integraframework/test/test-appcontext.xml");
		te = (TaskExecutor) ctx.getBean("taskExecutor");
		gService = (IntegraGroupServiceInterface) ctx.getBean("groupService");
		emlService = (GoogleEmailListServiceInterface) ctx.getBean("googleEmailListService");
		usrService = (GoogleUserAccountServiceInterface) ctx.getBean("personService");
		// Group group = gService.getById(141);
		try {
			people = new HashSet<Person>(usrService.getAllByDomainName("ice.ufjf.br"));
			/*
			 * eml = Hibernate.initialize(eml);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testLenha() {
		// Set empty = new HashSet<Object>();
		for (Person p : people) {
			try {
				te.execute(new TestService(emlService, p, emlService.getEmailListByNameAndDomain("ultimoteste", "ice.ufjf.br")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
