package ca.msbsoftware.accentis.gui.swing;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MSBAction extends AbstractAction {

	private ActionListener actionListener;
	
	public MSBAction(String name, Icon icon, String command, String tooltip,
			char mnemonic, KeyStroke accelerator, ActionListener listener) {
		super(name, icon);
		putValue(ACCELERATOR_KEY, accelerator);
		putValue(ACTION_COMMAND_KEY, command);
		putValue(MNEMONIC_KEY, new Integer(mnemonic));
		putValue(SHORT_DESCRIPTION, tooltip);
		actionListener = listener;
	}

	public MSBAction(ActionResourceDefinition resourceString, ActionListener listener) {
		resourceString.configureAction(this);
		actionListener = listener;
	}
	
	public MSBAction(String command, ActionListener listener) {
		putValue(ACTION_COMMAND_KEY, command);
		actionListener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionListener.actionPerformed(e);
	}
}
