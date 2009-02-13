package com.integrareti.integraframework.controller.adm.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;

import com.integrareti.integraframework.authentication.UserGroup;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.google.GoogleUserAccountServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraGroupServiceInterface;
import com.integrareti.integraframework.service.siga.SigaService;
import com.integrareti.integraframework.valueobject.NameVO;

/**
 * This class gives to groupAdmManagerWindow.zul (view) the access to business
 * components
 * 
 * @author Thiago
 * 
 */
public class GroupManagerController {
	private SigaService sigaService;
	private IntegraGroupServiceInterface integraGroupServiceInterface;
	private GoogleUserAccountServiceInterface googleUserAccountServiceInterface;

	/**
	 * Creates a new GroupManagerController
	 * 
	 * @param sigaService
	 * @param integraGroupServiceInterface
	 * @param googleUserAccountServiceInterface
	 */
	public GroupManagerController(SigaService sigaService, IntegraGroupServiceInterface integraGroupServiceInterface, GoogleUserAccountServiceInterface googleUserAccountServiceInterface) {
		this.sigaService = sigaService;
		this.integraGroupServiceInterface = integraGroupServiceInterface;
		this.googleUserAccountServiceInterface = googleUserAccountServiceInterface;
	}

	/**
	 * 
	 * @param registry
	 * @return Returns a person by registry
	 * @throws Exception
	 */
	public Person getPersonByRegistry(String registry) throws Exception {
		return googleUserAccountServiceInterface.getByRegistry(registry);
	}

	/**
	 * 
	 * @param description
	 * @return Returns groups by description
	 * @throws Exception
	 */
	public List<Group> getGroupsByDescription(String description) throws Exception {
		return integraGroupServiceInterface.getGroupsByDescription(description);
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 * @throws DataAccessException
	 */
	public List<Group> getGroupsByDescriptionAndName(String description, String name) throws Exception {
		return integraGroupServiceInterface.getGroupsByDescriptionAndName(description, name);
	}

	/**
	 * 
	 * @param names
	 * @return Returns groups by pieces of name
	 * @throws Exception
	 */
	public List<Group> getGroupsByPiecesOfNames(List<String> names) throws Exception {
		return integraGroupServiceInterface.getGroupsByPiecesOfNames(names);
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 * @throws DataAccessException
	 */
	public List<Group> getGroupsByDescriptionAndName(String description, List<String> names) throws Exception {
		return integraGroupServiceInterface.getGroupsByDescriptionAndName(description, names);
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the person name
	 * @throws Exception
	 */
	public String getPersonName(String registry, Set<UserGroup> userGroups) throws Exception {
		List<String> list = new ArrayList<String>();
		for (UserGroup userGroup : userGroups)
			list.add(userGroup.getName());
		if (list.isEmpty())
			return null;
		return sigaService.getPersonName(registry, list);
	}

	/**
	 * 
	 * @param people
	 * @return Returns a list of person with theirs names
	 * @throws Exception
	 */
	public Set<Person> setPeopleName(Set<Person> people) throws Exception {
		return sigaService.initPeopleName(people);
	}

	/**
	 * Saves a group, updates the email lists
	 * 
	 * @param group
	 * @param addedPeople
	 * @param removedPeople
	 * @return Returns a map with errors
	 * @throws Exception
	 */
	public Map<String, Group> saveGroup(Group group, Set<Person> addedPeople, Set<Person> removedPeople, Set<Person> addedOwners, Set<Person> removedOwners) throws Exception {
		return integraGroupServiceInterface.saveGroupAndUpDateEmailLists(group, addedPeople, removedPeople, addedOwners, removedOwners);
	}

	/**
	 * Checks if the group name is valid
	 * 
	 * @param name
	 * @return true if it is valid. Otherwise false
	 * @throws Exception
	 */
	public boolean isValidGroupName(String name) throws Exception {
		if (!StringUtils.hasText(name))
			return false;
		if (integraGroupServiceInterface.getGroupIdByName(name, false) != null)
			return false;
		for (int i = 0; i < name.length(); i++)
			if (name.charAt(i) == ' ')
				return false;
		return true;
	}

	/**
	 * Deletes a group
	 * 
	 * @param group
	 * 
	 */
	public Map<String, Group> deleteGroup(Group group) {
		return integraGroupServiceInterface.deleteGroupWithEmailLists(group);
	}

	/**
	 * 
	 * @param name
	 * @return Returns a list of nameVO object with the name and registry by
	 *         name (using like primitive)
	 * @throws Exception
	 */
	public List<NameVO> getNameAndRegistryByName(String name) throws Exception {
		List<NameVO> list = sigaService.getNameAndRegistryByName(name);
		if (list != null) {
			int aux = list.size();
			boolean stillOpenConnection = true;
			for (Iterator<NameVO> iterator = list.iterator(); iterator.hasNext();) {
				NameVO n = (NameVO) iterator.next();
				if (aux == 1)
					stillOpenConnection = false;
				if (googleUserAccountServiceInterface.isPersonSaved(n.getRegistry(), stillOpenConnection) == null)
					iterator.remove();
				aux--;
			}
		}
		return list;
	}

	/**
	 * Updates the participants - get from siga
	 * 
	 * @param group
	 * @throws Exception
	 */
	public Map<String, Group> upDateParticipants(Group group) throws Exception {
		if (!group.isManuallyCreated()) {
			String name = group.getName();
			// getting datas from name
			String classroom = name.substring(name.length() - 1);
			String semester = name.substring(name.length() - 2, name.length() - 1);
			String year = name.substring(name.length() - 6, name.length() - 2);
			String subjectCode = name.substring(0, name.length() - 6);
			Set<Person> participants = new HashSet<Person>();
			Set<Person> owners = new HashSet<Person>();
			List<String> registries = sigaService.getRegistriesBySubjectCode(subjectCode, year, semester, classroom, group.getParticipantsRegistries());
			List<Person> peopleSaved = googleUserAccountServiceInterface.arePeopleSaved(registries);
			if (!peopleSaved.isEmpty())
				participants.addAll(peopleSaved);
			// defining the owners
			List<NameVO> ownersNameVO = sigaService.getSubjectOwner(subjectCode, year, semester, classroom);
			registries = new ArrayList<String>();
			if (ownersNameVO != null)
				for (NameVO n : ownersNameVO)
					registries.add(n.getRegistry());
			peopleSaved = googleUserAccountServiceInterface.arePeopleSaved(registries);
			if (!peopleSaved.isEmpty())
				owners.addAll(peopleSaved);
			// saves group
			return integraGroupServiceInterface.saveGroupAndUpDateEmailLists(group, participants, new HashSet<Person>(0), owners, new HashSet<Person>(0));
		}
		return new HashMap<String, Group>(0);
	}

	/**
	 * Binds the group to hibernate context
	 * 
	 * @param group
	 */
	public void reattach(Group group) {
		integraGroupServiceInterface.reattach(group);
	}
}
