package ca.msbsoftware.accentis.gui.views;

import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;

import ca.msbsoftware.accentis.gui.swing.TwoDecimalTextField;

@SuppressWarnings("serial")
public class BigDecimalTableCellEditor extends DefaultCellEditor implements TableCellEditor {

	public BigDecimalTableCellEditor() {
		super(new TwoDecimalTextField());
	}

	@Override
	public Object getCellEditorValue() {
		return ((TwoDecimalTextField) editorComponent).getValue();
	}
}
