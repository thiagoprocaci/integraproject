package com.integrareti.integraframework.test;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BasicIntegraTestCase extends TestCase {
	private ApplicationContext ctx;

	public ApplicationContext getAppContext() {
		if (ctx == null)
			ctx = new FileSystemXmlApplicationContext("/WebContent/WEB-INF/integra-data.xml");
		return ctx;
	}

	public Object getBean(String s) {
		return getAppContext().getBean(s);
	}
}
