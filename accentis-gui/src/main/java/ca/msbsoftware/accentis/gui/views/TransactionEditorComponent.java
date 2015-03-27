package ca.msbsoftware.accentis.gui.views;

import javax.swing.JOptionPane;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.persistence.pojos.TransactionDetail;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.ValidatingTabbedPane;

@SuppressWarnings("serial")
public class TransactionEditorComponent extends ValidatingTabbedPane {

	public static final int SIMPLE_TRANSACTION_TAB = 0;

	public static final int TRANSFER_TRANSACTION_TAB = 1;

	public static final int COMPLEX_TRANSACTION_TAB = 2;

	private Transaction transaction;

	private boolean editing;
	
	private boolean dirty;
	
	private Account account;
	
	public TransactionEditorComponent() {
		setValidator(createValidator());
		createTabs();
	}
	
	private ValidatingTabbedPane.Validator createValidator() {
		return new ValidatingTabbedPane.Validator() {
			@Override
			public boolean canSwitchTabs(int newTabIndex) {
				if (COMPLEX_TRANSACTION_TAB == getSelectedIndex()) {
					if (SIMPLE_TRANSACTION_TAB == newTabIndex) {
						if (1 < getTransaction().getTransactionDetails().size()) {
							JOptionPane.showMessageDialog(TransactionEditorComponent.this,
									Resources.getInstance().getString("transactioneditor.twotransactiondetailcannotconverttosimple.message")); //$NON-NLS-1$
	
							return false;
						}
					} else if (TRANSFER_TRANSACTION_TAB == newTabIndex) {
						if (2 < getTransaction().getTransactionDetails().size()) {
							JOptionPane.showMessageDialog(TransactionEditorComponent.this,
									Resources.getInstance().getString("transactioneditor.morethantwotransactiondetailcannotconverttotransfer.message")); //$NON-NLS-1$
	
							return false;
						} else if (2 == getTransaction().getTransactionDetails().size()) {
							TransactionDetail detail1 = getTransaction().getTransactionDetails().get(0);
							TransactionDetail detail2 = getTransaction().getTransactionDetails().get(1);
	
							Account account1 = detail1.getAccount();
							Account account2 = detail2.getAccount();
	
							if (account1.equals(account2)) {
								JOptionPane.showMessageDialog(TransactionEditorComponent.this,
										Resources.getInstance().getString("transactioneditor.twotransactiondetailsameaccountcannotconverttotransfer.message")); //$NON-NLS-1$
	
								return false;
							}
						}
					}
				} else if (TRANSFER_TRANSACTION_TAB == getSelectedIndex()) {
					TransferTransactionEditorTab view = (TransferTransactionEditorTab) getSelectedComponent();
					if (SIMPLE_TRANSACTION_TAB == newTabIndex) {
						Account fromAccount = (Account) view.getFromAccountComboBox().getSelectedItem();
						Account toAccount = (Account) view.getToAccountComboBox().getSelectedItem();
	
						if (null != fromAccount && null != toAccount) {
							JOptionPane.showMessageDialog(TransactionEditorComponent.this,
									Resources.getInstance().getString("transactioneditor.transfertransactionwithtwoaccountscannotconverttosimple.message")); //$NON-NLS-1$
	
							return false;
						}
					}
				}
	
				return true;
			}
		};
	}
	
	private void createTabs() {
		addTab("simple", new SimpleTransactionEditorTab(this)); //$NON-NLS-1$
		addTab("transfer", new TransferTransactionEditorTab(this)); //$NON-NLS-1$
		addTab("complex", new ComplexTransactionEditorTab(this)); //$NON-NLS-1$
	}

	public Transaction getTransaction() {
		return transaction;
	}
	
	public void setTransaction(Transaction value) {
		transaction = value;

		for (int i = 0; i < getTabCount(); ++i) {
			AbstractTransactionEditorTab view = (AbstractTransactionEditorTab) getComponentAt(i);

			if (null == value)
				view.clearTransaction();
			else
				view.setTransaction(value);
		}
	}
	
	public boolean isEditing() {
		return editing;
	}
	
	public void beginEditing() {
		editing = true;
		setEnabled(true);
	}
	
	public void endEditing() {
		editing = false;
		setEnabled(false);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		for (int i = 0; i < getTabCount(); ++i)
			getComponentAt(i).setEnabled(enabled);
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean value) {
		dirty = value;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account value) {
		account = value;
	}
	
	public void commitChanges() {
		((AbstractTransactionEditorTab) getComponentAt(getSelectedIndex())).commitChanges();
	}
}
