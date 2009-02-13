package com.integrareti.integraframework.acegi;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines where to redirect a user based it's the role
 * 
 * @created 27/02/2008
 * @author Thiago Athouguia Gama *
 * @version 1.0
 */
public class PageRedirectDefinition {
	private String roleName;
	private String pageURL;

	public PageRedirectDefinition() {
	}

	public PageRedirectDefinition(String roleName, String pageName) {
		setRoleName(roleName);
		setPageURL(pageName);
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

	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageName) {
		this.pageURL = pageName;
	}
}