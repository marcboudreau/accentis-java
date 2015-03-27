package ca.msbsoftware.accentis.gui.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.gui.Resources;

@SuppressWarnings("serial")
public class TransactionWithBalanceTableModel extends AbstractTransactionTableModel {

	private static final String[] COLUMN_NAMES = { "date", "reference", "payee", "amount", "balance" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	private static final Class<?>[] COLUMN_CLASSES = { Date.class, String.class, Payee.class, BigDecimal.class, BigDecimal.class };

	private List<Transaction> list = new ArrayList<Transaction>();

	private List<BigDecimal> balances = new ArrayList<BigDecimal>();

	protected Account account;

	private final Map<String, Object> queryParameters = new HashMap<String, Object>();

	public TransactionWithBalanceTableModel() {
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account value) {
		account = value;
		queryParameters.put("account", value); //$NON-NLS-1$

		reload();
	}

	protected List<Transaction> getTransactions() {
		return list;
	}

	protected List<BigDecimal> getBalances() {
		return balances;
	}

	@Override
	protected void clear() {
		balances.clear();
		super.clear();
	}

	public Transaction getTransactionAt(int index) {
		return list.get(index);
	}

	private BigDecimal getBalanceAt(int index) {
		if (-1 == index)
			return getAccount().getStartBalance();

		return balances.get(index);
	}

	protected Map<String, Object> getPojoQueryParameters() {
		return PersistenceManager.createQueryParameterMap("account", account); //$NON-NLS-1$
	}

	private void calculateBalances(int fromIndex, int toIndex) {
		if (toIndex < fromIndex)
			return;

		balances.subList(fromIndex, toIndex).clear();

		BigDecimal balance = getBalanceAt(fromIndex - 1);
		List<BigDecimal> insertedBalances = new ArrayList<BigDecimal>();

		for (int index = fromIndex; index <= toIndex; ++index) {
			balance = balance.add(list.get(index).getValueForAccount(getAccount()));
			insertedBalances.add(balance);
		}

		balances.addAll(fromIndex, insertedBalances);
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (4 == columnIndex)
			return balances.get(rowIndex);

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

				list.add(index, transaction);

				BigDecimal previousBalance = getBalanceAt(index - 1);
				balances.add(index, previousBalance.add(transaction.getValueForAccount(getAccount())));

				fireTableRowsInserted(index, index);

				if (index < getRowCount() - 1 && 0 != transaction.getValueForAccount(getAccount()).compareTo(BigDecimal.ZERO)) {
					calculateBalances(index + 1, getRowCount() - 1);
					fireTableChanged(new TableModelEvent(TransactionWithBalanceTableModel.this, index + 1, getRowCount() - 1, 4));
				}
			}

			
			@Override
			public void pojoDeleted(BaseObject object) {
				if (object instanceof Transaction)
					transactionRemoved((Transaction) object);
				else
					accountRemoved((Account) object);
			}
			
			private void transactionRemoved(Transaction transaction) { 
				int index = Collections.binarySearch(list, transaction);
				if (0 > index)
					return;

				list.remove(index);
				balances.remove(index);
				fireTableRowsDeleted(index, index);

				if (index < getRowCount() - 1) {
					calculateBalances(index, getRowCount() - 1);
					fireTableChanged(new TableModelEvent(TransactionWithBalanceTableModel.this, index, getRowCount() - 1, 4));
				}
			}
			
			private void accountRemoved(Account account) {
				clear();
			}

			@Override
			public void pojoSaved(BaseObject object) {
				if (object instanceof Transaction)
					transactionSaved((Transaction) object);
				else
					accountSaved((Account) object);
			}
			
			private void transactionSaved(Transaction transaction) {
				/*
				 * When a Transaction has been saved, its date, which is the sort key, made have been modified, so we need to determine if the row corresponding
				 * to it has moved. In that case, the balances between the original position and the new position need to be recalculated.
				 * 
				 * Its amount could also have changed. In that case, the balances between the original position and the new position as well as every balances
				 * after that range need to be recalculated.
				 * 
				 * Determining if the position of the Transaction needs to change is done by recording the current position (original position), resorting the
				 * list, and then comparing the recorded original position to the new position. If they are different, the row has moved.
				 * 
				 * Determining if the amount has changed is done by calculating the sum of the value of the Transaction and the balance of the previous
				 * Transaction and comparing the result to the balance of the current Transaction. If they are different, the amount has changed.
				 */
				int preSortIndex = list.indexOf(transaction);
				Collections.sort(list);
				int postSortIndex = Collections.binarySearch(list, transaction);

				boolean amountChanged = hasAmountChanged(transaction, preSortIndex);
				boolean transactionMoved = preSortIndex != postSortIndex;

				int startIndex = Math.min(preSortIndex, postSortIndex);
				int endIndex = Math.max(preSortIndex, postSortIndex);

				if (amountChanged)
					endIndex = getRowCount();

				calculateBalances(startIndex, endIndex - 1);

				if (transactionMoved) {
					fireTableRowsDeleted(preSortIndex, preSortIndex);
					fireTableRowsInserted(postSortIndex, postSortIndex);
				}

				if (startIndex < endIndex)
					fireTableChanged(new TableModelEvent(TransactionWithBalanceTableModel.this, startIndex, endIndex - 1, 4));
			}
			
			private void accountSaved(Account account) {
				handleAccountModification(account);
			}

			@Override
			public void pojoRefreshed(BaseObject object) {
				if (object instanceof Account)
					accountRefreshed((Account) object);
			}

			private void accountRefreshed(Account account) {
				handleAccountModification(account);
			}
			
			private boolean hasAmountChanged(Transaction object, int index) {
				BigDecimal balance = getBalanceAt(index - 1);
				balance = balance.add(object.getValueForAccount(getAccount()));

				return 0 != balance.compareTo(getBalanceAt(index));
			}

			private void handleAccountModification(Account object) {
				if (hasAccountStartingBalanceChanged(object)) {
					calculateBalances(0, getRowCount() - 1);
					fireTableChanged(new TableModelEvent(TransactionWithBalanceTableModel.this, 0, getRowCount() - 1, getColumnCount()));
				}
			}

			private boolean hasAccountStartingBalanceChanged(Account object) {
				if (0 == balances.size())
					return false;

				BigDecimal balance = object.getStartBalance();
				BigDecimal firstBalance = balances.get(0);
				Transaction firstTransaction = list.get(0);

				return 0 != firstBalance.compareTo(balance.add(firstTransaction.getValueForAccount(getAccount())));
			}

		};
	}
}
