package ca.msbsoftware.accentis.gui.wizard;

import javax.swing.JComponent;

public abstract class AbstractWizardPage {

	protected abstract JComponent getPageComponent();
	
	protected abstract void recordInputs();
}
