package com.integrareti.integraframework.acegi;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

/**
 * Custom {@link AuthenticationProcessingFilter} that overides the
 * {@link AuthenticationProcessingFilter#getDefaultTargetUrl()} to redirect
 * based on {@link PageRedirectDefinition}.
 * 
 * @created 27/02/2008
 * @author Thiago Athouguia Gama
 * @version 1.0
 */
public class PageRedirectEnabledAuthenticationProcessingFilter extends AuthenticationProcessingFilter {
	private PageRedirectDefinitionSource pageRedirectDefinitionSource;

	/**
	 * <b>Description copied from
	 * {@link AbstractProcessingFilter#getDefaultTargetUrl()}</b> Supplies the
	 * default target Url that will be used if no saved request is found or the
	 * alwaysUseDefaultTargetUrl propert is set to true. Override this method of
	 * you want to provide a customized default Url (for example if you want
	 * different Urls depending on the authorities of the user who has just
	 * logged in).
	 */
	public String getDefaultTargetUrl() {
		Authentication authResult = SecurityContextHolder.getContext().getAuthentication();
		PageRedirectDefinition definition = getPageRedirectDefinitionSource().getDefinitionFor(authResult.getAuthorities());
		String targetUrl = super.getDefaultTargetUrl();
		if (definition != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("PageRedirectDefinition found :" + definition);
			}
			targetUrl = "/" + definition.getPageURL();
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("PageRedirectDefinition not found for authentication :" + authResult);
			}
		}
		logger.info("Default target url :" + targetUrl);
		return targetUrl;
	}

	public PageRedirectDefinitionSource getPageRedirectDefinitionSource() {
		return pageRedirectDefinitionSource;
	}

	public void setPageRedirectDefinitionSource(PageRedirectDefinitionSource pageRedirectDefinitionSource) {
		this.pageRedirectDefinitionSource = pageRedirectDefinitionSource;
	}
}
