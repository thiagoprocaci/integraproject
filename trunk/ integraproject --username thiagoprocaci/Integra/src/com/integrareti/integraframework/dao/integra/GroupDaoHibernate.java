package com.integrareti.integraframework.dao.integra;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;

/**
 * This class offers method to access the group's data
 * 
 * @author Thiago B. Procaci
 * 
 */
public class GroupDaoHibernate extends GenericDaoHibernate<Group, Integer> implements GroupDao {
	/**
	 * 
	 * @param groupName
	 * @return Returns a list of groups by name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupByName(String groupName) throws Exception {
		return (List<Group>) getHibernateTemplate().find("FROM Group WHERE name=?", groupName);
	}

	/**
	 * 
	 * @param groupName
	 * @param domainName
	 * @return Returns a group by a name and domain name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Group getGroupByNameAndDomainName(String groupName, String domainName) throws Exception {
		List<Group> list = (List<Group>) getHibernateTemplate().find("SELECT g FROM Group g, Domain d where g.name=?  and g.domain.name=? ", new String[] { groupName, domainName });
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param groupName
	 * @return Returns the group id by name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer getGroupIdByName(String groupName) throws Exception {
		List<Integer> list = (List<Integer>) getHibernateTemplate().find("SELECT g.id FROM Group g WHERE g.name=?", groupName);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns all groups without email list by domain name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Group> getAllGroupsWithoutEmailListByDomainName(String domainName) throws Exception {
		return getHibernateTemplate().find("From Group g WHERE size(g.emailLists) = 0 AND g.domain.name = ?", domainName);
	}

	/**
	 * 
	 * @param description
	 * @return Returns groups by description
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupsByDescription(String description) throws Exception {
		return getHibernateTemplate().find(String.format("SELECT g FROM Group g WHERE g.description like '%%%S%%'", description));
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	@Override
	public List<Group> getGroupsByDescriptionAndName(String description, String name) throws Exception {
		return getHibernateTemplate().find(String.format("SELECT g FROM Group g WHERE g.description like '%%%S%%' AND g.name like '%%%S%%'", new String[] { description, name }));
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	@Override
	public List<Group> getGroupsByDescriptionAndName(String description, List<String> names) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT g FROM Group g WHERE g.description like '%%%S%%' ");
		String[] array = new String[names.size() + 1];
		array[0] = description;
		int count = 1;
		for (String string : names) {
			buffer.append("AND g.name like '%%%S%%' ");
			array[count] = string;
			count++;
		}
		return getHibernateTemplate().find(String.format(buffer.toString(), array));
	}

	/**
	 * 
	 * 
	 * @param clue
	 * @return Returns groups by any clue (using like)
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	@Override
	public List<Group> getGroupsByClue(String clue) throws Exception {
		return getHibernateTemplate().find(String.format("SELECT g FROM Group g WHERE g.description like '%%%S%%' or g.name like '%%%S%%'", new String[] { clue, clue }));
	}

	/**
	 * 
	 * @param names
	 * @return Returns groups by pieces of name - using like primitive
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	@Override
	public List<Group> getGroupsByPiecesOfNames(List<String> names) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String[] array = new String[names.size()];
		int count = 0;
		for (String string : names) {
			if (count == 0)
				buffer.append("SELECT g FROM Group g WHERE g.name like '%%%S%%' ");
			else
				buffer.append(" AND g.name like '%%%S%%' ");
			array[count] = string;
			count++;
		}
		return getHibernateTemplate().find(String.format(buffer.toString(), array));
	}

	/**
	 * @return Returns all groups by domain
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getAllByDomainName(String domainName) throws Exception {
		return getHibernateTemplate().find("From Group g WHERE g.domain.name = ?", domainName);
	}

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups by domain name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getPageByDomainName(String domainName, int first, int offset) throws Exception {
		DetachedCriteria dt = DetachedCriteria.forClass(Group.class);
		dt.createAlias("domain", "dom");
		dt.add(Restrictions.eq("dom.name", domainName));
		return getHibernateTemplate().findByCriteria(dt, first, offset);
	}

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups without emaillist by domain
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getPageOfGroupsWithoutEmailListByDomainName(String domainName, int first, int offset) throws Exception {
		DetachedCriteria dt = DetachedCriteria.forClass(Group.class);
		dt.createAlias("domain", "dom");
		dt.add(Restrictions.eq("dom.name", domainName));
		dt.add(Restrictions.sizeEq("emailLists", 0));
		return getHibernateTemplate().findByCriteria(dt, first, offset);
	}

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups by domain name
	 * @throws Exception
	 */
	@Override
	public Long countGroupsByDomainName(String domain) throws Exception {
		return (Long) getHibernateTemplate().find("Select count(g) FROM Group g WHERE g.domain.name = ?", domain).get(0);
	}

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups without emaillist by domain name
	 * @throws Exception
	 */
	@Override
	public Long countGroupsWithoutEmailListByDomainName(String domain) throws Exception {
		return (Long) getHibernateTemplate().find("Select count(g) FROM Group g WHERE size(g.emailLists) = 0 AND g.domain.name = ?", domain).get(0);
	}

	/**
	 * 
	 * @param owner
	 * @return Returns the groups of a owner
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupsByOwner(Person owner) throws Exception {
		return getHibernateTemplate().find("FROM Group g WHERE ? in elements(g.owners)", owner);
	}

	/**
	 * 
	 * @param participant
	 * @return Returns the groups of a person (participant)
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupsByParticipant(Person participant) throws Exception {
		return getHibernateTemplate().find("FROM Group g WHERE ? in elements(g.participants)", participant);
	}
}
