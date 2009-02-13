package com.integrareti.integraframework.service.integra;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;

/**
 * This interface offers services to manipulates groups at integra database
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public interface IntegraGroupServiceInterface extends IntegraServiceInterface<Group, Integer> {
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
	 * @return Returns the group id by name - using hibernate
	 * @throws Exception
	 */
	public Integer getGroupIdByName(String groupName) throws Exception;

	/**
	 * 
	 * @param groupName
	 * @return Returns the group id by name - using JDBC
	 * @throws Exception
	 */
	public Integer getGroupIdByName(String groupName, boolean stillOpenConnection) throws Exception;

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
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 */
	public List<Group> getGroupsByDescriptionAndName(String description, List<String> names) throws Exception;

	/**
	 * Saves a group and updates the email lists if necessary
	 * 
	 * @param group
	 * @return Returns a map of errors <String (error description),Group (object
	 *         value)>
	 */
	public Map<String, Group> saveGroupAndUpDateEmailLists(Group group, Set<Person> addedPeople, Set<Person> removedPeople, Set<Person> addOwners, Set<Person> removedOwners) throws Exception;

	/**
	 * Deletes a group
	 * 
	 * @param group
	 * @return Returns a map of errors <String (error description),Group (object
	 *         value)>
	 */
	public Map<String, Group> deleteGroupWithEmailLists(Group group);

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

	/**
	 * Creates more email lists to a group
	 * 
	 * @param group
	 * @param addedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	public Map<String, Group> createEmailListToGroup(final Group group, final Set<Person> addedPeople);

	/**
	 * Adds participants to group and email list
	 * 
	 * @param group
	 * @param addedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	public Map<String, Group> addParticipantToGroupAndEmailList(final Group group, final Set<Person> addedPeople);

	/**
	 * Removes participants from group and email list
	 * 
	 * @param group
	 * @param removedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	public Map<String, Group> removeParticipantFromGroupAndEmailList(final Group group, final Set<Person> removedPeople);

	/**
	 * Merge group
	 * 
	 * @param group
	 * @throws Exception
	 * @return merged group
	 */
	public Group mergeGroup(Group group) throws Exception;
}