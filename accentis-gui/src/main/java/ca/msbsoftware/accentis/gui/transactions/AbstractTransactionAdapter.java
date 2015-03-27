package ca.msbsoftware.accentis.gui.transactions;

import java.math.BigDecimal;
import java.util.Date;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;

public abstract class AbstractTransactionAdapter {

	private Transaction currentTransaction;
	
	private Account currentAccount;
	
	private String reference;
	
	private Date date;
	
	protected AbstractTransactionAdapter(Transaction transaction, Account account) {
		currentTransaction = transaction;
		currentAccount = account;
		
		reference = transaction.getReference();
		date = transaction.getDate();
	}
	
	protected Account getCurrentAccount() {
		return currentAccount;
	}
	
	public Transaction getActualTransaction() {
		return currentTransaction;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String value) {
		reference = value;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date value) {
		date = value;
	}
	
	public abstract BigDecimal getAmount();
	
	public void saveTransaction() {
		getActualTransaction().setReference(getReference());
		getActualTransaction().setDate(getDate());
	}
}
