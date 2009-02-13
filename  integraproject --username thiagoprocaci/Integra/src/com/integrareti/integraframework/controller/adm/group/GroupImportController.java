package com.integrareti.integraframework.controller.adm.group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.google.GoogleUserAccountServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraDomainServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraGroupServiceInterface;
import com.integrareti.integraframework.service.siga.SigaService;
import com.integrareti.integraframework.valueobject.GroupVO;
import com.integrareti.integraframework.valueobject.NameVO;

/**
 * This class gives to groups.zul (view) the access to business components
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class GroupImportController {

	private SigaService sigaService;
	private IntegraGroupServiceInterface integraGroupServi;
	private IntegraDomainServiceInterface integraDomainService;
	private GoogleUserAccountServiceInterface googleUserAccountService;

	/**
	 * Creates a new instance of GroupImportController
	 * 
	 * @param sigaService
	 * @param integraGroupService
	 * @param googleEmailListService
	 */
	public GroupImportController(SigaService sigaService,
			IntegraGroupServiceInterface integraGroupService,
			GoogleUserAccountServiceInterface googleUserAccountService,
			IntegraDomainServiceInterface integraDomainService) {
		this.sigaService = sigaService;
		this.integraGroupServi = integraGroupService;
		this.googleUserAccountService = googleUserAccountService;
		this.integraDomainService = integraDomainService;
	}

	/**
	 * 
	 * @return Returns all domains Returns null case it throws any exception
	 * @throws Exception
	 */
	public List<Domain> getDomains() throws Exception {
		return integraDomainService.getAll();
	}

	/**
	 * Imports the possibles groups
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @return Returns a list of groupVO
	 * @throws Exception
	 */
	public List<GroupVO> getSubjectByPeriodAndSector(String year,
			String semester, String sector) throws Exception {
		List<GroupVO> list = sigaService.getSubjectByPeriodAndSector(year,
				semester, sector);
		return getGroupsNotCreated(list, year, semester);
	}



	/**
	 * Imports the possibles groups
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @param department
	 * @return Returns all subject of a period , sector and departament
	 * @throws Exception
	 */
	public List<GroupVO> getSubjectByPeriodAndSectorAndDepartment(String year,
			String semester, String sector, String department) throws Exception {
		List<GroupVO> list = sigaService
				.getSubjectByPeriodAndSectorAndDepartment(year, semester,
						sector, department);
		return getGroupsNotCreated(list, year, semester);
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param subjectCode
	 * @return Returns all subject of a period , sector with the specified
	 *         subject code
	 * @throws Exception
	 */
	public List<GroupVO> getSubjectByPeriodAndSectorAndSubjectCode(String year,
			String semester, String subjectCode, String sector)
			throws Exception {
		List<GroupVO> list = sigaService
				.getSubjectByPeriodAndSectorAndSubjectCode(year, semester,
						subjectCode, sector);
		return getGroupsNotCreated(list, year, semester);
	}

	/**
	 * Creates the groups
	 * 
	 * @param groupsVO
	 * @param domain
	 * @param year
	 * @param semester
	 * @return A map of groups that could not be created
	 */
	public Map<String, GroupVO> createGroups(List<GroupVO> groupsVO,
			Domain domain, String year, String semester) {
		Map<String, GroupVO> errors = new HashMap<String, GroupVO>();
		boolean stillOpenConnection = true;
		int aux = groupsVO.size();
		for (GroupVO groupVO : groupsVO) {
			if (aux == 1)
				stillOpenConnection = false;
			Map<String, GroupVO> error = createGroup(groupVO, domain, year,
					semester, stillOpenConnection);
			if (!error.isEmpty())
				errors.putAll(error);
			aux--;
		}
		return errors;
	}

	/**
	 * Creates a group
	 * 
	 * @param groupVO
	 * @param domain
	 * @param year
	 * @param semester
	 * @return A map of groups that could not be created
	 */
	public Map<String, GroupVO> createGroup(GroupVO groupVO, Domain domain,
			String year, String semester, boolean stillOpenConnection) {
		Map<String, GroupVO> errors = new HashMap<String, GroupVO>();
		// creating a new group
		Group group = new Group();
		group.setDomain(domain);
		group.setActive(true);
		group.setManuallyCreated(false);
		group.setName(groupVO.getSubjectCode().trim() + year.trim()
				+ semester.trim() + groupVO.getClassroom().trim());
		group.setDescription(groupVO.getSubjectName().trim() + " - Turma "
				+ groupVO.getClassroom().trim() + " Semestre: "
				+ semester.trim() + " Ano: " + year.trim());
		Set<Person> participants = new HashSet<Person>();
		Set<Person> owners = new HashSet<Person>();
		try {
			boolean stillOpenAux = stillOpenConnection;
			if (stillOpenConnection == false)
				stillOpenConnection = true;
			// getting the students's registries of a specified subject
			List<String> registries = sigaService.getRegistriesBySubjectCode(
					groupVO.getSubjectCode().trim(), year.trim(), semester
							.trim(), groupVO.getClassroom().trim(),
					stillOpenConnection);
			if (registries != null && !registries.isEmpty()) {
				// getting the participants that has already been save at
				// integra database
				for (String reg : registries) {
					Integer id = googleUserAccountService.isPersonSaved(reg,
							stillOpenConnection);
					if (id != null) {
						Person p = googleUserAccountService.getById(id);
						participants.add(p);
					}
				}
			}
			// defining the owners
			List<NameVO> ownersNameVO = sigaService.getSubjectOwner(groupVO
					.getSubjectCode(), year, semester, groupVO.getClassroom(),
					stillOpenAux);
			if (ownersNameVO != null && !ownersNameVO.isEmpty()) {
				for (NameVO nameVO : ownersNameVO) {
					Integer id = googleUserAccountService.isPersonSaved(nameVO
							.getRegistry(), stillOpenAux);
					if (id != null) {
						Person p = googleUserAccountService.getById(id);

						owners.add(p);
					}
				}
			}
			if (!stillOpenAux)
				googleUserAccountService.closeConnection();
			// saving group
			Map<String, Group> map = integraGroupServi
					.saveGroupAndUpDateEmailLists(group, participants,
							new HashSet<Person>(0), owners,
							new HashSet<Person>(0));
			// getting errors
			Iterator<String> itKeys = map.keySet().iterator();
			while (itKeys.hasNext())
				errors.put(itKeys.next(), groupVO);
		} catch (Exception e) {
			String stringError = e.getMessage() + ".";
			errors.put(group.getName() + " " + stringError, groupVO);
		}
		return errors;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns all groups from a specified domain
	 * @throws Exception
	 */
	public List<Group> getGroups(String domainName) throws Exception {
		return integraGroupServi.getAllByDomainName(domainName);
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns the groups without email list
	 * @throws Exception
	 */
	public List<Group> getGroupsWithoutEmailList(String domainName)
			throws Exception {
		return integraGroupServi
				.getAllGroupsWithoutEmailListByDomainName(domainName);
	}

	/**
	 * 
	 * @param sector
	 * @return Returns a list of departament name by sector
	 * @throws Exception
	 */
	public List<String> getDepartamentsBySector(String sector) throws Exception {
		return sigaService.getDepartamentsBySector(sector);
	}

	/**
	 * 
	 * 
	 * @param list
	 * @return Returns the list of groupsVO that was not created
	 * @throws Exception
	 */
	private List<GroupVO> getGroupsNotCreated(List<GroupVO> list, String year,
			String semester) throws Exception {
		if (list != null && !list.isEmpty()) {
			int aux = list.size();
			boolean stillOpenConnection = true;
			for (Iterator<GroupVO> iterator = list.iterator(); iterator
					.hasNext();) {
				if (aux == 1)
					stillOpenConnection = false;
				GroupVO groupVO = (GroupVO) iterator.next();
				String name = groupVO.getSubjectCode().trim() + year.trim()
						+ semester.trim() + groupVO.getClassroom().trim();
				if (integraGroupServi.getGroupIdByName(name,
						stillOpenConnection) != null)
					iterator.remove();
				aux--;
			}
		}
		return list;
	}

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups without emaillist by domain
	 * @throws Exception
	 */
	public List<Group> getPageOfGroupsWithoutEmailListByDomainName(
			String domainName, int first, int offset) throws Exception {
		return integraGroupServi.getPageOfGroupsWithoutEmailListByDomainName(
				domainName, first, offset);
	}

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups by domain name
	 * @throws Exception
	 */
	public List<Group> getPageByDomainName(String domainName, int first,
			int offset) throws Exception {
		return integraGroupServi.getPageByDomainName(domainName, first, offset);
	}

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups by domain name
	 * @throws Exception
	 */
	public Long countGroupsByDomainName(String domain) throws Exception {
		return integraGroupServi.countGroupsByDomainName(domain);
	}

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups without emaillist by domain name
	 * @throws Exception
	 */
	public Long countGroupsWithoutEmailListByDomainName(String domain)
			throws Exception {
		return integraGroupServi
				.countGroupsWithoutEmailListByDomainName(domain);
	}

	public void reatach(Object o) {
		integraGroupServi.reattach(o);
	}

}
