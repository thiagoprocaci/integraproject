package com.integrareti.integraframework.service.integra;

import java.util.List;

import com.integrareti.integraframework.authentication.UserGroup;

/**
 * This interface offers service to manipulates userGroups at integra database
 * @author Thiago
 *
 */
public interface IntegraUserGroupServiceInterface extends
		IntegraServiceInterface<UserGroup, Integer> {
	
	/**
	 * 
	 * @param group names
	 * @return <code>List<UserGroup></code>  each one with name = name[x]
	 */
	public List<UserGroup> getByName(String... name) throws Exception;

}
