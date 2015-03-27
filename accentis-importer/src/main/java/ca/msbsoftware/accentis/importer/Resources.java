package ca.msbsoftware.accentis.importer;

import ca.msbsoftware.accentis.utils.resources.ResourcesBase;

public class Resources extends ResourcesBase {

	private static Resources instance = new Resources();
	
	public static Resources getInstance() {
		return instance;
	}
	
	@Override
	protected String getBundleName() {
		return "org.msb.accentis.gui.resources"; //$NON-NLS-1$
	}
}
