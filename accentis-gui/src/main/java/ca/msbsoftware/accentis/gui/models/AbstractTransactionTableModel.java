package ca.msbsoftware.accentis.gui.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.listeners.PojoListenerManager;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.gui.GUIApplication;

@SuppressWarnings("serial")
public abstract class AbstractTransactionTableModel extends AbstractTableModel implements IPersistenceManagerListener {

	protected List<Transaction> list = new ArrayList<Transaction>();
	
	protected Account account;
	
	private final Map<String, Object> queryParameters = new HashMap<String, Object>();
	
	protected IPojoListener pojoListener;
	
	protected AbstractTransactionTableModel() {
		GUIApplication.getInstance().addPersistenceManagerListener(this);
		PojoListenerManager.getInstance().addPojoListener(getTransactionDataManagerListener());
	}
	
	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return null;
	}

	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		clear();
		
		load(persistenceManager);
	}

	protected List<Transaction> getTransactions() {
		return list;
	}

	protected void addTransaction(Transaction transaction, int index) {
		list.add(index, transaction);
	}
	
	protected void removeTransaction(int index) {
		list.remove(index);
	}

	public Transaction getTransactionAt(int index) {
		return list.get(index);
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account value) {
		account = value;
		queryParameters.put("account", value); //$NON-NLS-1$
	
		reload();
	}

	protected void clear() {
		int size = getRowCount();
		list.clear();
		fireTableRowsDeleted(0, size);
	}

	public void reload() {
		clear();
		load(GUIApplication.getInstance().getPersistenceManager());
	}

	
	private void load(PersistenceManager persistenceManager) {
		if (null != persistenceManager && null != account) {
			list.clear();
	
			List<Transaction> transactions = persistenceManager.<Transaction> get(Transaction.GET_TRANSACTION_DETAILS_FOR_ACCOUNT_QUERY, Transaction.class, queryParameters);
			for (int i = 0; i < transactions.size(); ++i) {
				Transaction transaction = transactions.get(i);
				addTransaction(transaction, i);
			}

			fireTableRowsInserted(0, getRowCount());
		}
	}
	
	protected IPojoListener getTransactionDataManagerListener() {
		if (null == pojoListener)
			pojoListener = createPojoListener();
		
		return pojoListener;
	}
	
	protected abstract IPojoListener createPojoListener();
}
