package ca.msbsoftware.accentis.persistence;

import ca.msbsoftware.accentis.utils.resources.ResourcesBase;

public class Resources extends ResourcesBase {

	private static Resources instance = new Resources();
	
	public static Resources getInstance() {
		return instance;
	}
	
	@Override
	protected String getBundleName() {
		return "org.msb.accentis.data.resources"; //$NON-NLS-1$
	}
}
