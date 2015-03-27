package ca.msbsoftware.accentis.gui.views;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.persistence.pojos.Category;
import ca.msbsoftware.accentis.persistence.pojos.Individual;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;
import ca.msbsoftware.accentis.gui.transactions.SimpleTransactionAdapter;

@SuppressWarnings("serial")
public class SimpleTransactionEditorTab extends AbstractTransactionEditorTab implements IPersistenceManagerListener {

	private SimpleTransactionAdapter adaptedTransaction;

	public SimpleTransactionEditorTab(TransactionEditorComponent editor) {
		super(editor);
		
		GUIApplication.getInstance().addPersistenceManagerListener(this);
	}

	protected void createContent() {
		/*
		 * Adding components as they appear in the layout from left to right, top to bottom.
		 */
		add(new JLabel(Resources.getInstance().getString("transactioneditor.payee.caption")), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		add(getPayeeComboBox(), new GridBagConstraints(3, 0, 2, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		add(new JLabel(Resources.getInstance().getString("transactioneditor.date.caption")), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		add(getDateField(), new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, CENTER, NONE, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("transactioneditor.reference.caption")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.ZERO_INSETS, 0, 0));
		add(getReferenceField(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, CENTER, NONE, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		add(new JLabel(Resources.getInstance().getString("transactioneditor.category.caption")), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(getCategoryComboBox(), new GridBagConstraints(3, 1, 1, 1, 0.5, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(new JLabel(Resources.getInstance().getString("transactioneditor.amount.caption")), new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(getAmountField(), new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0, CENTER, NONE, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("transactioneditor.individual.caption")), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(getIndividualComboBox(), new GridBagConstraints(3, 2, 2, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("transactioneditor.description.caption")), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(getDescriptionField(), new GridBagConstraints(3, 3, 2, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
	}

	@Override
	void setTransaction(Transaction transaction) {
		adaptedTransaction = new SimpleTransactionAdapter(transaction, getTransactionEditor().getAccount());

		getReferenceField().setText(adaptedTransaction.getReference());
		getPayeeComboBox().setSelectedItem(adaptedTransaction.getPayee());
		setCategories(adaptedTransaction.getCategory());
		getIndividualComboBox().setSelectedItem(adaptedTransaction.getIndividual());
		getDescriptionField().setText(adaptedTransaction.getDescription());
		getDateField().setValue(adaptedTransaction.getDate());
		getAmountField().setValue(adaptedTransaction.getAmount());
	}

	private void setCategories(Category category) {
		getCategoryComboBox().setSelectedItem(category);
	}

	private Category getCategory() {
		return (Category) getCategoryComboBox().getSelectedItem();
	}

	@Override
	protected void commitChanges() {
		assert null != adaptedTransaction : "null Transaction encountered in SimpleTransactionEditorView commitChanges"; //$NON-NLS-1$

		adaptedTransaction.setAccount(getTransactionEditor().getAccount());
		adaptedTransaction.setAmount(getAmountField().getBigDecimalValue());
		adaptedTransaction.setCategory(getCategory());
		adaptedTransaction.setDate(getDateField().getDateValue());
		adaptedTransaction.setDescription(getDescriptionField().getText());
		adaptedTransaction.setIndividual((Individual) getIndividualComboBox().getSelectedItem());
		adaptedTransaction.setPayee((Payee) getPayeeComboBox().getSelectedItem());
		adaptedTransaction.setReference(getReferenceField().getText());
		
		adaptedTransaction.saveTransaction();
	}
	
	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		if (null != persistenceManager) {
			((PojoComboBoxModel<Payee>) getPayeeComboBox().getModel()).reload(PersistenceManager.EMPTY_PARAMETER_MAP);
			((PojoComboBoxModel<Category>) getCategoryComboBox().getModel()).reload(PersistenceManager.EMPTY_PARAMETER_MAP);
			((PojoComboBoxModel<Individual>) getIndividualComboBox().getModel()).reload(PersistenceManager.EMPTY_PARAMETER_MAP);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		getReferenceField().setEnabled(enabled);
		getPayeeComboBox().setEnabled(enabled);
		getCategoryComboBox().setEnabled(enabled);
		getIndividualComboBox().setEnabled(enabled);
		getDescriptionField().setEnabled(enabled);
		getDateField().setEnabled(enabled);
		getAmountField().setEnabled(enabled);
	}
}
