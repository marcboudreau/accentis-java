package ca.msbsoftware.accentis.gui.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Category;
import ca.msbsoftware.accentis.persistence.pojos.Individual;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.TransactionDetail;

import ca.msbsoftware.accentis.gui.Resources;

@SuppressWarnings("serial")
public class TransactionDetailTableModel extends AbstractTableModel {

	private static final Class<?>[] COLUMN_CLASSES = { Account.class, Payee.class, Category.class, Individual.class, String.class,
			BigDecimal.class };

	private static final String[] COLUMN_NAMES = { "account", "payee", "category", "individual", "description", "amount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

	private List<Integer> detailIds = new ArrayList<Integer>();

	private List<Account> accounts = new ArrayList<Account>();

	private List<Payee> payees = new ArrayList<Payee>();

	private List<Category> categories = new ArrayList<Category>();

	private List<Individual> individuals = new ArrayList<Individual>();

	private List<String> descriptions = new ArrayList<String>();

	private List<BigDecimal> amounts = new ArrayList<BigDecimal>();

	public TransactionDetailTableModel() {
	}

	public void setTransactionDetails(List<TransactionDetail> details) {
		detailIds.clear();
		accounts.clear();
		payees.clear();
		categories.clear();
		individuals.clear();
		descriptions.clear();
		amounts.clear();

		if (null != details)
			for (TransactionDetail detail : details) {
				detailIds.add(detail.getId());
				accounts.add(detail.getAccount());
				payees.add(detail.getPayee());
				categories.add(detail.getCategory());
				individuals.add(detail.getIndividual());
				descriptions.add(detail.getDescription());
				amounts.add(detail.getAmount());
			}

		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return detailIds.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_CLASSES.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return getAccountValueAt(row);
		case 1:
			return getPayeeValueAt(row);
		case 2:
			return getCategoryValueAt(row);
		case 3:
			return getIndividualValueAt(row);
		case 4:
			return getDescriptionValueAt(row);
		case 5:
			return getAmountValueAt(row);
		}

		return null;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
		case 0:
			setAccountValueAt((Account) value, row);
			break;
		case 1:
			setPayeeValueAt((Payee) value, row);
			break;
		case 2:
			setCategoryValueAt((Category) value, row);
			break;
		case 3:
			setIndividualValueAt((Individual) value, row);
			break;
		case 4:
			setDescriptionValueAt((String) value, row);
			break;
		case 5:
			setAmountValueAt((BigDecimal) value, row);
			break;
		}
	}

	private Account getAccountValueAt(int row) {
		return accounts.get(row);
	}

	private void setAccountValueAt(Account value, int row) {
		accounts.set(row, value);
	}

	private Payee getPayeeValueAt(int row) {
		return payees.get(row);
	}

	private void setPayeeValueAt(Payee value, int row) {
		payees.set(row, value);
	}

	private Category getCategoryValueAt(int row) {
		return categories.get(row);
	}

	private void setCategoryValueAt(Category value, int row) {
		categories.set(row, value);
	}

	private Individual getIndividualValueAt(int row) {
		return individuals.get(row);
	}

	private void setIndividualValueAt(Individual value, int row) {
		individuals.set(row, value);
	}

	private String getDescriptionValueAt(int row) {
		return descriptions.get(row);
	}

	private void setDescriptionValueAt(String value, int row) {
		descriptions.set(row, value);
	}

	private BigDecimal getAmountValueAt(int row) {
		return amounts.get(row);
	}

	private void setAmountValueAt(BigDecimal value, int row) {
		amounts.set(row, value);
	}

	@Override
	public String getColumnName(int column) {
		return Resources.getInstance().getString("transactiondetailstable." + COLUMN_NAMES[column] + "column.name"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return COLUMN_CLASSES[column];
	}

	public void addDetail() {
		detailIds.add(0);
		accounts.add(null);
		payees.add(null);
		categories.add(null);
		individuals.add(null);
		descriptions.add(null);
		amounts.add(null);

		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}

	public void removeDetails(int[] rows) {
		for (int i = rows.length - 1; 0 <= i; --i)
			removeDetail(rows[i]);
	}

	public void removeDetail(int row) {
		detailIds.remove(row);
		accounts.remove(row);
		payees.remove(row);
		categories.remove(row);
		individuals.remove(row);
		descriptions.remove(row);
		amounts.remove(row);

		fireTableRowsDeleted(row, row);
	}

	public void saveTransactionDetails(List<TransactionDetail> details) {
		for (Iterator<TransactionDetail> it = details.iterator(); it.hasNext();) {
			TransactionDetail existingDetail = it.next();
			int pos = detailIds.indexOf(existingDetail.getId());

			if (0 > pos)
				it.remove();
			else
				updateDetail(existingDetail, pos);
		}

		for (int i = 0; i < detailIds.size(); ++i)
			if (0 == detailIds.get(i)) {
				TransactionDetail detail = new TransactionDetail();
				updateDetail(detail, i);
				details.add(detail);
			}
	}

	private void updateDetail(TransactionDetail detail, int pos) {
		detail.setAccount(getAccountValueAt(pos));
		detail.setPayee(getPayeeValueAt(pos));
		detail.setCategory(getCategoryValueAt(pos));
		detail.setIndividual(getIndividualValueAt(pos));
		detail.setDescription(getDescriptionValueAt(pos));
		detail.setAmount(getAmountValueAt(pos));
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == 3 && null != getValueAt(row, 2)) || (col != 3);
	}

	public BigDecimal getTotalAmount() {
		BigDecimal total = BigDecimal.ZERO;

		for (BigDecimal amount : amounts)
			total = total.add((null == amount ? BigDecimal.ZERO : amount));

		return total;
	}
}
