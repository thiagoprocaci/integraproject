package com.integrareti.integraframework.dao.integra;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.integrareti.integraframework.business.Person;

/**
 * This class offers methods to manipulates person at integra database - using
 * JDBC to improve performance
 * 
 * @author Thiago
 * 
 */
public class PersonDaoJDBC extends PersonDaoJDBCAdapter {

	private Connection connection = null;
	private DataSource dataSource;

	/**
	 * Checks if a person exists in integra database
	 * 
	 * @param registry
	 * @return Returns id if person exist
	 * @throws Exception
	 */
	@Override
	public Integer isPersonSaved(String registry) throws Exception {
		Statement st = connection.createStatement();
		String query = "select personID from person where registry = '"
				+ registry + "'";
		ResultSet rs = (ResultSet) st.executeQuery(query);
		if (rs.next())
			return Integer.parseInt(rs.getString("personID"));
		return null;
	}

	/**
	 * Open database connection
	 * 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */

	public void openConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		connection = dataSource.getConnection();
	}

	/**
	 * Close database connection
	 * 
	 * @throws SQLException
	 */

	public void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	/**
	 * Checks if integra database connection is open
	 * 
	 * @return True if open. False if close
	 * @throws SQLException
	 */
	public boolean isConnectionOpen() throws SQLException {
		if (connection == null)
			return false;
		if (connection.isClosed())
			return false;
		return true;
	}

	/**
	 * @return Returns a person by id
	 * @param id
	 */
	@Override
	public Person getById(Integer id) throws Exception {
		Statement st = connection.createStatement();
		String query = "select personID from person where personID = " + id;
		if (query != null) {
			ResultSet rs = (ResultSet) st.executeQuery(query);
			if (rs.next()) {
				Integer personId = Integer.parseInt(rs.getString("personID"));
				Person p = new Person();
				p.setId(personId);
				return p;
			}
		}
		return null;
	}

	/**
	 * 
	 * @return Returns dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets dataSource
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
