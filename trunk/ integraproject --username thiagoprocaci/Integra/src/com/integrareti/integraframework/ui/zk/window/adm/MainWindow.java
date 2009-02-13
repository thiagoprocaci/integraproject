package com.integrareti.integraframework.ui.zk.window.adm;

import java.util.List;
import java.util.Scanner;

import org.acegisecurity.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;

import com.integrareti.integraframework.acegi.ComponentVisibilityDefinitionHolder;
import com.integrareti.integraframework.business.Person;

/**
 * Implements the main window of the administration system
 * (/zul/secure/adm/main.zul)
 * 
 * @author Thiago Athouguia Gama
 * 
 */
@SuppressWarnings("serial")
public class MainWindow extends Borderlayout {
	private ComponentVisibilityDefinitionHolder holder;
	private Label lblUser;

	public void onCreate() {
		lblUser = (Label) getFellow("lblUser");
		lblUser.setValue(((Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
		secureComponents();
	}

	/**
	 * Makes components invisible if user dos not have the specified Role
	 */
	private void secureComponents() {
		holder = (ComponentVisibilityDefinitionHolder) SpringUtil.getBean("componentVisibilityDefinitionHolder");
		List<String> comps = holder.getComponentVisibilityDefinitionSource().getComponentsToHide(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		Component component = this;
		for (String string : comps) {
			Scanner sc = new Scanner(string);
			sc.useDelimiter("/");
			while (sc.hasNext()) {
				String compId = sc.next();
				component = component.getFellow(compId);
			}
			component.setVisible(false);
			component = this;
		}
	}
}
