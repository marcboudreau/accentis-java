package ca.msbsoftware.accentis.gui.views;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import ca.msbsoftware.accentis.persistence.pojos.NamedObject;

import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;

public abstract class BasicTableView<T extends NamedObject> extends BasicView<T> {

	private JTable table;
	
	protected BasicTableView(String id, ManagePojoDialog dialog) {
		super(id, dialog);
	}

	@Override
	protected JComponent getPojoListComponent() {
		return getTable();
	}
	
	protected JTable getTable() {
		if (null == table)
			createTable();
	
		return table;
	}

	private void createTable() {
		table = new JTable(createTableModel());
		table.getSelectionModel().addListSelectionListener(getTableSelectionListener());
		additionalTableCustomizations(table);
	}

	protected abstract TableModel createTableModel();

	class BasicTableListSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			getAction("button.modify").setEnabled(1 == table.getSelectedRowCount()); //$NON-NLS-1$
			getAction("button.remove").setEnabled(1 <= table.getSelectedRowCount()); //$NON-NLS-1$
		}
	}
	
	protected ListSelectionListener getTableSelectionListener() {
		return new BasicTableListSelectionListener();
	}
	
	protected void additionalTableCustomizations(JTable table) {
	}
	
	@SuppressWarnings("unchecked")
	protected T getSelectedPojo() {
		return ((BasicTableModel<T>) getTable().getModel()).getPojoAt(getTable().getSelectedRow());
	}
	
	@SuppressWarnings("unchecked")
	protected void removeSelectedPojos() {
		((BasicTableModel<T>) getTable().getModel()).removePojos(getTable().getSelectedRows());
	}
}
