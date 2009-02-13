package com.integrareti.integraframework.test;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.integrareti.integraframework.dao.siga.SigaDao;
import com.integrareti.integraframework.dao.siga.SigaDaoJDBC;

public class SigaDatabase {
	private SigaDao sigaDao;

	public static void main(String[] args) {
		new SigaDatabase();
	}

	public SigaDatabase() {
		sigaDao = new SigaDaoJDBC();
		try {
			sigaDao.openConnection();
			// List<String> noList = new ArrayList<String>();
			for (int i = 1998; i < 2008; i++) {
				List<String> rs = sigaDao.getRegistriesBySubjectCode("MAT127", i + "", "1", "A");
				System.out.println(i + " " + rs.size());
				for (String string : rs) {
					System.out.println(string);
				}
				System.out.println("------------------------------------");
			}
			sigaDao.closeConnection();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void test2() {
		String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
		String DB_URL = "jdbc:oracle:thin:@athenas.cpd.ufjf.br:1521:ufjf";
		String DB_USER = "integrare";
		String DB_PASS = "hewlett2";
		java.sql.Connection connection = null;
		try {
			Class.forName(JDBC_DRIVER).newInstance();
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			Statement st = connection.createStatement();
			ResultSet rs = (ResultSet) st.executeQuery("select p.nome from cm_pessoa p, ga_aluno a where p.idpessoa=a.idpessoa and a.matricula='200515070'");
			while (rs.next()) {
				String s = rs.getString("nome");
				System.out.println(s);
			}
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}

	public SigaDao getSigaDao() {
		return sigaDao;
	}

	public void setSigaDao(SigaDao sigaDao) {
		this.sigaDao = sigaDao;
	}
}
