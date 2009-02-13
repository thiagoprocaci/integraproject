package com.integrareti.integraframework.controller.adm.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.integrareti.integraframework.authentication.UserGroup;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.google.GoogleUserAccountServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraGroupServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraUserGroupServiceInterface;
import com.integrareti.integraframework.service.siga.SigaService;
import com.integrareti.integraframework.valueobject.NameVO;

/**
 * This class gives to userEditWindow.zul (view) the access to business
 * components
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class UserEditController {
	private SigaService sigaService;
	private GoogleUserAccountServiceInterface googleUserAccountServiceInterface;
	private IntegraGroupServiceInterface integraGroupServiceInterface;
	private IntegraUserGroupServiceInterface userGroupService;

	/**
	 * Creates a new UserEditController
	 * 
	 * @param sigaService
	 * @param googleUserAccountServiceInterface
	 */
	public UserEditController(SigaService sigaService, GoogleUserAccountServiceInterface googleUserAccountServiceInterface, IntegraGroupServiceInterface integraGroupServiceInterface, IntegraUserGroupServiceInterface userGroupService) {
		this.sigaService = sigaService;
		this.googleUserAccountServiceInterface = googleUserAccountServiceInterface;
		this.integraGroupServiceInterface = integraGroupServiceInterface;
		this.userGroupService = userGroupService;
	}

	/**
	 * 
	 * @param name
	 * @return Returns a list of person by name
	 * @throws Exception
	 */
	public List<Person> getPersonByName(String name) throws Exception {
		List<Person> people = new ArrayList<Person>(0);
		List<NameVO> namesVOs = sigaService.getNameAndRegistryByName(name);
		int aux = namesVOs.size();
		boolean stillOpenConnection = true;
		for (NameVO nameVO : namesVOs) {
			if (aux == 1)
				stillOpenConnection = false;
			Integer id = googleUserAccountServiceInterface.isPersonSaved(nameVO.getRegistry(), stillOpenConnection);
			if (id != null) {
				Person p = googleUserAccountServiceInterface.getById(id);
				p.setName(nameVO.getName());
				people.add(p);
			}
			aux--;
		}
		return people;
	}

	/**
	 * 
	 * @param registry
	 * @return Returns person by registry
	 * @throws Exception
	 */
	public Person getPersonByRegistry(String registry) throws Exception {
		Person p = googleUserAccountServiceInterface.getByRegistry(registry);
		if (p != null) {
			List<String> userGroups = new ArrayList<String>();
			for (UserGroup userGroup : p.getUserGroups())
				userGroups.add(userGroup.getName());
			p.setName(sigaService.getPersonName(p.getRegistry(), userGroups));
		}
		return p;
	}

	/**
	 * 
	 * @param person
	 * @return Returns person`s groups
	 * @throws Exception
	 */
	public List<Group> getPersonGroups(Person person) throws Exception {
		List<Group> list = googleUserAccountServiceInterface.getGroups(person.getRegistry());
		return list;
	}

	/**
	 * 
	 * @param name
	 * @return UserGroups by name
	 * @throws Exception
	 */
	public List<UserGroup> getUserGroupByName(String name) throws Exception {
		return userGroupService.getByName(name);
	}

	/**
	 * 
	 * 
	 * @param clue
	 * @return Returns groups by any clue
	 * @throws Exception
	 */
	public List<Group> getGroupsByClue(String clue) throws Exception {
		List<Group> list = integraGroupServiceInterface.getGroupsByClue(clue);
		return list;
	}

	/**
	 * Saves a person and update groups
	 * 
	 * @param person
	 * @param removedGroups
	 * @param addedGroups
	 * @throws Exception
	 */
	public Map<String, Group> save(Person person, Set<Group> removedGroups, Set<Group> addedGroups) throws Exception {
		googleUserAccountServiceInterface.save(person);
		Map<String, Group> errors = new HashMap<String, Group>(0);
		for (Group group : removedGroups) {
			Set<Person> people = new HashSet<Person>(0);
			people.add(person);
			Map<String, Group> aux = integraGroupServiceInterface.removeParticipantFromGroupAndEmailList(integraGroupServiceInterface.getById(group.getId()), people);
			if (!aux.isEmpty())
				errors.putAll(aux);
		}
		for (Group group : addedGroups) {
			Set<Person> people = new HashSet<Person>(0);
			people.add(person);
			Map<String, Group> aux = integraGroupServiceInterface.createEmailListToGroup(integraGroupServiceInterface.getById(group.getId()), people);
			if (!aux.isEmpty())
				errors.putAll(aux);
			aux = integraGroupServiceInterface.addParticipantToGroupAndEmailList(integraGroupServiceInterface.getById(group.getId()), people);
			if (!aux.isEmpty())
				errors.putAll(aux);
		}
		return errors;
	}

	/**
	 * Deletes a person
	 * 
	 * @param person
	 * @throws Exception
	 */
	public Map<String, Group> delete(Person person) throws Exception {
		Map<String, Group> map = new HashMap<String, Group>(0);
		List<Group> list = getPersonGroups(person);
		for (Group group : list) {
			Set<Person> people = new HashSet<Person>(0);
			people.add(person);
			Map<String, Group> aux = integraGroupServiceInterface.removeParticipantFromGroupAndEmailList(integraGroupServiceInterface.getById(group.getId()), people);
			if (!aux.isEmpty())
				map.putAll(aux);
		}
		if (map.isEmpty())
			googleUserAccountServiceInterface.delete(googleUserAccountServiceInterface.getById(person.getId()));
		return map;
	}
}
