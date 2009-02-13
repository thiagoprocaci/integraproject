package com.integrareti.integraframework.service.siga;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.integrareti.integraframework.authentication.UserGroup;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.dao.siga.SigaDao;
import com.integrareti.integraframework.valueobject.GroupVO;
import com.integrareti.integraframework.valueobject.NameVO;
import com.integrareti.integraframework.valueobject.PersonVO;
import com.integrareti.integraframework.valueobject.SectorVO;

/**
 * This class offers the basics services to access siga database
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class SigaServiceImpl implements SigaService {
	private SigaDao sigaDao;

	/**
	 * Creates a new SigaServiceImpl
	 * 
	 * @param sigaDao
	 */
	public SigaServiceImpl(SigaDao sigaDao) {
		this.sigaDao = sigaDao;
	}

	/**
	 * Authentication at system
	 * 
	 * @param username
	 * @param password
	 * @return true or false
	 * @throws Exception
	 */
	@Override
	public boolean loginPerson(String username, String password) throws Exception {
		if (username == null || password == null)
			return false;
		boolean login = false;
		sigaDao.openConnection();
		login = sigaDao.loginPerson(username, password);
		sigaDao.closeConnection();
		return login;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @return Returns the basics data of a person
	 *         (givenName,familyName,course/departament,userGroups, registry)
	 * @throws Exception
	 */
	@Override
	public PersonVO getPersonBasicsData(String registry) throws Exception {
		if (registry == null)
			return null;
		PersonVO p = new PersonVO();
		p.setRegistry(registry);
		sigaDao.openConnection();
		p.setUserPositionGroups(sigaDao.getUserPositionGroups(registry));
		if (p.getUserPositionGroups() != null && !p.getUserPositionGroups().isEmpty()) {
			if (p.getUserPositionGroups().contains(UserGroup.PROFESSOR_GROUP)) {
				p.setDeptoOrCourse(sigaDao.getDepartamentName(registry, p.getUserPositionGroups()));
			} else {
				if (p.getUserPositionGroups().contains(UserGroup.STUDENT_GROUP)) {
					p.setDeptoOrCourse(sigaDao.getCourseName(registry, p.getUserPositionGroups()));
				}
			}
			p.setSector(sigaDao.getPersonSector(registry, p.getUserPositionGroups()));
			p.setName(sigaDao.getPersonName(registry, p.getUserPositionGroups()));
		} else
			p = null;
		sigaDao.closeConnection();
		return p;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the course name
	 * @throws Exception
	 * 
	 */
	@Override
	public String getCourseName(String registry, List<String> userGroups) throws Exception {
		if (registry == null || userGroups == null || userGroups.isEmpty())
			return null;
		String aux = null;
		sigaDao.openConnection();
		aux = sigaDao.getCourseName(registry, userGroups);
		sigaDao.closeConnection();
		return aux;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the departament name
	 * @throws Exception
	 */
	@Override
	public String getDepartamentName(String registry, List<String> userGroups) throws Exception {
		if (registry == null || userGroups == null || userGroups.isEmpty())
			return null;
		String aux = null;
		sigaDao.openConnection();
		aux = sigaDao.getDepartamentName(registry, userGroups);
		sigaDao.closeConnection();
		return aux;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the name
	 * @throws Exception
	 * 
	 */
	@Override
	public String getPersonName(String registry, List<String> userGroups) throws Exception {
		if (registry == null || userGroups == null || userGroups.size() == 0)
			return null;
		String aux = null;
		sigaDao.openConnection();
		aux = sigaDao.getPersonName(registry, userGroups);
		sigaDao.closeConnection();
		return aux;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @return Returns an encrypted password (MD5)
	 * @throws Exception
	 * 
	 */
	@Override
	public String getPersonPassword(String registry) throws Exception {
		if (registry == null)
			return null;
		String aux = null;
		sigaDao.openConnection();
		aux = sigaDao.getPersonPassword(registry);
		sigaDao.closeConnection();
		return aux;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the sector. Example: ICE
	 * @throws Exception
	 * 
	 * 
	 */
	@Override
	public SectorVO getPersonSector(String registry, List<String> userGroups) throws Exception {
		if (registry == null || userGroups == null || userGroups.isEmpty())
			return null;
		SectorVO aux = null;
		sigaDao.openConnection();
		aux = sigaDao.getPersonSector(registry, userGroups);
		sigaDao.closeConnection();
		return aux;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @return Returns a list groupVO with the subjects
	 * @throws Exception
	 * 
	 */
	@Override
	public List<GroupVO> getSubjects(String registry, List<String> userGroups, String year, String semester) throws Exception {
		if (registry == null || userGroups == null || userGroups.isEmpty() || year == null || semester == null || (!semester.equals("1") && !semester.equals("2")))
			return new ArrayList<GroupVO>(0);
		sigaDao.openConnection();
		List<GroupVO> list = sigaDao.getSubjects(registry, userGroups, year, semester);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @return Returns siga's userGroups of a person
	 * @throws Exception
	 * 
	 */
	@Override
	public List<String> getUserGroup(String registry) throws Exception {
		if (registry == null)
			return new ArrayList<String>(0);
		sigaDao.openConnection();
		List<String> list = sigaDao.getUserPositionGroups(registry);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @param registry
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @param sector
	 * @param userGroups
	 * @return Returns true if person param registry has link to param sector
	 * 
	 */
	@Override
	public boolean hasPersonLinkWithSector(String registry, String year, String semester, String sector, List<String> userGroups) throws Exception {
		if (registry == null || year == null || semester == null || sector == null || userGroups == null || userGroups.isEmpty() || (!semester.equals("1") && !semester.equals("2")))
			return false;
		boolean aux = false;
		sigaDao.openConnection();
		aux = sigaDao.hasPersonLinkWithSector(registry, year, semester, sector, userGroups);
		sigaDao.closeConnection();
		return aux;
	}

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @return Returns all registry of people that is studying a subject
	 * @throws Exception
	 * 
	 */
	@Override
	public List<String> getRegistriesBySubjectCode(String subjectCode, String year, String semester, String classroom) throws Exception {
		if (subjectCode == null || year == null || semester == null || subjectCode == null || (!semester.equals("1") && !semester.equals("2")))
			return new ArrayList<String>(0);
		sigaDao.openConnection();
		List<String> list = sigaDao.getRegistriesBySubjectCode(subjectCode, year, semester, classroom);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @return Returns all subject of a period and sector
	 * @throws Exception
	 */
	@Override
	public List<GroupVO> getSubjectByPeriodAndSector(String year, String semester, String sector) throws Exception {
		if (sector == null || year == null || semester == null || (!semester.equals("1") && !semester.equals("2")))
			return new ArrayList<GroupVO>(0);
		sigaDao.openConnection();
		List<GroupVO> list = sigaDao.getSubjectByPeriodAndSector(year, semester, sector);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @param subjectsCode
	 * @param year
	 * @param semester
	 * @return Returns all registry of people that is studying a list of subject
	 * @throws Exception
	 * 
	 */
	@Override
	public List<String> getRegistriesBySubjectCode(List<String> subjectsCode, String year, String semester, String classroom) throws Exception {
		if (subjectsCode == null || year == null || semester == null || subjectsCode.isEmpty() || classroom == null || (!semester.equals("1") && !semester.equals("2")))
			return new ArrayList<String>(0);
		sigaDao.openConnection();
		List<String> list = sigaDao.getRegistriesBySubjectCode(subjectsCode, year, semester, classroom);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @param sector
	 * @return Returns a list of departament name by sector
	 * @throws Exception
	 * 
	 */
	@Override
	public List<String> getDepartamentsBySector(String sector) throws Exception {
		if (sector == null)
			return new ArrayList<String>(0);
		sigaDao.openConnection();
		List<String> list = sigaDao.getDepartamentsBySector(sector);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @param departament
	 * @return Returns all subject of a period , sector and departament
	 * @throws Exception
	 * 
	 */
	@Override
	public List<GroupVO> getSubjectByPeriodAndSectorAndDepartment(String year, String semester, String sector, String department) throws Exception {
		if (year == null || semester == null || sector == null || department == null || (!semester.equals("1") && !semester.equals("2")))
			return new ArrayList<GroupVO>(0);
		sigaDao.openConnection();
		List<GroupVO> list = sigaDao.getSubjectByPeriodAndSectorAndDepartment(year, semester, sector, department);
		sigaDao.closeConnection();
		return list;
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
	@Override
	public List<GroupVO> getSubjectByPeriodAndSectorAndSubjectCode(String year, String semester, String subjectCode, String sector) throws Exception {
		if (year == null || semester == null || sector == null || subjectCode == null || (!semester.equals("1") && !semester.equals("2")))
			return new ArrayList<GroupVO>(0);
		sigaDao.openConnection();
		List<GroupVO> list = sigaDao.getSubjectByPeriodAndSectorAndSubjectCode(year, semester, subjectCode, sector);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @param name
	 * @return Returns a list of nameVO object with the name and registry by
	 *         name (using like primitive)
	 * @throws Exception
	 */
	@Override
	public List<NameVO> getNameAndRegistryByName(String name) throws Exception {
		if (name == null)
			return new ArrayList<NameVO>(0);
		sigaDao.openConnection();
		List<NameVO> list = sigaDao.getNameAndRegistryByName(name);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @param people
	 * @return Returns a list of person with theirs names
	 * @throws Exception
	 */
	@Override
	public Set<Person> initPeopleName(Set<Person> people) throws Exception {
		if (people == null || people.isEmpty())
			return people;
		sigaDao.openConnection();
		for (Person person : people) {
			if (!StringUtils.hasText(person.getName())) {
				List<String> userGroups = new ArrayList<String>();
				for (UserGroup u : person.getUserGroups())
					userGroups.add(u.getName());
				person.setName(sigaDao.getPersonName(person.getRegistry(), userGroups));
			}
		}
		sigaDao.closeConnection();
		return people;
	}

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @return Returns a list of nameVOs(registry,name) representing the subject
	 *         owners
	 * @throws Exception
	 */
	@Override
	public List<NameVO> getSubjectOwner(String subjectCode, String year, String semester, String classroom) throws Exception {
		if (subjectCode == null || year == null || semester == null)
			return new ArrayList<NameVO>(0);
		sigaDao.openConnection();
		List<NameVO> list = sigaDao.getSubjectOwner(subjectCode, year, semester, classroom);
		sigaDao.closeConnection();
		return list;
	}

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
	@Override
	public List<String> getRegistriesBySubjectCode(String subjectCode, String year, String semester, String classroom, List<String> registriesNotWanted) throws Exception {
		if (subjectCode == null || year == null || semester == null || subjectCode == null || registriesNotWanted == null || (!semester.equals("1") && !semester.equals("2")))
			return new ArrayList<String>(0);
		sigaDao.openConnection();
		List<String> list = sigaDao.getRegistriesBySubjectCode(subjectCode, year, semester, classroom, registriesNotWanted);
		sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @return Returns all registry of people that is studying a list of subject -
	 *         Needs to open and close siga database connection
	 * @throws Exception
	 */
	@Override
	public List<String> getRegistriesBySubjectCode(String subjectCode, String year, String semester, String classroom, boolean stillOpenConnection) throws Exception {
		if (subjectCode == null || year == null || semester == null || subjectCode == null || (!semester.equals("1") && !semester.equals("2")))
			return new ArrayList<String>(0);
		if (!sigaDao.isConnectionOpen())
			sigaDao.openConnection();
		List<String> list = sigaDao.getRegistriesBySubjectCode(subjectCode, year, semester, classroom);
		if (!stillOpenConnection)
			sigaDao.closeConnection();
		return list;
	}

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @return Returns a list of nameVOs(registry,name) representing the subject -
	 *         Needs to open and close siga database connection owners
	 * @throws Exception
	 */
	@Override
	public List<NameVO> getSubjectOwner(String subjectCode, String year, String semester, String classroom, boolean stillOpenConnection) throws Exception {
		if (subjectCode == null || year == null || semester == null)
			return new ArrayList<NameVO>(0);
		if (!sigaDao.isConnectionOpen())
			sigaDao.openConnection();
		List<NameVO> list = sigaDao.getSubjectOwner(subjectCode, year, semester, classroom);
		if (!stillOpenConnection)
			sigaDao.closeConnection();
		return list;
	}
}