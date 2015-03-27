package ca.msbsoftware.accentis.sgml.resources;

import ca.msbsoftware.accentis.utils.resources.ResourcesBase;

public class Resources extends ResourcesBase {

	private static final Resources instance = new Resources();
	
	public static Resources getInstance() {
		return instance;
	}
	
	@Override
	protected String getBundleName() {
		return "ca.msbsoftware.accentis.sgml.resources"; //$NON-NLS-1$
	}

}
