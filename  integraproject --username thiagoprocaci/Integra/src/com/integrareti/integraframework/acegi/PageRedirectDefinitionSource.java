package com.integrareti.integraframework.acegi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.acegisecurity.GrantedAuthority;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper class to work with {@link PageRedirectDefinition}
 * 
 * @created 27/02/2008
 * @author Thiago Athouguia Gama *
 * @version 1.0
 */
public class PageRedirectDefinitionSource {
	private static final Log logger = LogFactory.getLog(PageRedirectDefinitionSource.class);
	private List<PageRedirectDefinition> definitionsList;

	/**
	 * Returns the {@link PageRedirectDefinition} based on the given
	 * {@link GrantedAuthority}[]
	 * 
	 * @param authorities
	 * @return
	 */
	public PageRedirectDefinition getDefinitionFor(GrantedAuthority[] authorities) {
		List<String> roleList = new ArrayList<String>();
		for (int i = 0; i < authorities.length; i++)
			roleList.add(authorities[i].getAuthority());
		for (Iterator<PageRedirectDefinition> iter = getDefinitionsList().iterator(); iter.hasNext();) {
			PageRedirectDefinition definition = (PageRedirectDefinition) iter.next();
			if (roleList.contains(definition.getRoleName())) {
				logger.debug("First matched PageRedirectDefinition :" + definition);
				return definition;
			}
		}
		return null;
	}

	public List<PageRedirectDefinition> getDefinitionsList() {
		return definitionsList;
	}

	public void setDefinitionsList(List<PageRedirectDefinition> definitionsList) {
		this.definitionsList = definitionsList;
	}
}
