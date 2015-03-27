package ca.msbsoftware.accentis.gui.views;

import java.math.BigDecimal;
import java.util.Date;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.DownloadedTransaction;

@SuppressWarnings("serial")
public class DownloadedTransactionTableModel extends BasicTableModel<DownloadedTransaction> {

	private static final String[] COLUMN_NAMES = { "account", "date", "description", "amount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private static final Class<?>[] COLUMN_CLASSES = { Account.class, Date.class, String.class, BigDecimal.class };

	@Override
	protected String[] getColumnNames() {
		return COLUMN_NAMES;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return COLUMN_CLASSES[columnIndex];
	}

	@Override
	protected String getResourcePrefix() {
		return "downloadedtransaction"; //$NON-NLS-1$
	}

	@Override
	protected Object getValue(DownloadedTransaction object, int column) {
		switch (column) {
		case 0:
			return object.getBankAccountId().getAccount();
		case 1:
			return object.getPostedDateTime();
		case 2:
			return object.getDescription();
		case 3:
			return object.getAmount();
		}

		return null;
	}

	@Override
	protected String getQueryName() {
		return DownloadedTransaction.GET_ALL_DOWNLOADEDTRANSACTIONS_QUERY;
	}

	@Override
	protected Class<DownloadedTransaction> getPojoClass() {
		return DownloadedTransaction.class;
	}
}
