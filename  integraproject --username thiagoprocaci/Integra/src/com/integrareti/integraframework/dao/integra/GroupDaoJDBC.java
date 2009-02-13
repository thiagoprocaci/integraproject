package com.integrareti.integraframework.dao.integra;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * This class offers methods to manipulates group at integra database - using
 * JDBC to improve performance
 * 
 * @author Thiago
 * 
 */
public class GroupDaoJDBC extends GroupDaoJDBCAdapter {
	private Connection connection = null;
	private DataSource dataSource;

	/**
	 * 
	 * @param groupName
	 * @return Returns the group id by name
	 * @throws Exception
	 */
	@Override
	public Integer getGroupIdByName(String groupName) throws Exception {
		Statement st = connection.createStatement();
		String query = "select groupID from `group` where name = '" + groupName + "'";
		ResultSet rs = (ResultSet) st.executeQuery(query);
		if (rs.next())
			return Integer.parseInt(rs.getString("groupID"));
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
	public void openConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
	 * Checks if siga database connection is open
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
	 * 
	 * @return Returns dataSource
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
