package ca.msbsoftware.accentis.gui.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import ca.msbsoftware.accentis.persistence.pojos.Transaction;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;

/**
 * The {@code TransactionEditor} class is an object that controls the editing of {@link Transaction} objects. It dispatches the proper messages to the view
 * components that present the user interface to edit an existing or new {@link Transaction} object.
 * 
 */
@SuppressWarnings("serial")
public class RegisterTransactionEditor extends AbstractTransactionEditor {

	private Action newAction;

	private Action editAction;

	private Action enterAction;

	private Action cancelAction;

	public RegisterTransactionEditor() {
	}

	@Override
	protected void addButtonsToPanel(JComponent panel) {
		panel.add(new JButton(getNewAction()));
		panel.add(new JButton(getEditAction()));
		panel.add(new JButton(getEnterAction()));
		panel.add(new JButton(getCancelAction()));
	}
	
	protected Action getNewAction() {
		if (null == newAction)
			createNewAction();

		return newAction;
	}

	private void createNewAction() {
		newAction = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button.new"), "new"), getActionListener()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected Action getEditAction() {
		if (null == editAction)
			createEditAction();

		return editAction;
	}

	private void createEditAction() {
		editAction = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button.edit"), "edit"), getActionListener()); //$NON-NLS-1$ //$NON-NLS-2$
		editAction.setEnabled(false);
	}

	protected void createActionListener() {
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				assert null != command : "null command encountered in TransactionEditor ActionListener"; //$NON-NLS-1$

				if (command.equals("new")) //$NON-NLS-1$
					createNewTransaction();
				else if (command.equals("edit")) //$NON-NLS-1$
					beginEditing();
				else if (command.equals("enter")) //$NON-NLS-1$
					saveTransaction();
				else if (command.equals("cancel")) //$NON-NLS-1$
					revertTransaction();
			}
		};
	}

	private void createNewTransaction() {
		if (getTransactionEditorComponent().isDirty())
			if (!canProceedAfterOfferToSaveTransaction())
				return;

		setTransaction(new Transaction());
		getTransactionEditorComponent().setSelectedIndex(TransactionEditorComponent.SIMPLE_TRANSACTION_TAB);
		beginEditing();
	}

	protected void beginEditing() {
		super.beginEditing();
		getNewAction().setEnabled(false);
		getEditAction().setEnabled(false);
		getEnterAction().setEnabled(true);
		getCancelAction().setEnabled(true);
	}

	protected void endEditing() {
		super.setEnabled(false);
		getNewAction().setEnabled(true);
		getEditAction().setEnabled(true);
		getEnterAction().setEnabled(false);
		getCancelAction().setEnabled(false);
	}

	private boolean canProceedAfterOfferToSaveTransaction() {
		int reply = JOptionPane
				.showConfirmDialog(
						this,
						Resources.getInstance().getString("transactioneditor.unsavedchanges.message"), Resources.getInstance().getString("transactioneditor.unsavedchanges.title"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.YES_NO_CANCEL_OPTION);

		if (JOptionPane.CANCEL_OPTION == reply)
			return false;
		else if (JOptionPane.OK_OPTION == reply)
			saveTransaction();

		return true;
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

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		getNewAction().setEnabled(enabled);
		getEditAction().setEnabled(enabled);
		getEnterAction().setEnabled(enabled);
		getCancelAction().setEnabled(enabled);
	}
}
