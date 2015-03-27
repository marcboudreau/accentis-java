package ca.msbsoftware.accentis.gui;

import ca.msbsoftware.accentis.utils.resources.ResourcesBase;

public class Resources extends ResourcesBase {

	private static Resources instance = new Resources();
	
	public static Resources getInstance() {
		return instance;
	}
	
	@Override
	protected String getBundleName() {
		return "ca.msbsoftware.accentis.gui.resources"; //$NON-NLS-1$
	}
}
