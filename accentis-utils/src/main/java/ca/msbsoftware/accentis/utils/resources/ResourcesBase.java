package ca.msbsoftware.accentis.utils.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class ResourcesBase {
	
	private static final String BANG = "!";  //$NON-NLS-1$
	
	private ResourceBundle resources = findBundle();

	public String getString(String key) {
		try {
			return resources.getString(key);
		} catch (MissingResourceException ex) {
			
			return BANG + key + BANG;
		}
	}
	
	public char getCharacter(String key) {
		try {
			String resource = resources.getString(key);
			if (resource.isEmpty())
				return '\0';
			
			return resource.charAt(0);
		} catch (MissingResourceException ex) {
			return '\0';
		}
	}

	private ResourceBundle findBundle() {
		return ResourceBundle.getBundle(getBundleName());
	}
	
	protected abstract String getBundleName();
}
