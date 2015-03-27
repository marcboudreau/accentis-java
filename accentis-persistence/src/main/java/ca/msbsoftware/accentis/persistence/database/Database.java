package ca.msbsoftware.accentis.persistence.database;

import java.sql.Connection;

public interface Database {

	public void changePassword(Connection connection, char[] oldPassword, char[] newPassword) throws DatabaseException;
	
	public String getConnectionURL(char[] password);
	
	public String getLocation();
}
