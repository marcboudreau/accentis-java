package ca.msbsoftware.accentis.gui.swing;

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ca.msbsoftware.accentis.utils.DecimalUtils;

@SuppressWarnings("serial")
public class TwoDecimalTableCellRenderer extends DefaultTableCellRenderer {

	public TwoDecimalTableCellRenderer() {
		setHorizontalAlignment(RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return super.getTableCellRendererComponent(table, formatValue(value), isSelected, hasFocus, row, column);
	}
	
	private Object formatValue(Object value) {
		if (value instanceof BigDecimal)
			return DecimalUtils.format((BigDecimal) value);
		
		return value;
	}
}
