package ca.msbsoftware.accentis.gui.models;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.utils.DateUtils;

@SuppressWarnings("serial")
public class TransactionWithoutBalanceTableModel extends AbstractTransactionTableModel {

	private static final String[] COLUMN_NAMES = { "date", "reference", "payee", "amount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private static final Class<?>[] COLUMN_CLASSES = { Date.class, String.class, Payee.class, BigDecimal.class };

	public TransactionWithoutBalanceTableModel() {
	}

	protected Map<String, Object> getPojoQueryParameters() {
		return PersistenceManager.createQueryParameterMap("account", account); //$NON-NLS-1$
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Transaction transaction = getTransactionAt(rowIndex);
		switch (columnIndex) {
		case 0:
			return transaction.getDate();
		case 1:
			return transaction.getReference();
		case 2:
			return transaction.getPayee();
		case 3:
			return transaction.getValueForAccount(getAccount());
		}

		return null;
	}

	@Override
	public String getColumnName(int column) {
		return Resources.getInstance().getString("transactiontable." + COLUMN_NAMES[column] + "column.name"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return COLUMN_CLASSES[columnIndex];
	}

	@Override
	protected IPojoListener createPojoListener() {
		return new IPojoListener() {
			@Override
			public boolean listensToClass(Class<? extends BaseObject> klass) {
				return Transaction.class.isInstance(klass);
			}
			
			@Override
			public void pojoCreated(BaseObject object) {
				Transaction transaction = (Transaction) object;
				int index = -Collections.binarySearch(list, transaction) - 1;
				if (0 > index)
					return;

				addTransaction(transaction, index);

				fireTableRowsInserted(index, index);
			}

			@Override
			public void pojoDeleted(BaseObject object) {
				Transaction transaction = (Transaction) object;
				int index = Collections.binarySearch(list, transaction);
				if (0 > index)
					return;

				removeTransaction(index);
				fireTableRowsDeleted(index, index);
			}

			@Override
			public void pojoSaved(BaseObject object) {
				Transaction transaction = (Transaction) object;
				int preSortIndex = list.indexOf(object);
				Collections.sort(list);
				int postSortIndex = Collections.binarySearch(list, transaction);

				boolean transactionMoved = preSortIndex != postSortIndex;

				if (transactionMoved) {
					fireTableRowsDeleted(preSortIndex, preSortIndex);
					fireTableRowsInserted(postSortIndex, postSortIndex);
				}
			}

			@Override
			public void pojoRefreshed(BaseObject object) {
			}
		};
	};
	
	public int findIndexOfTransactionWithDate(Date date) {
		final Date midnight = DateUtils.createDateAtMidnight(date);
		for (int i = 0; i < list.size(); ++i)
			if (!list.get(i).getDate().before(midnight))
				return i;
		
		return list.size();
	}
}