package com.integrareti.integraframework.dao.siga;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.integrareti.integraframework.authentication.UserGroup;
import com.integrareti.integraframework.exceptions.ConnectionDataBaseException;
import com.integrareti.integraframework.util.StringUtil;
import com.integrareti.integraframework.valueobject.GroupVO;
import com.integrareti.integraframework.valueobject.NameVO;
import com.integrareti.integraframework.valueobject.SectorVO;

/**
 * This class offers methods to access the siga dataDase
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class SigaDaoJDBC implements SigaDao {

	// PRIVATE CONSTANTS
	private static final String FIRST_SEMESTER = "1";
	private static final String SECOND_SEMESTER = "3";
	private DataSource dataSource;
	private Connection connection = null;

	/**
	 * Converts the semester
	 * 
	 * @param s
	 * @return converted semester
	 */
	private String getSemester(String s) {
		s = s.trim();
		if (s.equals("2"))
			return SECOND_SEMESTER;
		if (s.equals("1"))
			return FIRST_SEMESTER;
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroup
	 * @return Returns the sql to find a person's name at siga
	 */
	private String getPersonNameSQL(String registry, List<String> userGroups) {
		if (registry != null && userGroups != null) {
			if (userGroups.contains(UserGroup.STUDENT_GROUP)) {
				return "select p.nome from cm_pessoa p, ga_aluno a where p.idpessoa=a.idpessoa and a.matricula='"
						+ registry.trim() + "'";
			}

			if (userGroups.contains(UserGroup.PROFESSOR_GROUP)) {
				return "select p.nome from cm_pessoa p, ga_docente a where p.idpessoa=a.idpessoa and a.idvinc='"
						+ registry.trim() + "'";
			}

			if (userGroups.contains(UserGroup.EMPLOYEE_GROUP)) {
				return "select p.nome from cm_pessoa p,rh_funcionario f,rh_vinculo v "
						+ " where f.idpessoa=p.idpessoa and v.idfuncionario=f.idfuncionario and v.idvinculo='"
						+ registry.trim() + "'";
			}
		}
		return null;
	}

	/**
	 * 
	 * @param course
	 * @param userGroup
	 * @return Returns the sql to find a person's course at siga
	 */
	private String getCourseNameSQL(String registry, List<String> userGroups) {
		if (registry != null && userGroups != null) {
			if (userGroups.contains(UserGroup.STUDENT_GROUP)) {
				return "select c.nome from GA_PROGRAMA p, GA_CURSO c where p.matricula='"
						+ registry.trim() + "' and c.curso=p.curso";
			}
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroup
	 * @return Returns the sql to find a person's departament at siga
	 */
	private String getDepartamentNameSQL(String registry,
			List<String> userGroups) {
		if (registry != null && userGroups != null) {
			if (userGroups.contains(UserGroup.PROFESSOR_GROUP)) {
				return "select d.nome from ga_docente do, ga_departamento d where do.iddepto=d.iddepto and do.idvinc="
						+ registry.trim();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return Returns the sql that makes authentication of a person
	 */
	private String getLoginPersonSQL(String username, String password) {
		if (username != null && password != null) {
			try {
				return "select b.idpessoa,b.nome from cm_usuario a, cm_pessoa b where a.login='"
						+ username.trim()
						+ "' and a.idpessoa=b.idpessoa and a.passmd5='"
						+ StringUtil.MD5Encrypt(password.trim()) + "'";
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @return Returns the sql to verifies if a person is a student
	 */
	private String isStudentSQL(String registry) {
		if (registry != null) {
			return "select matricula from GA_PROGRAMA p where p.matricula='"
					+ registry.trim() + "'";
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @return Returns the sql to verifies if a person is a professor
	 */
	private String isProfessorSQL(String registry) {
		if (registry != null && StringUtil.isInteger(registry)) {
			return "select idvinc from ga_docente where idvinc="
					+ registry.trim();
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @return Returns the sql to verifies if a person is an employee
	 */
	private String isEmployeeSQL(String registry) {
		if (registry != null && StringUtil.isInteger(registry)) {
			return "select f.idfuncionario from rh_funcionario f, rh_vinculo v "
					+ " where f.idfuncionario=v.idfuncionario and v.idvinculo='"
					+ registry.trim() + "'";
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @return Returns the sql to find the student's subjects
	 */
	private String getStudentSubjectsSQL(String registry,
			List<String> userGroups, String year, String semester) {
		if (registry != null && year != null && semester != null
				&& userGroups != null && year != null && semester != null
				&& (semester.equals("1") || semester.equals("2"))) {
			semester = getSemester(semester);
			if (userGroups.contains(UserGroup.STUDENT_GROUP)) {
				return "select t.disciplina, t.turma, d.nome from ga_matricula m, ga_turma t , ga_disciplina d"
						+ " where m.idturma=t.idturma and m.matricula='"
						+ registry.trim()
						+ "' and t.ano="
						+ year.trim()
						+ " and t.semestre="
						+ semester.trim()
						+ " and d.disciplina = t.disciplina";
			}
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @return Returns the sql to find the student's subjects
	 */
	private String getProfessorSubjectsSQL(String registry,
			List<String> userGroups, String year, String semester) {
		if (registry != null && year != null && semester != null
				&& userGroups != null && year != null && semester != null
				&& (semester.equals("1") || semester.equals("2"))) {

			semester = getSemester(semester);
			if (userGroups.contains(UserGroup.PROFESSOR_GROUP)) {
				return "select t.idturma,t.disciplina,t.turma, d.idvinc, a.nome"
						+ " from ga_turma t, ga_docenteturma dt, ga_docente d,ga_disciplina a"
						+ " where t.idturma=dt.idturma and dt.iddocente=d.iddocente and t.ano="
						+ year.trim()
						+ " and t.semestre="
						+ semester.trim()
						+ " and d.idvinc="
						+ registry.trim()
						+ " and a.disciplina=t.disciplina";
			}
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the sql to find the person's sector
	 */
	private String getPersonSectorSQL(String registry, List<String> userGroups) {
		if (registry != null && userGroups != null) {
			if (userGroups.contains(UserGroup.STUDENT_GROUP)) {
				return "select s.paisetor,s.siglasetor from GA_PROGRAMA p, GA_CURSO c, CM_SETOR s where p.matricula='"
						+ registry.trim()
						+ "' and c.curso = p.curso and c.idsetor = s.idsetor";
			}
			if (userGroups.contains(UserGroup.PROFESSOR_GROUP)) {
				return "select s.paisetor,s.siglasetor from ga_docente do, ga_departamento d, cm_setor s where do.iddepto=d.iddepto and do.idvinc='"
						+ registry.trim() + "' and s.idsetor=d.idsetor";
			}
			if (userGroups.contains(UserGroup.EMPLOYEE_GROUP)) {
				return "select p.nome, s.paisetor,s.siglasetor from cm_pessoa p, rh_vinculo v, rh_funcionario f, cm_usuario u, cm_setor s where v.idvinculo = '"
						+ registry.trim()
						+ "' and v.idfuncionario = f.idfuncionario and f.idpessoa = p.idpessoa and u.idpessoa = p.idpessoa and u.idsetor = s.idsetor";
			}
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @param year
	 * @param semester
	 *            (1 , 2)
	 * @param sector
	 * @param userGroup
	 * @return Returns the sql to find the person's sectors
	 */
	private String hasPersonLinkWithSectorSQL(String registry, String year,
			String semester, String sector, List<String> userGroups) {

		if (registry != null && userGroups != null
				&& userGroups.contains(UserGroup.STUDENT_GROUP) && year != null
				&& semester != null
				&& (semester.equals("1") || semester.equals("2"))) {
			semester = getSemester(semester);
			return "select distinct(s.paisetor) from ga_matricula m, ga_turma t ,ga_disciplina d ,ga_departamento dep,cm_setor s "
					+ " where m.idturma = t.idturma "
					+ " and m.matricula='"
					+ registry.trim()
					+ "'"
					+ " and t.ano="
					+ year.trim()
					+ " and t.semestre="
					+ semester.trim()
					+ " and t.disciplina = d.disciplina "
					+ " and d.iddepto = dep.iddepto "
					+ " and dep.idsetor = s.idsetor "
					+ " and s.paisetor = '"
					+ sector.toUpperCase().trim() + "'";
		}
		return null;
	}

	/**
	 * 
	 * @param registry
	 * @return Returns the sql to find the person's password
	 */
	private String getPersonPasswordSQL(String registry) {
		if (registry != null)
			return "select passmd5 from cm_usuario where login='"
					+ registry.trim() + "'";
		return null;
	}

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @return Returns the sql to find the register of people that is studying a
	 *         subject
	 */
	private String getRegistriesBySubjectCodeSQL(String subjectCode,
			String year, String semester, String classroom) {
		if (subjectCode != null && year != null && semester != null
				&& (semester.equals("1") || semester.equals("2"))) {
			semester = getSemester(semester);
			return "select  m.matricula  from ga_matricula m, ga_turma t , ga_disciplina d"
					+ " where m.idturma = t.idturma "
					+ " and t.turma='"
					+ classroom.toUpperCase().trim()
					+ "'"
					+ " and t.ano="
					+ year.trim()
					+ " and t.semestre= "
					+ semester.trim()
					+ " and d.disciplina = t.disciplina "
					+ " and d.disciplina = '" + subjectCode.trim() + "'";
		}
		return null;
	}

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @param registriesNotWanted
	 * @return Returns the sql to find the register of people that is studying a
	 *         subject, except the registries not wanted
	 */
	private String getRegistriesBySubjectCodeSQL(String subjectCode,
			String year, String semester, String classroom,
			List<String> registriesNotWanted) {
		if (subjectCode != null && year != null && semester != null
				&& classroom != null && registriesNotWanted != null) {
			StringBuffer buffer = new StringBuffer();
			for (String string : registriesNotWanted)
				buffer.append(" and m.matricula <> " + string);
			return "select  m.matricula  from ga_matricula m, ga_turma t , ga_disciplina d"
					+ " where m.idturma = t.idturma "
					+ " and t.turma='"
					+ classroom.toUpperCase().trim()
					+ "'"
					+ " and t.ano="
					+ year.trim()
					+ " and t.semestre= "
					+ getSemester(semester.trim())
					+ " and d.disciplina = t.disciplina "
					+ " and d.disciplina = '"
					+ subjectCode.toUpperCase().trim()
					+ "'"
					+ buffer.toString();
		}
		return null;
	}

	/**
	 * 
	 * @param subjectsCode
	 * @param year
	 * @param semester
	 * @return Returns the sql to find the register of people that is studying a
	 *         subject
	 */
	private String getRegistriesBySubjectCodeSQL(List<String> subjectsCode,
			String year, String semester, String classroom) {
		if (subjectsCode != null && year != null && semester != null
				&& !subjectsCode.isEmpty()
				&& (semester.equals("1") || semester.equals("2"))) {
			semester = getSemester(semester);
			StringBuffer query = new StringBuffer(
					"select distinct(m.matricula) from ga_matricula m, ga_turma t , ga_disciplina d"
							+ " where m.idturma = t.idturma "
							+ " and t.turma= '"
							+ classroom.toUpperCase().trim()
							+ "'"
							+ " and t.ano="
							+ year.trim()
							+ " and t.semestre= "
							+ semester.trim()
							+ " and d.disciplina = t.disciplina and (");
			int count = 0;
			for (String string : subjectsCode) {
				query.append("d.disciplina = '" + string + "'");
				if (count != subjectsCode.size() - 1)
					query.append(" or ");
				else
					query.append(")");

				count++;
			}
			return query.toString();
		}
		return null;
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @return Returns the sql to find all subject of a period and sector
	 */
	private String getSubjectByPeriodAndSectorSQL(String year, String semester,
			String sector) {
		if (year != null && semester != null && sector != null
				&& (semester.equals("1") || semester.equals("2"))) {
			semester = getSemester(semester);
			return "select di.disciplina,di.nome, t.turma, de.depto from ga_disciplina di, ga_departamento de, cm_setor s, ga_turma t"
					+ " where t.ano="
					+ year.trim()
					+ " and t.semestre="
					+ semester.trim()
					+ " and di.disciplina = t.disciplina"
					+ " and di.iddepto = de.iddepto"
					+ " and de.idsetor = s.idsetor"
					+ " and (s.paisetor = '"
					+ sector.toUpperCase().trim()
					+ "' or s.siglasetor='"
					+ sector.toUpperCase().trim()
					+ "')"
					+ "order by de.depto, t.turma";
		}
		return null;
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @return Returns the sql to find all subject of a period and sector
	 */
	private String getSubjectByPeriodAndSectorAndDepartmentSQL(String year,
			String semester, String sector, String department) {
		if (year != null && semester != null && sector != null
				&& (semester.equals("1") || semester.equals("2"))) {
			semester = getSemester(semester);
			return "select di.disciplina,di.nome, t.turma, de.depto from ga_disciplina di, ga_departamento de, cm_setor s, ga_turma t"
					+ " where t.ano="
					+ year.trim()
					+ " and t.semestre="
					+ semester.trim()
					+ " and di.disciplina = t.disciplina "
					+ " and di.iddepto = de.iddepto"
					+ " and de.depto='"
					+ department.trim()
					+ "'"
					+ " and de.idsetor = s.idsetor"
					+ " and (s.paisetor = '"
					+ sector.toUpperCase().trim()
					+ "' or s.siglasetor='"
					+ sector.toUpperCase().trim()
					+ "')" + "order by de.depto, t.turma";
		}
		return null;
	}

	/**
	 * 
	 * @param sector
	 * @return Returns the sql to find the departaments by sector
	 */
	private String getDepartamentsBySectorSQL(String sector) {
		if (sector != null) {
			return "select d.depto from ga_departamento d,cm_setor s "
					+ " where  d.idsetor = s.idsetor and (s.siglasetor='"
					+ sector.toUpperCase().trim() + "' or s.paisetor = '"
					+ sector.toUpperCase().trim() + "')";
		}
		return null;
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param subjectCode
	 * @param sector
	 * @return Returns the sql to find subjects by year, semester,
	 *         subjectCode,sector
	 */
	private String getSubjectByPeriodAndSectorAndSubjectCodeSQL(String year,
			String semester, String subjectCode, String sector) {
		if (year != null && semester != null && subjectCode != null
				&& sector != null) {
			semester = getSemester(semester);
			return "select di.disciplina,di.nome, t.turma, de.depto from ga_disciplina di, ga_departamento de, cm_setor s, ga_turma t "
					+ " where t.ano='"
					+ year.trim()
					+ "' "
					+ " and t.semestre='"
					+ semester.trim()
					+ "' "
					+ " and di.disciplina = t.disciplina "
					+ " and di.iddepto = de.iddepto "
					+ " and di.disciplina = '"
					+ subjectCode.toUpperCase().trim()
					+ "' "
					+ " and de.idsetor = s.idsetor "
					+ " and (s.paisetor = '"
					+ sector.toUpperCase().trim()
					+ "' "
					+ " or s.siglasetor='"
					+ sector.toUpperCase().trim()
					+ "') "
					+ " order by de.depto, t.turma";
		}
		return null;
	}

	/**
	 * 
	 * @param name
	 * @return Returns the sql to find name and registry of a person by name
	 */
	private String getNameAndRegistryByNameSQL(String name) {
		if (name != null)
			return "select p.nome, u.login from cm_pessoa p, cm_usuario u where p.nome like '"
					+ name.toUpperCase().trim()
					+ "%' and u.idpessoa = p.idpessoa";
		return null;
	}

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @return Returns the sql to find the registry and name of the subject
	 *         owner
	 */
	private String getSubjectOwnerSQL(String subjectCode, String year,
			String semester, String classroom) {
		if (subjectCode != null && year != null && semester != null
				&& classroom != null) {
			semester = getSemester(semester);
			return "select p.nome, d.idvinc  from ga_docenteturma dt, ga_turma t, ga_docente d, cm_pessoa p "
					+ " where dt.idturma = t.idturma and "
					+ " t.ano = '"
					+ year.trim()
					+ "' and "
					+ " t.semestre = '"
					+ semester.trim()
					+ "'  and "
					+ " t.disciplina = '"
					+ subjectCode.toUpperCase().trim()
					+ "' and "
					+ " dt.iddocente = d.iddocente and "
					+ " d.idpessoa = p.idpessoa "
					+ " and t.turma = '"
					+ classroom.toUpperCase().trim() + "'";
		}
		return null;
	}

	/**
	 * 
	 * @param subjectCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @return Returns all registry of people that is studying a subject
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public List<String> getRegistriesBySubjectCode(String subjectCode,
			String year, String semester, String classroom)
			throws ConnectionDataBaseException, Exception {
		List<String> list = new ArrayList<String>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getRegistriesBySubjectCodeSQL(subjectCode, year,
				semester, classroom);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				list.add(rs.getString("matricula"));
		}
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
	 * @throws ConnectionDataBaseException
	 * @throws Exception
	 */
	@Override
	public List<String> getRegistriesBySubjectCode(String subjectCode,
			String year, String semester, String classroom,
			List<String> registriesNotWanted)
			throws ConnectionDataBaseException, Exception {
		List<String> list = new ArrayList<String>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getRegistriesBySubjectCodeSQL(subjectCode, year,
				semester, classroom, registriesNotWanted);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				list.add(rs.getString("matricula"));
		}
		return list;
	}

	/**
	 * 
	 * @param subjectsCode
	 * @param year
	 * @param semester
	 * @param classroom
	 * @return Returns all registry of people that is studyinga list of subject
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public List<String> getRegistriesBySubjectCode(List<String> subjectsCode,
			String year, String semester, String classroom)
			throws ConnectionDataBaseException, Exception {
		List<String> list = new ArrayList<String>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getRegistriesBySubjectCodeSQL(subjectsCode, year,
				semester, classroom);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				list.add(rs.getString("matricula"));
		}
		return list;
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @return Returns all subject of a period and sector
	 */
	@Override
	public List<GroupVO> getSubjectByPeriodAndSector(String year,
			String semester, String sector) throws ConnectionDataBaseException,
			Exception {
		List<GroupVO> list = new ArrayList<GroupVO>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getSubjectByPeriodAndSectorSQL(year, semester, sector);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next()) {
				GroupVO groupVO = new GroupVO();
				groupVO.setClassroom(rs.getString("turma"));
				groupVO.setSubjectCode(rs.getString("disciplina"));
				groupVO.setSubjectName(rs.getString("nome"));
				groupVO.setDepartament(rs.getString("depto"));
				list.add(groupVO);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param sector
	 * @param departament
	 * @return Returns all subject of a period , sector and departament
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public List<GroupVO> getSubjectByPeriodAndSectorAndDepartment(String year,
			String semester, String sector, String department)
			throws ConnectionDataBaseException, Exception {
		List<GroupVO> list = new ArrayList<GroupVO>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getSubjectByPeriodAndSectorAndDepartmentSQL(year,
				semester, sector, department);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next()) {
				GroupVO groupVO = new GroupVO();
				groupVO.setClassroom(rs.getString("turma"));
				groupVO.setSubjectCode(rs.getString("disciplina"));
				groupVO.setSubjectName(rs.getString("nome"));
				groupVO.setDepartament(rs.getString("depto"));
				list.add(groupVO);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param year
	 * @param semester
	 * @param subjectCode
	 * @return Returns all subject of a period , sector with the specified
	 *         subject code
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public List<GroupVO> getSubjectByPeriodAndSectorAndSubjectCode(String year,
			String semester, String subjectCode, String sector)
			throws ConnectionDataBaseException, Exception {
		List<GroupVO> list = new ArrayList<GroupVO>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getSubjectByPeriodAndSectorAndSubjectCodeSQL(year,
				semester, subjectCode, sector);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next()) {
				GroupVO groupVO = new GroupVO();
				groupVO.setClassroom(rs.getString("turma"));
				groupVO.setSubjectCode(rs.getString("disciplina"));
				groupVO.setSubjectName(rs.getString("nome"));
				groupVO.setDepartament(rs.getString("depto"));
				list.add(groupVO);
			}
		}
		return list;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @return Returns the encrypted password (MD5)
	 * @throws ConnectionDataBaseException
	 * 
	 */
	@Override
	public String getPersonPassword(String registry)
			throws ConnectionDataBaseException, Exception {
		String pass = null;
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getPersonPasswordSQL(registry);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next()) {
				pass = rs.getString("passmd5");
			}
		}
		return pass;
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
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public boolean hasPersonLinkWithSector(String registry, String year,
			String semester, String sector, List<String> userGroups)
			throws ConnectionDataBaseException, Exception {
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = hasPersonLinkWithSectorSQL(registry, year, semester,
				sector, userGroups);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			if (rs.next())
				return true;
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the sector. Example: ICE
	 * @throws ConnectionDataBaseException
	 * 
	 */
	@Override
	public SectorVO getPersonSector(String registry, List<String> userGroups)
			throws ConnectionDataBaseException, Exception {
		SectorVO s = null;
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getPersonSectorSQL(registry, userGroups);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				s = new SectorVO(rs.getString("paisetor"), rs
						.getString("siglasetor"));
		}
		return s;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the name
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public String getPersonName(String registry, List<String> userGroups)
			throws ConnectionDataBaseException, Exception {
		String s = null;
		if (connection == null) {
			throw new ConnectionDataBaseException();
		}
		Statement st = connection.createStatement();
		String query = getPersonNameSQL(registry, userGroups);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				s = rs.getString("nome");
		}
		return s;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the course
	 * @throws ConnectionDataBaseException
	 * 
	 */
	@Override
	public String getCourseName(String registry, List<String> userGroups)
			throws ConnectionDataBaseException, Exception {
		String s = null;
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getCourseNameSQL(registry, userGroups);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				s = rs.getString("nome");
		}
		return s;
	}

	/**
	 * 
	 * @param registry
	 * @param userGroups
	 * @return Returns the departament
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public String getDepartamentName(String registry, List<String> userGroups)
			throws ConnectionDataBaseException, Exception {
		String s = null;
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getDepartamentNameSQL(registry, userGroups);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				s = rs.getString("nome");
		}
		return s;
	}

	/**
	 * Authentication at system
	 * 
	 * @param username
	 * @param password
	 * @return true or false
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public boolean loginPerson(String username, String password)
			throws ConnectionDataBaseException, Exception {
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getLoginPersonSQL(username, password);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			if (rs.next())
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param sector
	 * @return Returns a list of departament name by sector
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public List<String> getDepartamentsBySector(String sector)
			throws ConnectionDataBaseException, Exception {
		List<String> departaments = new ArrayList<String>();
		if (connection == null) {
			throw new ConnectionDataBaseException();
		}
		Statement st = connection.createStatement();
		String query = getDepartamentsBySectorSQL(sector);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				departaments.add(rs.getString("depto"));
		}
		return departaments;
	}

	/**
	 * @param registry
	 * @return Returns siga's userGroups of a person
	 * @throws ConnectionDataBaseException
	 */
	@Override
	public List<String> getUserPositionGroups(String registry)
			throws ConnectionDataBaseException, Exception {
		List<String> groups = new ArrayList<String>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		// is student?
		String query = isStudentSQL(registry);
		if (query != null) {
			ResultSet rs = st.executeQuery(query);
			while (rs.next())
				groups.add(UserGroup.STUDENT_GROUP);
		}
		if (groups.isEmpty()) {
			// is professor?
			query = isProfessorSQL(registry);
			if (query != null) {
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
					groups.add(UserGroup.PROFESSOR_GROUP);
					groups.add(UserGroup.EMPLOYEE_GROUP);
				}
			}
		}
		if (groups.isEmpty()) {
			// is employee?
			query = isEmployeeSQL(registry);
			if (query != null) {
				ResultSet rs = st.executeQuery(query);
				while (rs.next())
					groups.add(UserGroup.EMPLOYEE_GROUP);
			}
		}
		return groups;
	}

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
	@Override
	public List<GroupVO> getSubjects(String registry, List<String> userGroups,
			String year, String semester) throws ConnectionDataBaseException,
			Exception {
		List<GroupVO> list = new ArrayList<GroupVO>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getStudentSubjectsSQL(registry, userGroups, year,
				semester);
		if (query != null) {
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				GroupVO groupVO = new GroupVO();
				groupVO.setClassroom(rs.getString("turma"));
				groupVO.setSubjectCode(rs.getString("disciplina"));
				groupVO.setSubjectName(rs.getString("nome"));
				list.add(groupVO);
			}
		}
		query = getProfessorSubjectsSQL(registry, userGroups, year, semester);
		if (query != null) {
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				GroupVO groupVO = new GroupVO();
				groupVO.setClassroom(rs.getString("turma"));
				groupVO.setSubjectCode(rs.getString("disciplina"));
				groupVO.setSubjectName(rs.getString("nome"));
				list.add(groupVO);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param name
	 * @return Returns a list of nameVO object with the name and registry by
	 *         name (using like primitive)
	 */
	@Override
	public List<NameVO> getNameAndRegistryByName(String name)
			throws ConnectionDataBaseException, Exception {
		List<NameVO> list = new ArrayList<NameVO>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getNameAndRegistryByNameSQL(name);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				list
						.add(new NameVO(rs.getString("nome"), rs
								.getString("login")));
		}
		return list;
	}

	/**
	 * 
	 * @param subjectName
	 * @param year
	 * @param semester
	 * @return Returns a list of nameVOs(registry,name) representing the subject
	 *         owners
	 * @throws Exception
	 */
	@Override
	public List<NameVO> getSubjectOwner(String subjectCode, String year,
			String semester, String classroom)
			throws ConnectionDataBaseException, Exception {
		List<NameVO> list = new ArrayList<NameVO>();
		if (connection == null)
			throw new ConnectionDataBaseException();
		Statement st = connection.createStatement();
		String query = getSubjectOwnerSQL(subjectCode, year, semester,
				classroom);
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			while (rs.next())
				list.add(new NameVO(rs.getString("nome"), rs
						.getString("idvinc")));
		}
		return list;
	}

	/**
	 * Open database connection
	 * 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	@Override
	public void openConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		connection = dataSource.getConnection();
	}

	/**
	 * Close database connection
	 * 
	 * @throws SQLException
	 */
	@Override
	public void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	/**
	 * Checks if siga database connection is open
	 * 
	 * @return True if open. False if close
	 * @throws SQLException
	 * @throws SQLException
	 */
	@Override
	public boolean isConnectionOpen() throws SQLException {
		if (connection == null)
			return false;
		if (connection.isClosed())
			return false;
		return true;
	}

	/**
	 * 
	 * @return returns dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets dataSource
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}