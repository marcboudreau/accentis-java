package ca.msbsoftware.accentis.gui.views;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.NONE;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.persistence.utils.TransactionHelper;
import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.gui.InsetsConstants;

@SuppressWarnings("serial")
public abstract class AbstractTransactionEditor extends JComponent {

	private TransactionEditorComponent transactionEditorComponent;

	private boolean newTransaction;
	
	protected ActionListener actionListener;
	
	public AbstractTransactionEditor() {
		createComponents();
	}
	
	private void createComponents() {
		setLayout(new GridBagLayout());

		add(getTransactionEditorComponent(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, InsetsConstants.ZERO_INSETS, 0, 0));
		add(createButtonPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, EAST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
	}
	
	protected TransactionEditorComponent getTransactionEditorComponent() {
		if (null == transactionEditorComponent)
			createTransactionEditorComponent();
	
		return transactionEditorComponent;
	}

	private void createTransactionEditorComponent() {
		transactionEditorComponent = new TransactionEditorComponent();
	}

	private JComponent createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 5, 0));

		addButtonsToPanel(panel);

		return panel;
	}

	protected abstract void addButtonsToPanel(JComponent panel);

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		getTransactionEditorComponent().setEnabled(enabled);
	}

	/**
	 * Changes the {@link Transaction} object being edited. A new {@link Transaction} object can be created by passing a {@code null} reference to this method.
	 * 
	 * @param value
	 *            The {@link Transaction} object to edit, or {@code null} to create a new {@link Transaction} object.
	 */
	public void setTransaction(Transaction value) {
		setNewTransaction(!TransactionHelper.isExistingTransaction(value));
		getTransactionEditorComponent().setTransaction(value);
	}
	
	private boolean isNewTransaction() {
		return newTransaction;
	}
	
	private void setNewTransaction(boolean value) {
		newTransaction = value;
	}
	
	protected void beginEditing() {
		getTransactionEditorComponent().beginEditing();
	}
	
	protected void endEditing() {
		getTransactionEditorComponent().endEditing();
	}

	/**
	 * Commits the modifications made by the editor to the current {@link Transaction} object into the database.
	 */
	public void saveTransaction() {
		endEditing();
		getTransactionEditorComponent().commitChanges();
	
		PersistenceManager persistenceManager = GUIApplication.getInstance().getPersistenceManager();
		Transaction transaction = getTransactionEditorComponent().getTransaction();
		
		if (isNewTransaction())
			persistenceManager.create(transaction);
		else
			persistenceManager.save(transaction);
	}

	/**
	 * Resets the values of the current {@link Transaction} object's fields to that of the persisted values in the database. This reverts any modifications made
	 * by the editor.
	 */
	public void revertTransaction() {
		endEditing();
	
		if (isNewTransaction())
			setTransaction(null);
		else
			revertExistingTransaction();
	}

	private void revertExistingTransaction() {
		PersistenceManager persistenceManager = GUIApplication.getInstance().getPersistenceManager();
		Transaction transaction = getTransactionEditorComponent().getTransaction();
		
		persistenceManager.refresh(transaction);
		setTransaction(transaction);
	}
	
	public Account getAccount() {
		return getTransactionEditorComponent().getAccount();
	}
	
	public void setAccount(Account account) {
		getTransactionEditorComponent().setAccount(account);
	}
	
	protected ActionListener getActionListener() {
		if (null == actionListener)
			createActionListener();
			
		return actionListener;
	}
	
	protected abstract void createActionListener();
}