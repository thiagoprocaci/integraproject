package com.integrareti.integraframework.acegi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.acegisecurity.GrantedAuthority;

/**
 * Helper class to work with {@link ComponentVisibilityDefinition}
 * 
 * @created 27/02/2008
 * @author Thiago Athouguia Gama *
 * @version 1.0
 */
public class ComponentVisibilityDefinitionSource {
	private List<ComponentVisibilityDefinition> definitionsList;

	/**
	 * Return the Id's of the ZK components to hide based on the
	 * {@link GrantedAuthority}[] Privided
	 * 
	 * @param GrantedAuthority[]
	 *            authorities
	 * @return List of Id's of the components
	 */
	public List<String> getComponentsToHide(GrantedAuthority[] authorities) {
		List<String> componentsToHide = new ArrayList<String>();
		List<String> componentsNotToHide = new ArrayList<String>();
		List<String> roleList = new ArrayList<String>();
		for (int i = 0; i < authorities.length; i++)
			roleList.add(authorities[i].getAuthority());
		for (Iterator<ComponentVisibilityDefinition> iter = getDefinitionsList().iterator(); iter.hasNext();) {
			ComponentVisibilityDefinition definition = (ComponentVisibilityDefinition) iter.next();
			if (roleList.contains(definition.getRoleName()))
				componentsNotToHide.add(definition.getComponentId());
		}
		for (Iterator<ComponentVisibilityDefinition> iter = getDefinitionsList().iterator(); iter.hasNext();) {
			ComponentVisibilityDefinition definition = (ComponentVisibilityDefinition) iter.next();
			if (!componentsNotToHide.contains(definition.getComponentId()))
				componentsToHide.add(definition.getComponentId());
		}
		return componentsToHide;
	}

	public List<ComponentVisibilityDefinition> getDefinitionsList() {
		return definitionsList;
	}

	public void setDefinitionsList(List<ComponentVisibilityDefinition> definitionsList) {
		this.definitionsList = definitionsList;
	}
}
