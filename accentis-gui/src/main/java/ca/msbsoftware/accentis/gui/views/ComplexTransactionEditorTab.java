package ca.msbsoftware.accentis.gui.views;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Individual;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;

import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.models.TransactionDetailTableModel;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;
import ca.msbsoftware.accentis.gui.transactions.ComplexTransactionAdapter;

@SuppressWarnings("serial")
public class ComplexTransactionEditorTab extends AbstractTransactionEditorTab {

	private ComplexTransactionAdapter adaptedTransaction;

	private JTable transactionDetailsTable;

	private JPopupMenu popupMenu;
	
	private Action addAction;
	
	private Action removeAction;
	
	private ActionListener actionListener;
	
	protected ComplexTransactionEditorTab(TransactionEditorComponent editor) {
		super(editor);

		getAmountField().setEditable(false);
	}

	protected JTable getTransactionDetailsTable() {
		if (null == transactionDetailsTable)
			createTransactionDetailsTable();

		return transactionDetailsTable;
	}

	private void createTransactionDetailsTable() {
		transactionDetailsTable = new JTable(new TransactionDetailTableModel());
		transactionDetailsTable.setDefaultEditor(Account.class, new PojoTableCellEditor<Account>(Account.GET_ALL_ACCOUNTS_QUERY, Account.class, false));
		transactionDetailsTable.setDefaultEditor(Payee.class, new PojoTableCellEditor<Payee>(Payee.GET_ALL_PAYEES_QUERY, Payee.class, true));
		transactionDetailsTable.setDefaultEditor(Individual.class, new PojoTableCellEditor<Individual>(Individual.GET_ALL_INDIVIDUALS_QUERY, Individual.class, true));
		transactionDetailsTable.setDefaultEditor(BigDecimal.class, new BigDecimalTableCellEditor());

		CategoriesCellEditor categoriesCellEditor = new CategoriesCellEditor();
		transactionDetailsTable.getColumnModel().getColumn(2).setCellEditor(categoriesCellEditor.getCategoryCellEditor());
		transactionDetailsTable.getColumnModel().getColumn(3).setCellEditor(categoriesCellEditor.getSubcategoryCellEditor());
	}
	
	protected JPopupMenu getTransactionDetailsTablePopupMenu() {
		if (null == popupMenu)
			createTransactionDetailsTablePopupMenu();
		
		return popupMenu;
	}
	
	private void createTransactionDetailsTablePopupMenu() {
		popupMenu = new JPopupMenu();
		popupMenu.add(getAddAction());
		popupMenu.add(getRemoveAction());
	}
	
	protected Action getAddAction() {
		if (null == addAction)
			createAddAction();
		
		return addAction;
	}
	
	private void createAddAction() {
		addAction = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.popupmenu.add"), "add"), getActionListener()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	protected Action getRemoveAction() {
		if (null == removeAction)
			createRemoveAction();
		
		return removeAction;
	}
	
	private void createRemoveAction() {
		removeAction = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.popupmenu.remove"), "remove"), getActionListener()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	protected ActionListener getActionListener() {
		if (null == actionListener)
			createActionListener();
		
		return actionListener;
	}
	
	private void createActionListener() {
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				assert null != e.getActionCommand() : "null action command encountered in ComplexTransactionEditorView action listener"; //$NON-NLS-1$
				String command = e.getActionCommand();
				
				if (command.equals("add")) //$NON-NLS-1$
					doAdd();
				else if (command.equals("remove")) //$NON-NLS-1$
					doRemove();
			}
		};
	}
	
	@Override
	protected void createContent() {
		add(new JLabel(Resources.getInstance().getString("transactioneditor.payee.caption")), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		add(getPayeeComboBox(), new GridBagConstraints(3, 0, 2, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		add(new JLabel(Resources.getInstance().getString("transactioneditor.date.caption")), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		add(getDateField(), new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, CENTER, NONE, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("transactioneditor.reference.caption")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		add(getReferenceField(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, CENTER, NONE, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		JScrollPane sp = new JScrollPane(getTransactionDetailsTable());
		sp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					getTransactionDetailsTablePopupMenu().show(getTransactionDetailsTable(), e.getX(), e.getY());
			}
		});
		sp.setPreferredSize(new Dimension(10,10));
		add(sp, new GridBagConstraints(3, 1, 2, 2, 1.0, 1.0, CENTER, BOTH,
				InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(new JLabel(Resources.getInstance().getString("transactioneditor.amount.caption")), new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(getAmountField(), new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0, CENTER, NONE, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("transactioneditor.description.caption")), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(getDescriptionField(), new GridBagConstraints(3, 3, 2, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
	}

	@Override
	void setTransaction(Transaction transaction) {
		adaptedTransaction = new ComplexTransactionAdapter(transaction, getTransactionEditor().getAccount(), (TransactionDetailTableModel) getTransactionDetailsTable().getModel());

		getReferenceField().setText(adaptedTransaction.getReference());
		getPayeeComboBox().setSelectedItem(adaptedTransaction.getPayee());
		getDescriptionField().setText(adaptedTransaction.getDescription());
		getDateField().setValue(adaptedTransaction.getDate());
		getAmountField().setValue(adaptedTransaction.getAmount());
	}

	@Override
	protected void commitChanges() {
		assert null != adaptedTransaction : "null Transaction encountered in ComplexTransactionEditorView commitChanges"; //$NON-NLS-1$

		adaptedTransaction.setDate(getDateField().getDateValue());
		adaptedTransaction.setDescription(getDescriptionField().getText());
		adaptedTransaction.setPayee((Payee) getPayeeComboBox().getSelectedItem());
		adaptedTransaction.setReference(getReferenceField().getText());
		
		adaptedTransaction.saveTransaction();
	}

	@Override
	void clearTransaction() {
		super.clearTransaction();
		
		adaptedTransaction = null;
		TransactionDetailTableModel model = (TransactionDetailTableModel) getTransactionDetailsTable().getModel();
		model.setTransactionDetails(null);
		model.fireTableDataChanged();
	}

	private void doAdd() {
		((TransactionDetailTableModel) getTransactionDetailsTable().getModel()).addDetail();
	}
	
	private void doRemove() {
		((TransactionDetailTableModel) getTransactionDetailsTable().getModel()).removeDetails(getTransactionDetailsTable().getSelectedRows());
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		getReferenceField().setEnabled(enabled);
		getPayeeComboBox().setEnabled(enabled);
		getTransactionDetailsTable().setEnabled(enabled);
		getDescriptionField().setEnabled(enabled);
		getDateField().setEnabled(enabled);
	}
}