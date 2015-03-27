package ca.msbsoftware.accentis.gui.models;

import java.awt.Component;
import java.util.Currency;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import ca.msbsoftware.accentis.gui.Resources;

@SuppressWarnings("serial")
public class CurrencyComboBoxRenderer extends DefaultListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (null != value)
			value = createDisplayNameAndSymbol((Currency) value);
		
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	} 
	
	private String createDisplayNameAndSymbol(Currency currency) {
		String displayName = currency.getDisplayName();
		String symbol = currency.getSymbol();
		
		if (displayName.equals(symbol))
			return displayName;
		else
			return String.format(Resources.getInstance().getString("currencycomborenderer.displaynamewithsymbol"), displayName, symbol); //$NON-NLS-1$
	}
}