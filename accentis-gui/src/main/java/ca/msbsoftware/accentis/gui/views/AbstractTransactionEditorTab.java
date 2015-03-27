package ca.msbsoftware.accentis.gui.views;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;
import ca.msbsoftware.accentis.gui.swing.DateFormattedTextField;
import ca.msbsoftware.accentis.gui.swing.PojoComboBox;
import ca.msbsoftware.accentis.gui.swing.TwoDecimalTextField;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Category;
import ca.msbsoftware.accentis.persistence.pojos.Individual;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;

@SuppressWarnings("serial")
public abstract class AbstractTransactionEditorTab extends JComponent {

	private DateFormattedTextField dateField;

	private JTextField referenceField;

	private TwoDecimalTextField amountField;

	private JComboBox<Individual> individualComboBox;

	private JTextField descriptionField;

	private TransactionEditorComponent transactionEditor;

	private JComboBox<Category> categoryComboBox;

	private JComboBox<Payee> payeeComboBox;

	private JComboBox<Account> fromAccountComboBox;

	private JComboBox<Account> toAccountComboBox;

	private class DirtyListener implements DocumentListener, ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			handleModification();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			handleModification();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			handleModification();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			handleModification();
		}

		private void handleModification() {
			if (getTransactionEditor().isEditing())
				getTransactionEditor().setDirty(true);
		}
	}

	private DirtyListener dirtyListener = new DirtyListener();

	protected AbstractTransactionEditorTab(TransactionEditorComponent editor) {
		transactionEditor = editor;
		setLayout(new GridBagLayout());
		createContent();
	}

	protected abstract void createContent();

	protected DateFormattedTextField getDateField() {
		if (null == dateField)
			createDateField();

		return dateField;
	}

	private void createDateField() {
		dateField = new DateFormattedTextField();
		dateField.setColumns(10);
		dateField.setEnabled(false);
		dateField.getDocument().addDocumentListener(dirtyListener);
	}

	protected JTextField getReferenceField() {
		if (null == referenceField)
			createReferenceField();

		return referenceField;
	}

	private void createReferenceField() {
		referenceField = new JTextField(10);
		referenceField.setEnabled(false);
		referenceField.getDocument().addDocumentListener(dirtyListener);
	}

	protected TwoDecimalTextField getAmountField() {
		if (null == amountField)
			createAmountField();

		return amountField;
	}

	private void createAmountField() {
		amountField = new TwoDecimalTextField();
		amountField.setColumns(10);
		amountField.setEnabled(false);
		amountField.getDocument().addDocumentListener(dirtyListener);
	}

	protected JComboBox<Individual> getIndividualComboBox() {
		if (null == individualComboBox)
			createIndividualComboBox();

		return individualComboBox;
	}

	private void createIndividualComboBox() {
		individualComboBox = new PojoComboBox<Individual>(new PojoComboBoxModel<Individual>(Individual.GET_ALL_INDIVIDUALS_QUERY, Individual.class, true), Individual.class);
		individualComboBox.setEnabled(false);
		individualComboBox.addItemListener(dirtyListener);
	}

	protected JTextField getDescriptionField() {
		if (null == descriptionField)
			createDescriptionField();

		return descriptionField;
	}

	private void createDescriptionField() {
		descriptionField = new JTextField();
		descriptionField.setEnabled(false);
		descriptionField.getDocument().addDocumentListener(dirtyListener);
	}

	protected JComboBox<Payee> getPayeeComboBox() {
		if (null == payeeComboBox)
			createPayeeComboBox();

		return payeeComboBox;
	}

	private void createPayeeComboBox() {
		payeeComboBox = new PojoComboBox<Payee>(new PojoComboBoxModel<Payee>(Payee.GET_ALL_PAYEES_QUERY, Payee.class, true), Payee.class);
		payeeComboBox.setEnabled(false);
		payeeComboBox.addItemListener(dirtyListener);
	}

	protected JComboBox<Category> getCategoryComboBox() {
		if (null == categoryComboBox)
			createCategoryComboBox();

		return categoryComboBox;
	}

	private void createCategoryComboBox() {
		categoryComboBox = new PojoComboBox<Category>(new PojoComboBoxModel<Category>(Category.GET_ALL_TOP_LEVEL_CATEGORIES_QUERY, Category.class, true), Category.class);
		categoryComboBox.setEnabled(false);
		categoryComboBox.addItemListener(dirtyListener);
	}

	protected JComboBox<Account> getFromAccountComboBox() {
		if (null == fromAccountComboBox)
			createFromAccountComboBox();

		return fromAccountComboBox;
	}

	private void createFromAccountComboBox() {
		fromAccountComboBox = new JComboBox<Account>(new PojoComboBoxModel<Account>(Account.GET_ALL_ACCOUNTS_QUERY, Account.class, false));
		fromAccountComboBox.setEnabled(false);
		fromAccountComboBox.addItemListener(dirtyListener);
	}

	protected JComboBox<Account> getToAccountComboBox() {
		if (null == toAccountComboBox)
			createToAccountComboBox();

		return toAccountComboBox;
	}

	private void createToAccountComboBox() {
		toAccountComboBox = new JComboBox<Account>(new PojoComboBoxModel<Account>(Account.GET_ALL_ACCOUNTS_QUERY, Account.class, false));
		toAccountComboBox.setEnabled(false);
		toAccountComboBox.addItemListener(dirtyListener);
	}

	protected TransactionEditorComponent getTransactionEditor() {
		return transactionEditor;
	}

	protected abstract void commitChanges();

	abstract void setTransaction(Transaction transaction);

	void clearTransaction() {
		getAmountField().setText(null);
		getCategoryComboBox().setSelectedItem(null);
		getDateField().setText(null);
		getDescriptionField().setText(null);
		getFromAccountComboBox().setSelectedItem(null);
		getIndividualComboBox().setSelectedItem(null);
		getPayeeComboBox().setSelectedItem(null);
		getReferenceField().setText(null);
		getToAccountComboBox().setSelectedItem(null);
	}
}
