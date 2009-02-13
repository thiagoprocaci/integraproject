package com.integrareti.integraframework.dao.integra;

import java.util.List;

import com.integrareti.integraframework.authentication.UserGroup;

/**
 * This interface offers methods to manipulates usergroups st integra database
 * @author Thiago
 *
 */
public interface UserGroupDao extends GenericDao<UserGroup, Integer> {

	/**
	 * 
	 * @param name
	 * @return Returns a list of usergroups by a list of usergroup name
	 * @throws Exception
	 */
	public List<UserGroup> getByName(String... name) throws Exception;

}
