package ca.msbsoftware.accentis.gui.views;

import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class ViewToolBar extends JToolBar {

	private ButtonGroup buttonGroup;
	
	private Set<Action> actions;

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		for (Action action : getActions())
			action.setEnabled(enabled);
	}
	
	protected Set<Action> getActions() {
		if (null == actions)
			actions = new HashSet<Action>();
		
		return actions;
	}

	public void addAction(Action action) {
		action.setEnabled(isEnabled());
		
		actions.add(action);
		addButton(new JToggleButton(action));
	}
	
	private void addButton(AbstractButton button) {
		add(button);
		getButtonGroup().add(button);
	}
	
	protected ButtonGroup getButtonGroup() {
		if (null == buttonGroup)
			createButtonGroup();
		
		return buttonGroup;
	}
	
	private void createButtonGroup() {
		buttonGroup = new ButtonGroup();
	}
}
