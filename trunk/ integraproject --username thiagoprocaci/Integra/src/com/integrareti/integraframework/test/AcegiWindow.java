package com.integrareti.integraframework.test;

import org.acegisecurity.context.SecurityContextHolder;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.integrareti.integraframework.business.Person;

@SuppressWarnings("serial")
public class AcegiWindow extends Window {
	private Label label;

	public void onCreate() {
		label = (Label) getFellow("label");
		Person p = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		label.setValue("Seja bem-vindo " + p.getEmail());
	}
}
