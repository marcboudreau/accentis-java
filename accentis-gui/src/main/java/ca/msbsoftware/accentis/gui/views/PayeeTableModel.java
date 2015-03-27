package ca.msbsoftware.accentis.gui.views;

import ca.msbsoftware.accentis.persistence.pojos.Payee;

@SuppressWarnings("serial")
class PayeeTableModel extends BasicTableModel<Payee> {

	private static String[] COLUMN_NAMES = { "name", "contact", "phonenumber", "email", "website" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	@Override
	public String[] getColumnNames() {
		return COLUMN_NAMES;
	}
	
	@Override
	protected String getResourcePrefix() {
		return "payee"; //$NON-NLS-1$
	}

	@Override
	public Object getValue(Payee object, int column) {
		switch (column) {
		case 0:
			return object.getName();
		case 1:
			return object.getContactName();
		case 2:
			return object.getPhoneNumber();
		case 3:
			return object.getEmailAddress();
		case 4:
			return object.getWebsite();
		}

		return null;
	}

	@Override
	protected String getQueryName() {
		return Payee.GET_ALL_PAYEES_QUERY;
	}

	@Override
	protected Class<Payee> getPojoClass() {
		return Payee.class;
	}
}