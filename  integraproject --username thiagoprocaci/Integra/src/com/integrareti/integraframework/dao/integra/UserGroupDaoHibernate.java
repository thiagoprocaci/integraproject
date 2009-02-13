package com.integrareti.integraframework.dao.integra;

import java.util.List;

import com.integrareti.integraframework.authentication.UserGroup;

/**
 * This interface offers methods to manipulates usergroups st integra database
 * @author Thiago
 *
 */
public class UserGroupDaoHibernate extends
		GenericDaoHibernate<UserGroup, Integer> implements UserGroupDao {

	/**
	 * 
	 * @param name
	 * @return Returns a list of usergroups by a list of usergroup name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UserGroup> getByName(String... names)
			throws Exception {
		String sql = "From UserGroup ug WHERE ug.name = ? ";
		for (int i = 0; i < names.length - 1; i++) {
			sql += "OR ug.name = ? ";
		}
		return getHibernateTemplate().find(sql, names);
	}

	public List<UserGroup> getAllByDomainName(String domainName) throws Exception {
		return null;
	}
}
