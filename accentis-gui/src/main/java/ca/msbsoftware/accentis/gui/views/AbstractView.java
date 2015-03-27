package ca.msbsoftware.accentis.gui.views;

import javax.swing.JComponent;

public abstract class AbstractView {

	private String viewID;
	
	protected AbstractView(String id) {
		viewID = id;
	}
	
	public String getViewID() {
		return viewID;
	}
	
	public abstract String getActionResourceKey();
	
	protected abstract JComponent getViewComponent();
}
