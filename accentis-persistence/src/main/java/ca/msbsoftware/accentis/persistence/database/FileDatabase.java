package ca.msbsoftware.accentis.persistence.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class FileDatabase implements Database {

	private String filePath;
	
	public FileDatabase(File file) {
		filePath = file.getAbsolutePath();
	}
	
	@Override
	public void changePassword(Connection connection, char[] oldPassword, char[] newPassword) throws DatabaseException {
		try {
			connection.prepareCall(String.format("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('bootPassword','%s,%s')", new String(oldPassword), new String(newPassword)));
		} catch (SQLException ex) {
			if (ex.getSQLState().equals("XBCXA")) //$NON-NLS-1$
				throw new DatabaseWrongPasswordException();
			else
				throw new DatabaseException(ex);
		}
	}

	@Override
	public String getConnectionURL(char[] password) {
		return String.format("jdbc:derby:%s;create=true;dataEncryption=true;bootPassword=%s", filePath, new String(password));
	}

	@Override
	public String getLocation() {
		return filePath;
	}
}
