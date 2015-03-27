package ca.msbsoftware.accentis.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.msbsoftware.accentis.gui.dialogs.MSBDialog;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;
import ca.msbsoftware.accentis.gui.swing.PojoComboBox;
import ca.msbsoftware.accentis.persistence.pojos.Account;

import ca.msbsoftware.accentis.importer.IBankAccountIdMappingHandler;

@SuppressWarnings("serial")
public class BankAccountIdMappingDialog extends MSBDialog implements IBankAccountIdMappingHandler {

	public BankAccountIdMappingDialog(Window owner) {
		super(owner, Resources.getInstance().getString("bankaccountidmappingdialog.title"), ModalityType.APPLICATION_MODAL); //$NON-NLS-1$
	}

	private PojoComboBox<Account> accountComboBox;

	private JTextField bankAccountIdField;

	@Override
	protected JComponent getContentComponent() {
		JPanel panel = new JPanel(new GridBagLayout());

		panel.add(
				new JLabel(Resources.getInstance().getString("bankaccountidmappingdialog.bankaccountid.label")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, InsetsConstants.ZERO_INSETS, 0, 0)); //$NON-NLS-1$
		panel.add(getBankAccountIdField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		panel.add(
				new JLabel(Resources.getInstance().getString("bankaccountidmappingdialog.account.label")), new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0)); //$NON-NLS-1$
		panel.add(getAccountComboBox(), new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		return panel;
	}

	protected JTextField getBankAccountIdField() {
		if (null == bankAccountIdField)
			createBankAccountIdField();

		return bankAccountIdField;
	}

	private void createBankAccountIdField() {
		bankAccountIdField = new JTextField();
		bankAccountIdField.setEditable(false);
		bankAccountIdField.setColumns(20);
	}

	protected PojoComboBox<Account> getAccountComboBox() {
		if (null == accountComboBox)
			createAccountComboBox();

		return accountComboBox;
	}

	private void createAccountComboBox() {
		accountComboBox = new PojoComboBox<Account>(new PojoComboBoxModel<Account>(Account.GET_ALL_ACCOUNTS_QUERY, Account.class), Account.class);
	}

	@Override
	protected void doCancel() {
		getAccountComboBox().setSelectedItem(null);
		
		super.doCancel();
	}
	
	@Override
	protected boolean canFinish() {
		return -1 != getAccountComboBox().getSelectedIndex();
	}

	@Override
	public Account handleMapping(String bankAccountId) {
		getBankAccountIdField().setText(bankAccountId);
		
		setVisible(true);
		
		return (Account) getAccountComboBox().getSelectedItem();
	}
}
