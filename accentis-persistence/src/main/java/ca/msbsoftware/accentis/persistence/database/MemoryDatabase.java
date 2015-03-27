package ca.msbsoftware.accentis.persistence.database;

import java.sql.Connection;

public class MemoryDatabase implements Database {

	@Override
	public void changePassword(Connection connection, char[] oldPassword,
			char[] newPassword) throws DatabaseException {
		/*
		 * No-op - No passwords for in-memory databases.
		 */
	}

	@Override
	public String getConnectionURL(char[] password) {
		return "jdbc:derby:memory:accentis;create=true";
	}

	@Override
	public String getLocation() {
		return null;
	}
}
