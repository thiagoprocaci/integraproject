package com.integrareti.integraframework.test;

import java.util.List;

import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.exceptions.UsernameException;

public class TestUsername {
	public static void main(String[] args) {
		new TestUsername();
	}

	public TestUsername() {
		Person p = new Person();
		p.setGivenName("Thiago");
		p.setFamilyName("Baesso Procaci ");
		List<String> list;
		try {
			list = p.getPossiblesUsernames();
			for (String string : list) {
				System.out.println(string);
			}
		} catch (UsernameException e) {
			e.printStackTrace();
		}
	}
}
