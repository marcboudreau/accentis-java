package ca.msbsoftware.accentis.gui.models;

import java.awt.Component;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ca.msbsoftware.accentis.utils.DateUtils;

@SuppressWarnings("serial")
public class DateTableCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return super.getTableCellRendererComponent(table, formatValue(value), isSelected, hasFocus, row, column);
	}

	private Object formatValue(Object value) {
		if (value instanceof Date)
			return DateUtils.format((Date) value);
			
		return value;
	}
}
