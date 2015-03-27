package ca.msbsoftware.accentis.utils;

import ca.msbsoftware.accentis.utils.resources.ResourcesBase;

public class Resources extends ResourcesBase {

	private static final Resources instance = new Resources();
	
	public static Resources getInstance() {
		return instance;
	}
	
	@Override
	protected String getBundleName() {
		return "org.msb.accentis.utils.resources"; //$NON-NLS-1$
	}

}
