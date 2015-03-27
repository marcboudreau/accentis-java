package ca.msbsoftware.accentis.gui.views;

import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import ca.msbsoftware.accentis.persistence.pojos.Individual;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.managedialog.AbstractManageDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManageIndividualDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;
import ca.msbsoftware.accentis.gui.models.DateTableCellRenderer;

public class IndividualsView extends BasicTableView<Individual> {

	public IndividualsView(ManagePojoDialog dialog) {
		super("individuals", dialog); //$NON-NLS-1$
	}

	@Override
	protected String getViewTitleCaption() {
		return Resources.getInstance().getString("individualsview.title.caption"); //$NON-NLS-1$
	}

	@Override
	protected TableModel createTableModel() {
		return new IndividualTableModel();
	}

	@Override
	protected AbstractManageDialogPage<Individual> createManageDialogPage() {
		return new ManageIndividualDialogPage(getManagePojoDialog());
	}

	@Override
	public String getActionResourceKey() {
		return "action.view.individuals"; //$NON-NLS-1$
	}

	@Override
	protected void additionalTableCustomizations(JTable table) {
		table.setDefaultRenderer(Date.class, new DateTableCellRenderer());
	}
}
