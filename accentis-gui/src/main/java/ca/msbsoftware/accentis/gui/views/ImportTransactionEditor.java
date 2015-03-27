package ca.msbsoftware.accentis.gui.views;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;

@SuppressWarnings("serial")
public class ImportTransactionEditor extends AbstractTransactionEditor {

	private Action enterAction;

	private Action cancelAction;

	private ActionListener providedActionListener;
	
	public ImportTransactionEditor(ActionListener actionListener) {
		providedActionListener = actionListener;
	}
	
	protected Action getEnterAction() {
		if (null == enterAction)
			createEnterAction();

		return enterAction;
	}

	private void createEnterAction() {
		enterAction = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button.enter"), "enter"), getActionListener()); //$NON-NLS-1$ //$NON-NLS-2$
		enterAction.setEnabled(false);
	}

	protected Action getCancelAction() {
		if (null == cancelAction)
			createCancelAction();

		return cancelAction;
	}

	private void createCancelAction() {
		cancelAction = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button.cancel"), "cancel"), getActionListener()); //$NON-NLS-1$ //$NON-NLS-2$
		cancelAction.setEnabled(false);
	}

	protected void createActionListener() {
		actionListener = providedActionListener;
	}

	@Override
	protected void addButtonsToPanel(JComponent panel) {
		panel.add(new JButton(getEnterAction()));
		panel.add(new JButton(getCancelAction()));
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		getEnterAction().setEnabled(enabled);
		getCancelAction().setEnabled(enabled);
	}


	protected void beginEditing() {
		super.beginEditing();
		getEnterAction().setEnabled(true);
		getCancelAction().setEnabled(true);
	}

	protected void endEditing() {
		super.endEditing();
		getEnterAction().setEnabled(false);
		getCancelAction().setEnabled(false);
	}
}
