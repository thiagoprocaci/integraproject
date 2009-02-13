package com.integrareti.integraframework.service.siga;

import java.util.List;
import java.util.Set;

import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.valueobject.GroupVO;
import com.integrareti.integraframework.valueobject.NameVO;
import com.integrareti.integraframework.valueobject.PersonVO;
import com.integrareti.integraframework.valueobject.SectorVO;

/**
 * This interface offers the basics services to access siga database
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public interface SigaService {

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the name
	 * 
	 */
	public String getPersonName(String registry, List<String> userGroups)
			throws Exception;

	/**
	 * 
	 * 
	 * @param registry
	 * @param userGroup
	 * @return Returns the course name
	 * 
	 * 
	 */
	public String getCourseName(String registry, List<String> userGroups)
			throws Exception;

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the departament name
	 * 
	 */
	public String getDepartamentName(String registry, List<String> userGroups)
			throws Exception;

	/**
	 * Authentication at system
	 * 
	 * @param username
	 * @param password
	 * @return true or false
	 * 
	 */
	public boolean loginPerson(String username, String password)
			throws Exception;

	/**
	 * 
	 * 
	 * @param registry
	 * @return Returns siga's userGroups of a person
	 * 
	 */
	public List<String> getUserGroup(String registry) throws Exception;

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @throws Exception
	 * @return Returns a list groupVO with the subjects
	 */
	public List<GroupVO> getSubjects(String registry, List<String> userGroups,
			String year, String semester) throws Exception;

	/**
	 * 
	 * 
	 * @param registry
	 * @throws Exception
	 * @return Returns the basics data of a person
	 *         (givenName,familyName,course/departament,userGroups)
	 */
	public PersonVO getPersonBasicsData(String registry) throws Exception;

	/**
	 * 
	 * 
	 * @param registry
	 * @throws Exception
	 * @return Returns an encrypted password (MD5)
	 */
	public String getPersonPassword(String registry) throws Exception;

	/**
	 * 
	 * 
	 * @param registry
	 * @param userGroups
	 * @throws Exception
	 * @return Returns the sector. Example: ICE
	 * 
	 */
	public SectorVO getPersonSector(String registry, List<String> userGroups)
			throws Exception;

	/**
	 * 
	 * @param registry
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @param sector
	 * @param userGroups
	 * @throws Exception
	 * @return Returns true if person param registry has link to param sector
	 */
	public boolean hasPersonLinkWithSector(String registry, String year,
			String semester, String sector, List<String> userGroups)
			throws Exception;

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @throws Exception
	 * @return Returns all registry of people that is studying a subject
	 */
	public List<String> getRegistriesBySubjectCode(String subjectCode,
			String year, String semester, String classroom) throws Exception;

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @throws Exception
	 * @return Returns all subject of a period and sector
	 */
	public List<GroupVO> getSubjectByPeriodAndSector(String year,
			String semester, String sector) throws Exception;

	/**
	 * 
	 * @param subjectsCode
	 * @param year
	 * @param semester
	 * @throws Exception
	 * @return Returns all registry of people that is studying a list of subject
	 */
	public List<String> getRegistriesBySubjectCode(List<String> subjectsCode,
			String year, String semester, String classroom) throws Exception;

	/**
	 * 
	 * @param sector
	 * @throws Exception
	 * @return Returns a list of departament name by sector
	 * 
	 */
	public List<String> getDepartamentsBySector(String sector) throws Exception;

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @param departament
	 * @throws Exception
	 * @return Returns all subject of a period , sector and departament
	 * 
	 */
	public List<GroupVO> getSubjectByPeriodAndSectorAndDepartment(String year,
			String semester, String sector, String department) throws Exception;

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param subjectCode
	 * @throws Exception
	 * @return Returns all subject of a period , sector with the specified
	 *         subject code
	 */
	public List<GroupVO> getSubjectByPeriodAndSectorAndSubjectCode(String year,
			String semester, String subjectCode, String sector)
			throws Exception;

	/**
	 * 
	 * @param name
	 * @return Returns a list of nameVO object with the name and registry by
	 *         name (using like primitive)
	 * @throws Exception
	 */
	public List<NameVO> getNameAndRegistryByName(String name) throws Exception;

	/**
	 * 
	 * @param people
	 * @return Returns a list of person with theirs names
	 * @throws Exception
	 */
	public Set<Person> initPeopleName(Set<Person> people) throws Exception;

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @return Returns a list of nameVOs(registry,name) representing the subject
	 *         owners
	 * @throws Exception
	 */
	public List<NameVO> getSubjectOwner(String subjectCode, String year,
			String semester, String classroom) throws Exception;

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @return Returns a list of nameVOs(registry,name) representing the subject
	 * @throws Exception
	 */
	public List<NameVO> getSubjectOwner(String subjectCode, String year,
			String semester, String classroom, boolean stillOpenConnection)
			throws Exception;

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @param registriesNotWanted
	 *            (registries that doesn't need to return)
	 * @return Returns all registry of people that is studying a list of subject
	 * @throws Exception
	 */
	public List<String> getRegistriesBySubjectCode(String subjectCode,
			String year, String semester, String classroom,
			List<String> registriesNotWanted) throws Exception;

	/**
	 * 
	 * @return Returns all registry of people that is studying a list of subject
	 * @throws Exception
	 */
	public List<String> getRegistriesBySubjectCode(String subjectCode,
			String year, String semester, String classroom, boolean stillOpenConnection)
			throws Exception;

}
