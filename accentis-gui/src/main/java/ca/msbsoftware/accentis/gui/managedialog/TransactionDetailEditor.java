package ca.msbsoftware.accentis.gui.managedialog;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Individual;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.ScheduledTransaction;

import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;
import ca.msbsoftware.accentis.gui.models.TransactionDetailTableModel;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;
import ca.msbsoftware.accentis.gui.views.BigDecimalTableCellEditor;
import ca.msbsoftware.accentis.gui.views.CategoriesCellEditor;
import ca.msbsoftware.accentis.gui.views.PojoTableCellEditor;

@SuppressWarnings("serial")
public class TransactionDetailEditor extends JComponent {

	private JComboBox<Payee> transactionPayeeComboBox;

	private JTextField transactionReferenceField;

	private JTable transactionDetailsTable;

	private JTextField transactionDescriptionField;

	private JPopupMenu popupMenu;
	
	private Action addAction;
	
	private Action removeAction;
	
	private ActionListener actionListener;
	
	public TransactionDetailEditor() {
		createEditor();
	}

	private void createEditor() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Resources.getInstance().getString("managescheduledtransactiondialog.transaction.title"))); //$NON-NLS-1$
		setLayout(new GridBagLayout());

		add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.transaction.reference.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0)); //$NON-NLS-1$
		add(getTransactionReferenceField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.transaction.payee.caption")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0)); //$NON-NLS-1$
		add(getTransactionPayeeComboBox(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.transaction.details.caption")), new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0)); //$NON-NLS-1$

		JScrollPane sp = new JScrollPane(getTransactionDetailsTable());
		sp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					getTransactionDetailsTablePopupMenu().show(getTransactionDetailsTable(), e.getX(), e.getY());
			}
		});
		sp.setPreferredSize(new Dimension(10, 10));
		add(sp, new GridBagConstraints(0, 3, 2, 1, 0.0, 1.0, CENTER, BOTH, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.transaction.description.caption")), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0)); //$NON-NLS-1$
		add(getTransactionDescriptionField(), new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0,
				0));
	}

	public void updateFields(ScheduledTransaction transaction) {
		getTransactionPayeeComboBox().setSelectedItem(transaction.getTransactionPayee());
		getTransactionReferenceField().setText(transaction.getTransactionReference());
		((TransactionDetailTableModel) getTransactionDetailsTable().getModel()).setTransactionDetails(transaction.getTransactionDetails());
		getTransactionDescriptionField().setText(transaction.getTransactionDescription());
	}

	public void clearFields() {
		getTransactionPayeeComboBox().setSelectedItem(null);
		getTransactionReferenceField().setText(null);
		((TransactionDetailTableModel) getTransactionDetailsTable().getModel()).setTransactionDetails(null);
		getTransactionDescriptionField().setText(null);
	}
	
	void saveTransactionDetails(ScheduledTransaction transaction) {
		transaction.setTransactionReference(getTransactionReferenceField().getText());
		transaction.setTransactionPayee((Payee) getTransactionPayeeComboBox().getSelectedItem());
		((TransactionDetailTableModel) getTransactionDetailsTable().getModel()).saveTransactionDetails(transaction.getTransactionDetails());
		transaction.setTransactionDescription(getTransactionDescriptionField().getText());
	}

	protected JComboBox<Payee> getTransactionPayeeComboBox() {
		if (null == transactionPayeeComboBox)
			createTransactionPayeeComboBox();

		return transactionPayeeComboBox;
	}

	private void createTransactionPayeeComboBox() {
		transactionPayeeComboBox = new JComboBox<Payee>(new PojoComboBoxModel<Payee>(Payee.GET_ALL_PAYEES_QUERY, Payee.class, true));
	}

	protected JTextField getTransactionReferenceField() {
		if (null == transactionReferenceField)
			createTransactionReferenceField();

		return transactionReferenceField;
	}

	private void createTransactionReferenceField() {
		transactionReferenceField = new JTextField(20);
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

	protected JTextField getTransactionDescriptionField() {
		if (null == transactionDescriptionField)
			createTransactionDescriptionField();

		return transactionDescriptionField;
	}

	private void createTransactionDescriptionField() {
		transactionDescriptionField = new JTextField(20);
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
				String command = e.getActionCommand();
				assert null != command : "null action command encountered in TransactionDetailEditor action listener."; //$NON-NLS-1$
				
				if (command.equals("add")) //$NON-NLS-1$
					doAdd();
				else if (command.equals("remove")) //$NON-NLS-1$
					doRemove();
			}
		};
	}
	
	private void doAdd() {
		((TransactionDetailTableModel) getTransactionDetailsTable().getModel()).addDetail();
	}
	
	private void doRemove() {
		((TransactionDetailTableModel) getTransactionDetailsTable().getModel()).removeDetails(getTransactionDetailsTable().getSelectedRows());
	}
}
