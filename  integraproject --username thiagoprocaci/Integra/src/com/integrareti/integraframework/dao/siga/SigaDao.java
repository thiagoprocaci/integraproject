package com.integrareti.integraframework.dao.siga;

import java.sql.SQLException;
import java.util.List;

import com.integrareti.integraframework.exceptions.ConnectionDataBaseException;
import com.integrareti.integraframework.valueobject.GroupVO;
import com.integrareti.integraframework.valueobject.NameVO;
import com.integrareti.integraframework.valueobject.SectorVO;

/**
 * This interface offers methods to access the siga dataDase
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public interface SigaDao {
	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the name
	 * @throws ConnectionDataBaseException
	 */
	public String getPersonName(String registry, List<String> userGroups) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the course
	 * @throws ConnectionDataBaseException
	 * 
	 */
	public String getCourseName(String registry, List<String> userGroups) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the departament
	 * @throws ConnectionDataBaseException
	 */
	public String getDepartamentName(String registry, List<String> userGroups) throws ConnectionDataBaseException, Exception;

	/**
	 * Authentication at system
	 * 
	 * @param username
	 * @param password
	 * @return true or false
	 * @throws ConnectionDataBaseException
	 */
	public boolean loginPerson(String username, String password) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * 
	 * @param registry
	 * @return Returns siga's userGroups of a person
	 * @throws ConnectionDataBaseException
	 */
	public List<String> getUserPositionGroups(String registry) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @return Returns a list groupVO with the subjects
	 * @throws ConnectionDataBaseException
	 */
	public List<GroupVO> getSubjects(String registry, List<String> userGroups, String year, String semester) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * 
	 * @param registry
	 * @return Returns personVO with encrypted password (MD5)
	 * @throws ConnectionDataBaseException
	 */
	public String getPersonPassword(String registry) throws ConnectionDataBaseException, Exception;

	/**
	 * Open database connection
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void openConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;

	/**
	 * Close the database connection
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException;

	/**
	 * 
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the sector. Example: ICE, DEP DCC
	 * @throws ConnectionDataBaseException
	 * 
	 */
	public SectorVO getPersonSector(String registry, List<String> userGroups) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @return Returns all registry of people that is studying a subject
	 * @throws ConnectionDataBaseException
	 */
	public List<String> getRegistriesBySubjectCode(String subjectCode, String year, String semester, String classroom) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param subjectsCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @return Returns all registry of people that is studying a list of subject
	 * @throws ConnectionDataBaseException
	 */
	public List<String> getRegistriesBySubjectCode(List<String> subjectsCode, String year, String semester, String classroom) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @param registriesNotWanted
	 *            (registries that doesn't need to return)
	 * @return Returns all registry of people that is studying a list of subject
	 * @throws ConnectionDataBaseException
	 * @throws Exception
	 */
	public List<String> getRegistriesBySubjectCode(String subjectCode, String year, String semester, String classroom, List<String> registriesNotWanted) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @return Returns all subject of a period and sector
	 */
	public List<GroupVO> getSubjectByPeriodAndSector(String year, String semester, String sector) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @param departament
	 * @return Returns all subject of a period , sector and departament
	 * @throws ConnectionDataBaseException
	 */
	public List<GroupVO> getSubjectByPeriodAndSectorAndDepartment(String year, String semester, String sector, String department) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param subjectCode
	 * @return Returns all subject of a period , sector with the specified
	 *         subject code
	 * @throws ConnectionDataBaseException
	 */
	public List<GroupVO> getSubjectByPeriodAndSectorAndSubjectCode(String year, String semester, String subjectCode, String sector) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param registry
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @param sector
	 * @param userGroups
	 * @return Returns true if person param registry has link to param sector
	 * @throws ConnectionDataBaseException
	 */
	public boolean hasPersonLinkWithSector(String registry, String year, String semester, String sector, List<String> userGroups) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param sector
	 * @return Returns a list of departament name by sector
	 * @throws ConnectionDataBaseException
	 */
	public List<String> getDepartamentsBySector(String sector) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param name
	 * @return Returns a list of nameVO object with the name and registry by
	 *         name (using like primitive)
	 */
	public List<NameVO> getNameAndRegistryByName(String name) throws ConnectionDataBaseException, Exception;

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @return Returns a list of nameVOs(registry,name) representing the subject
	 *         owners
	 * @throws ConnectionDataBaseException
	 * @throws Exception
	 */
	public List<NameVO> getSubjectOwner(String subjectCode, String year, String semester, String classroom) throws ConnectionDataBaseException, Exception;

	/**
	 * Checks if siga database connection is open
	 * 
	 * @return True if open. False if close
	 */
	public boolean isConnectionOpen() throws SQLException;
}
