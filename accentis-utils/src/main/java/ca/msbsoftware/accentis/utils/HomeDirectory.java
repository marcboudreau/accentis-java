package ca.msbsoftware.accentis.utils;

import java.io.File;

import ca.msbsoftware.accentis.utils.io.FileUtils;

public class HomeDirectory {

	public static final String HOME_DIRECTORY_PROPERTY = "accentis.home"; //$NON-NLS-1$
	
	public static final String TEMP_DIRECTORY = "temp"; //$NON-NLS-1$
	
	public static File getAccentisHomeTempDirectory() {
		File accentisHomeTempDirectory = new File(getAccentisHomeDirectory(), TEMP_DIRECTORY);
		if (!accentisHomeTempDirectory.exists())
			accentisHomeTempDirectory.mkdirs();
		
		return accentisHomeTempDirectory;
	}
	
	public static File getAccentisHomeDirectory() {
		File accentisHomeDirectory = new File(System.getProperty(HOME_DIRECTORY_PROPERTY, FileUtils.CURRENT_DIRECTORY_STRING));
		if (!accentisHomeDirectory.exists())
			accentisHomeDirectory.mkdirs();
		
		return accentisHomeDirectory;
	}
}
