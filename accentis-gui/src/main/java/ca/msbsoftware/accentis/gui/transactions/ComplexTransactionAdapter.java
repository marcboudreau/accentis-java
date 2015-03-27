package ca.msbsoftware.accentis.gui.transactions;

import java.math.BigDecimal;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;

import ca.msbsoftware.accentis.gui.models.TransactionDetailTableModel;

public class ComplexTransactionAdapter extends AbstractTransactionAdapter {

	private Payee payee;

	private String description;

	private TransactionDetailTableModel tableModel;
	
	public ComplexTransactionAdapter(Transaction transaction, Account account, TransactionDetailTableModel model) {
		super(transaction, account);

		payee = transaction.getPayee();
		description = transaction.getDescription();
		tableModel = model;
		tableModel.setTransactionDetails(transaction.getTransactionDetails());
	}

	@Override
	public BigDecimal getAmount() {
		return tableModel.getTotalAmount();
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee value) {
		payee = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String value) {
		description = value;
	}

	public void saveTransaction() {
		super.saveTransaction();

		getActualTransaction().setPayee(getPayee());
		getActualTransaction().setDescription(getDescription());

		tableModel.saveTransactionDetails(getActualTransaction().getTransactionDetails());
	}
}
