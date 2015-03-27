package ca.msbsoftware.accentis.gui.views;



import javax.swing.JTable;
import javax.swing.table.TableModel;

import ca.msbsoftware.accentis.persistence.CurrencyDecimal;
import ca.msbsoftware.accentis.persistence.pojos.Account;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.managedialog.AbstractManageDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManageAccountDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;
import ca.msbsoftware.accentis.gui.swing.CurrencyDecimalTableCellRenderer;

public class AccountsView extends BasicTableView<Account> {

	public AccountsView(ManagePojoDialog dialog) {
		super("accounts", dialog); //$NON-NLS-1$
	}

	@Override
	public String getActionResourceKey() {
		return "action.view.accounts"; //$NON-NLS-1$
	}
	
	@Override
	public TableModel createTableModel() {
		return new AccountTableModel();
	}
	
	@Override
	public AbstractManageDialogPage<Account> createManageDialogPage() {
		return new ManageAccountDialogPage(getManagePojoDialog());
	}

	@Override
	protected String getViewTitleCaption() {
		return Resources.getInstance().getString("accountsview.title.caption"); //$NON-NLS-1$
	}

	@Override
	protected void additionalTableCustomizations(JTable table) {
		table.setDefaultRenderer(CurrencyDecimal.class, new CurrencyDecimalTableCellRenderer());
	}
}
