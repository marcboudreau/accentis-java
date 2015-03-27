package ca.msbsoftware.accentis.gui.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

@SuppressWarnings("serial")
public class CurrencyComboBoxModel extends AbstractListModel<Currency> implements ComboBoxModel<Currency> {

	private List<Currency> currencies = createCurrencyList();
	
	private Object selectedCurrency;
	
	@Override
	public int getSize() {
		return currencies.size();
	}

	@Override
	public Currency getElementAt(int index) {
		return currencies.get(index);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selectedCurrency = anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selectedCurrency;
	}
	
	private List<Currency> createCurrencyList() {
		List<Currency> list = new ArrayList<Currency>(Currency.getAvailableCurrencies());
		Collections.sort(list, new Comparator<Currency>() {
			@Override
			public int compare(Currency c1, Currency c2) {
				return c1.getDisplayName().compareTo(c2.getDisplayName());
			}
		});
		
		return list;
	}
}
