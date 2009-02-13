package com.integrareti.integraframework.dao.integra;

import java.util.List;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;

/**
 * This interface offers method to access the group's data
 */
public interface GroupDao extends GenericDao<Group, Integer> {
	/**
	 * 
	 * @param groupName
	 * @return Returns a list of groups by name
	 * @throws Exception
	 */
	public List<Group> getGroupByName(String groupName) throws Exception;

	/**
	 * 
	 * @param groupName
	 * @param domainName
	 * @return Returns a group by a name and domain name
	 * @throws Exception
	 */
	public Group getGroupByNameAndDomainName(String groupName, String domainName) throws Exception;

	/**
	 * 
	 * @param groupName
	 * @return Returns the group id by name
	 * @throws Exception
	 */
	public Integer getGroupIdByName(String groupName) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @return Returns all groups without email list by domain name
	 * @throws Exception
	 */
	public List<Group> getAllGroupsWithoutEmailListByDomainName(String domainName) throws Exception;

	/**
	 * 
	 * @param description
	 * @return Returns groups by description
	 * @throws Exception
	 */
	public List<Group> getGroupsByDescription(String description) throws Exception;

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 */
	public List<Group> getGroupsByDescriptionAndName(String description, String name) throws Exception;

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description and name (using like)
	 * @throws Exception
	 */
	public List<Group> getGroupsByDescriptionAndName(String description, List<String> names) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups without emaillist by domain
	 * @throws Exception
	 */
	public List<Group> getPageOfGroupsWithoutEmailListByDomainName(String domainName, int first, int offset) throws Exception;

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups by domain name
	 * @throws Exception
	 */
	public List<Group> getPageByDomainName(String domainName, int first, int offset) throws Exception;

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups by domain name
	 * @throws Exception
	 */
	public Long countGroupsByDomainName(String domain) throws Exception;

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups without emaillist by domain name
	 * @throws Exception
	 */
	public Long countGroupsWithoutEmailListByDomainName(String domain) throws Exception;

	/**
	 * 
	 * @param owner
	 * @return Returns the groups of a owner
	 * @throws Exception
	 */
	public List<Group> getGroupsByOwner(Person owner) throws Exception;

	/**
	 * 
	 * @param participant
	 * @return Returns the groups of a person (participant)
	 * @throws Exception
	 */
	public List<Group> getGroupsByParticipant(Person participant) throws Exception;

	/**
	 * 
	 * @param names
	 * @return Returns groups by pieces of name - using like primitive
	 * @throws Exception
	 */
	public List<Group> getGroupsByPiecesOfNames(List<String> names) throws Exception;

	/**
	 * 
	 * 
	 * @param clue
	 * @return Returns groups by any clue (using like)
	 * @throws Exception
	 */
	public List<Group> getGroupsByClue(String clue) throws Exception;
}
