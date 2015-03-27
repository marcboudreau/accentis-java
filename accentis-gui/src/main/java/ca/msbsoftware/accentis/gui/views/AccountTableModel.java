package ca.msbsoftware.accentis.gui.views;

import ca.msbsoftware.accentis.persistence.CurrencyDecimal;
import ca.msbsoftware.accentis.persistence.pojos.AccountType;
import ca.msbsoftware.accentis.persistence.pojos.Account;

@SuppressWarnings("serial")
public class AccountTableModel extends BasicTableModel<Account> {

	private static String[] COLUMN_NAMES = { "name", "number", "type", "balance" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private static Class<?>[] COLUMN_CLASSES = { String.class, String.class, AccountType.class, CurrencyDecimal.class };

	@Override
	public Class<?> getColumnClass(int column) {
		return COLUMN_CLASSES[column];
	}

	@Override
	public Object getValue(Account object, int column) {
		switch (column) {
		case 0:
			return object.getName();
		case 1:
			return object.getNumber();
		case 2:
			return object.getType();
		case 3:
			return new CurrencyDecimal(object.getStartBalance(), object.getCurrency());
		}

		return null;
	}

	@Override
	protected String[] getColumnNames() {
		return COLUMN_NAMES;
	}
	
	@Override
	protected String getResourcePrefix() {
		return "account"; //$NON-NLS-1$
	}

	@Override
	protected String getQueryName() {
		return Account.GET_ALL_ACCOUNTS_QUERY;
	}

	@Override
	protected Class<Account> getPojoClass() {
		return Account.class;
	}
}