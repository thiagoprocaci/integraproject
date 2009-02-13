package com.integrareti.integraframework.dao.google;

import java.util.logging.Logger;

import com.integrareti.integraframework.dao.integra.DomainDao;

/**
 * This abstract class contains some commons variables to the googleDao classes
 * 
 * @author Thiago
 * 
 */
public abstract class GoogleDomainDao {
	protected static final String APPS_FEEDS_URL_BASE = "https://www.google.com/a/feeds/";
	protected static final String SERVICE_VERSION = "2.0";
	protected Logger LOGGER;
	protected DomainDao domainDao;
}