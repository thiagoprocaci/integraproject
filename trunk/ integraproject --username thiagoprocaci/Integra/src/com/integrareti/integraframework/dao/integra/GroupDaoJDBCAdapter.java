package com.integrareti.integraframework.dao.integra;

import java.util.List;

import org.hibernate.Session;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;

/**
 * Class adapter of groupDao
 * 
 * @author Thiago
 * 
 */
public class GroupDaoJDBCAdapter implements GroupDao {
	private static final String MSG = "Not implementedn here - see GroupDaoHibernate";

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups by domain name
	 * @throws Exception
	 */
	@Override
	public Long countGroupsByDomainName(String domain) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups without emaillist by domain name
	 * @throws Exception
	 */
	@Override
	public Long countGroupsWithoutEmailListByDomainName(String domain) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns all groups without email list by domain name
	 * @throws Exception
	 */
	@Override
	public List<Group> getAllGroupsWithoutEmailListByDomainName(String domainName) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param groupName
	 * @return Returns a list of groups by name
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupByName(String groupName) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param groupName
	 * @param domainName
	 * @return Returns a group by a name and domain name
	 * @throws Exception
	 */
	@Override
	public Group getGroupByNameAndDomainName(String groupName, String domainName) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param groupName
	 * @return Returns the group id by name
	 * @throws Exception
	 */
	@Override
	public Integer getGroupIdByName(String groupName) throws Exception {
		return null;
	}

	/**
	 * 
	 * @param description
	 * @return Returns groups by description
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByDescription(String description) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByDescriptionAndName(String description, String name) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByDescriptionAndName(String description, List<String> names) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups by domain name
	 * @throws Exception
	 */
	@Override
	public List<Group> getPageByDomainName(String domainName, int first, int offset) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups without emaillist by domain
	 * @throws Exception
	 */
	@Override
	public List<Group> getPageOfGroupsWithoutEmailListByDomainName(String domainName, int first, int offset) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * Saves a group
	 */
	@Override
	public void delete(Group row) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * Returns all groups
	 */
	@Override
	public List<Group> getAll() throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * Returns all groups by domain
	 */
	@Override
	public List<Group> getAllByDomainName(String domainName) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * Returns group by id
	 */
	@Override
	public Group getById(Integer id) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * Returns hibernate session
	 */
	@Override
	public Session getHibernateSession() {
		throw new RuntimeException(MSG);
	}

	/**
	 * Saves a group
	 */
	@Override
	public void save(Group row) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param names
	 * @return Returns groups by pieces of name - using like primitive
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByPiecesOfNames(List<String> names) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * @param owner
	 * @return Returns the groups of a owner
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByOwner(Person owner) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * 
	 * 
	 * @param clue
	 * @return Returns groups by any clue
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByClue(String clue) throws Exception {
		throw new Exception(MSG);
	}

	/**
	 * close JDBC session
	 */
	@Override
	public void closeSession() {
		throw new RuntimeException(MSG);
	}

	/**
	 * Open JDBC session
	 */
	@Override
	public void openSession() {
		throw new RuntimeException(MSG);
	}

	/**
	 * 
	 * @param participant
	 * @return Returns the groups of a person (participant)
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByParticipant(Person participant) throws Exception {
		throw new Exception(MSG);
	}
}
