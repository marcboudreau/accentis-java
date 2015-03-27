package ca.msbsoftware.accentis.gui.views;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;
import ca.msbsoftware.accentis.gui.models.TransactionWithBalanceTableModel;
import ca.msbsoftware.accentis.gui.models.TransactionWithoutBalanceTableModel;

@SuppressWarnings("serial")
public class TransactionsRegisterTab extends JComponent implements
		IPersistenceManagerListener {

	private JComboBox<Account> accountComboBox;

	private JTable transactionTable;

	private RegisterTransactionEditor transactionEditor;

	public TransactionsRegisterTab() {
		createTab();

		GUIApplication.getInstance().addPersistenceManagerListener(this);
	}

	private void createTab() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		add(new JLabel(Resources.getInstance().getString(
				"transactionsview.tab.register.heading.caption")), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, //$NON-NLS-1$
						WEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		add(new JLabel(Resources.getInstance().getString(
				"transactionsview.tab.register.accounts.caption")), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, //$NON-NLS-1$
						EAST, NONE, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		add(getAccountComboBox(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				EAST, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		add(new JScrollPane(getTransactionTable()), new GridBagConstraints(0,
				1, 3, 1, 0.0, 1.0, CENTER, BOTH,
				InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		add(getTransactionEditor(), new GridBagConstraints(0, 2, 3, 1, 0.0,
				0.0, CENTER, BOTH, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
	}

	protected JComboBox<Account> getAccountComboBox() {
		if (null == accountComboBox)
			createAccountComboBox();

		return accountComboBox;
	}

	private void createAccountComboBox() {
		accountComboBox = new JComboBox<Account>(
				new PojoComboBoxModel<Account>(
						Account.GET_ALL_ACCOUNTS_QUERY, Account.class, false));
		accountComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				TransactionWithBalanceTableModel model = (TransactionWithBalanceTableModel) getTransactionTable()
						.getModel();
				Account account = (Account) e.getItem();
				model.setAccount(account);
				getTransactionEditor().setAccount(account);
			}
		});
	}

	protected JTable getTransactionTable() {
		if (null == transactionTable)
			createTransactionTable();

		return transactionTable;
	}

	private void createTransactionTable() {
		transactionTable = new JTable(new TransactionWithBalanceTableModel());
		transactionTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							int row = transactionTable.getSelectedRow();
							if (-1 != row) {
								TransactionWithoutBalanceTableModel model = (TransactionWithoutBalanceTableModel) transactionTable
										.getModel();
								Transaction value = model.getTransactionAt(row);
								getTransactionEditor().setTransaction(value);
							} else
								getTransactionEditor().setTransaction(null);
						}
					}
				});
		transactionTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		transactionTable.setShowVerticalLines(false);
	}

	protected RegisterTransactionEditor getTransactionEditor() {
		if (null == transactionEditor)
			createTransactionEditor();

		return transactionEditor;
	}

	private void createTransactionEditor() {
		transactionEditor = new RegisterTransactionEditor();
	}

	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		if (null != persistenceManager) {
			((PojoComboBoxModel<Account>) getAccountComboBox().getModel())
					.reload(PersistenceManager.EMPTY_PARAMETER_MAP);

			if (0 < getAccountComboBox().getItemCount())
				getAccountComboBox().setSelectedIndex(0);
			else
				getAccountComboBox().setSelectedIndex(-1);
		}
	}
}
