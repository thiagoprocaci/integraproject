package com.integrareti.integraframework.ui.zk.window.login;

import org.acegisecurity.context.SecurityContextHolder;

import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AbstractWindow;

@SuppressWarnings("serial")
public class AccessDeniedWindow extends AbstractWindow {	
	
	public void onCreate() {
		Person person = (Person)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		addHtmlWarning("warning", "Acesso negado", "Voc� n�o tem permiss�o para acessar o conte�do dessa p�gina. Voc� est� logado como \"" + person.getEmail() + "\"", HtmlWarning.WARNING);
		
	}

}
