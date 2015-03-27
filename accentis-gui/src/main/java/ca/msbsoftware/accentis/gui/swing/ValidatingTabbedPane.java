package ca.msbsoftware.accentis.gui.swing;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ValidatingTabbedPane extends JTabbedPane {

	private Validator validator;
	
	public ValidatingTabbedPane() {
	}

	public ValidatingTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}

	public ValidatingTabbedPane(int tabPlacement) {
		super(tabPlacement);
	}

	@Override
	public void setSelectedIndex(int index) {
		if (null == validator || validator.canSwitchTabs(index))
			super.setSelectedIndex(index);
	}

	public interface Validator {
		
		public boolean canSwitchTabs(int newTabIndex);
	}
	
	public Validator getValidator() {
		return validator;
	}
	
	public void setValidator(Validator value) {
		validator = value;
	}
}
