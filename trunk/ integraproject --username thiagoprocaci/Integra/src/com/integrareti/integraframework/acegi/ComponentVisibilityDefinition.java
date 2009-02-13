package com.integrareti.integraframework.acegi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines the zk component visibility (if it is visible) based on the role
 * 
 * @created 27/02/2008
 * @author Thiago Athouguia Gama *
 * @version 1.0
 * 
 */
public class ComponentVisibilityDefinition {
	private String roleName;
	private String componentId;

	public ComponentVisibilityDefinition() {
	}

	public ComponentVisibilityDefinition(String roleName, String componentId) {
		setRoleName(roleName);
		setComponentId(componentId);
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
}
