package ca.msbsoftware.accentis.gui.views;

import ca.msbsoftware.accentis.persistence.pojos.ScheduledTransaction;

@SuppressWarnings("serial")
public class ScheduledTransactionTableModel extends BasicTableModel<ScheduledTransaction> {

	private static final String[] COLUMN_NAMES = { "name", "payee", "frequency", "amount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	
	@Override
	protected String[] getColumnNames() {
		return COLUMN_NAMES;
	}
	
	@Override
	protected String getResourcePrefix() {
		return "scheduledtransaction"; //$NON-NLS-1$
	}

	@Override
	protected Object getValue(ScheduledTransaction object, int column) {
		switch(column) {
		case 0:
			return object.getName();
		case 1:
			return object.getTransactionPayee();
		case 2:
			return String.format("%s %s", object.getSchedule().getRepeatFrequency().toString(), object.getSchedule().getRepeatPeriod().toString()); //$NON-NLS-1$
		case 3:
			return object.getValue();
		}
		
		return null;
	}

	@Override
	protected String getQueryName() {
		return ScheduledTransaction.GET_ALL_SCHEDULEDTRANSACTIONS_QUERY;
	}

	@Override
	protected Class<ScheduledTransaction> getPojoClass() {
		return ScheduledTransaction.class;
	}
}
