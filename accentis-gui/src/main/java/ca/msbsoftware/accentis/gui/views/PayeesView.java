package ca.msbsoftware.accentis.gui.views;

import javax.swing.table.TableModel;

import ca.msbsoftware.accentis.persistence.pojos.Payee;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.managedialog.AbstractManageDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManagePayeeDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;

public class PayeesView extends BasicTableView<Payee> {

	public PayeesView(ManagePojoDialog dialog) {
		super("payees", dialog); //$NON-NLS-1$
	}

	public String getActionResourceKey() {
		return "action.view.payees"; //$NON-NLS-1$
	}

	@Override
	protected TableModel createTableModel() {
		return new PayeeTableModel();
	}
	
	protected AbstractManageDialogPage<Payee> createManageDialogPage() {
		return new ManagePayeeDialogPage(getManagePojoDialog());
	}

	@Override
	protected String getViewTitleCaption() {
		return Resources.getInstance().getString("payeesview.title.caption"); //$NON-NLS-1$
	}
}
