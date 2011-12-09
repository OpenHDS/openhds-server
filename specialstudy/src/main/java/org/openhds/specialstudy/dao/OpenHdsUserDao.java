package org.openhds.specialstudy.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * A custom DAO that will connect to the OpenHDS database
 */
public class OpenHdsUserDao extends JdbcTemplate {

	private static final int MILLISECONDS_IN_24_HOURS = 86400000;
	private static final String SELECT_FOR_SESSION_ID = "SELECT lastLoginTime FROM user WHERE sessionId = ?";

	public OpenHdsUserDao(DataSource dataSource) {
		super(dataSource);
	}
	
	public boolean isSessionIdValidForOpenHds(String sessionId) {
		SqlRowSet rowSet = queryForRowSet(SELECT_FOR_SESSION_ID, sessionId);
		
		if (!rowSet.next()) {
			return false;
		}
		
		long timeLoggedIn = rowSet.getLong(1);
		if (timeLoggedIn > 0 && (timeLoggedIn - MILLISECONDS_IN_24_HOURS > 0)) {
			return true;
		}
		
		return false;
	}
}
