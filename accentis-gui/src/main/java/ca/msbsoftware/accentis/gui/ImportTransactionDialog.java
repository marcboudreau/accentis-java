package ca.msbsoftware.accentis.gui;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.DownloadedTransaction;
import ca.msbsoftware.accentis.persistence.pojos.PayeeMapping;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.persistence.pojos.TransactionDetail;
import ca.msbsoftware.accentis.gui.models.TransactionWithoutBalanceTableModel;
import ca.msbsoftware.accentis.gui.swing.PlainLabel;
import ca.msbsoftware.accentis.gui.views.ImportTransactionEditor;

@SuppressWarnings("serial")
public class ImportTransactionDialog extends JDialog {

	private JTextField accountField;
	
	private ImportTransactionEditor transactionEditor;

	private JTable matchingTransactionTable;

	private ActionListener actionListener;

	private DownloadedTransaction downloadedTransaction;

	@Override
	protected void dialogInit() {
		super.dialogInit();

		setContentPane(createContentPane());
	}

	private JComponent createContentPane() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(
				new JLabel(Resources.getInstance().getString("importtransactiondialog.transactionheader.caption")), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, InsetsConstants.ZERO_INSETS, 0, 0)); //$NON-NLS-1$
		panel.add(new JLabel(Resources.getInstance().getString("importtransactiondialog.account.caption")), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, EAST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
		panel.add(getAccountField(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, EAST, NONE, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		panel.add(new PlainLabel(Resources.getInstance().getString("importtransactiondialog.transactionheader.description")), new GridBagConstraints(0, 1, 3, //$NON-NLS-1$
				1, 0.0, 0.0, WEST, HORIZONTAL, new Insets(0, 20, 0, 0), 0, 0));

		panel.add(getTransactionEditor(), new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.ZERO_INSETS, 0, 0));

		panel.add(new JLabel(Resources.getInstance().getString("importtransactiondialog.matchingtransactions.caption")), new GridBagConstraints(0, 3, 3, 1, //$NON-NLS-1$
				0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		panel.add(new PlainLabel(Resources.getInstance().getString("importtransactiondialog.matchingtransactions.description")), new GridBagConstraints(0, 4, //$NON-NLS-1$
				3, 1, 0.0, 0.0, WEST, HORIZONTAL, new Insets(0, 20, 0, 0), 0, 0));

		panel.add(new JScrollPane(getMatchingTransactionTable()), new GridBagConstraints(0, 5, 3, 1, 0.0, 1.0, CENTER, BOTH,
				InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		return panel;
	}

	protected JTextField getAccountField() {
		if (null == accountField)
			createAccountField();
		
		return accountField;
	}
	
	private void createAccountField() {
		accountField = new JTextField(20);
		accountField.setEditable(false);
	}
	
	protected ImportTransactionEditor getTransactionEditor() {
		if (null == transactionEditor)
			createImportTransactionEditor();

		return transactionEditor;
	}

	private void createImportTransactionEditor() {
		transactionEditor = new ImportTransactionEditor(getActionListener());
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
				assert null != command : "null command encountered in TransactionEditor ActionListener"; //$NON-NLS-1$

				if (command.equals("enter")) {//$NON-NLS-1$
					importTransaction();
					closeDialog();
				} else if (command.equals("cancel")) //$NON-NLS-1$
					closeDialog();
			}
		};
	}

	private void importTransaction() {
		getTransactionEditor().saveTransaction();
		GUIApplication.getInstance().getPersistenceManager().delete(downloadedTransaction);
	}

	private void closeDialog() {
		getTransactionEditor().setTransaction(null);
		setVisible(false);
	}

	protected JTable getMatchingTransactionTable() {
		if (null == matchingTransactionTable)
			createMatchingTransactionTable();

		return matchingTransactionTable;
	}

	private void createMatchingTransactionTable() {
		matchingTransactionTable = new JTable(createTransactionTableModel());
	}
	
	private TransactionWithoutBalanceTableModel createTransactionTableModel() {
		return new TransactionWithoutBalanceTableModel();
	}
	
	public void importTransaction(DownloadedTransaction downloadedTransaction) {
		if (null == downloadedTransaction)
			return;
		
		/*
		 * Deal with the case where the BankAccountId has yet to have been mapped with a real Account.
		 */
		Account account = downloadedTransaction.getBankAccountId().getAccount();
		getAccountField().setText(account.getName());
		((TransactionWithoutBalanceTableModel) getMatchingTransactionTable().getModel()).setAccount(account);
		getMatchingTransactionTable().getSelectionModel().clearSelection();

		final Date date = downloadedTransaction.getPostedDateTime();
		final String description = downloadedTransaction.getDescription();
		Transaction transaction = new Transaction();
		transaction.setDate(date);
		transaction.setDescription(description);
		
		List<PayeeMapping> results = GUIApplication.getInstance().getPersistenceManager().get("PayeeMapping.findPayeeMapping", PayeeMapping.class, PersistenceManager.createQueryParameterMap("text", description)); //$NON-NLS-1$ //$NON-NLS-2$
		if (0 < results.size())
			transaction.setPayee(results.get(0).getPayee());
		
		TransactionDetail detail = new TransactionDetail();
		detail.setAccount(account);
		detail.setAmount(downloadedTransaction.getAmount());
		
		transaction.getTransactionDetails().add(detail);
		
		getTransactionEditor().setTransaction(transaction);
		int index = ((TransactionWithoutBalanceTableModel) getMatchingTransactionTable().getModel()).findIndexOfTransactionWithDate(date);
		Rectangle rect = getMatchingTransactionTable().getCellRect(index, 0, false);
		getMatchingTransactionTable().scrollRectToVisible(rect);
		
		setVisible(true);
	}
}
