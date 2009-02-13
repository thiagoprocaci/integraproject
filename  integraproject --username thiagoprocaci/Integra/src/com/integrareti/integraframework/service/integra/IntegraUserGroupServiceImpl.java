package com.integrareti.integraframework.service.integra;

import java.util.List;

import com.integrareti.integraframework.authentication.UserGroup;
import com.integrareti.integraframework.dao.integra.UserGroupDao;

/**
 * This class offers service to manipulates userGroups at integra database
 * 
 * @author Thiago
 * 
 */
public class IntegraUserGroupServiceImpl extends
		IntegraServiceImpl<UserGroup, Integer> implements
		IntegraUserGroupServiceInterface {

	private UserGroupDao userGroupDao;

	/**
	 * Creates a new IntegraUserGroupServiceImpl
	 * 
	 * @param userGroupDao
	 */
	public IntegraUserGroupServiceImpl(UserGroupDao userGroupDao) {
		super(userGroupDao);
		this.userGroupDao = userGroupDao;
	}

	/**
	 * 
	 * @param group
	 *            names
	 * @return <code>List<UserGroup></code> each one with name = name[x]
	 * @throws Exception 
	 */
	@Override
	public List<UserGroup> getByName(String... name) throws Exception {
		return userGroupDao.getByName(name);
	}


}
