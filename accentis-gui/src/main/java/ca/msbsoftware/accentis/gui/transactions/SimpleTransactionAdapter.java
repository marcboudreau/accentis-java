package ca.msbsoftware.accentis.gui.transactions;

import java.math.BigDecimal;
import java.util.List;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Category;
import ca.msbsoftware.accentis.persistence.pojos.Individual;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.persistence.pojos.TransactionDetail;

public class SimpleTransactionAdapter extends AbstractTransactionAdapter {

	private Account account;
	
	private BigDecimal amount;
	
	private Individual individual;
	
	private Category category;
	
	private String description;
	
	private Payee payee;
	
	public SimpleTransactionAdapter(Transaction transaction, Account account) {
		super(transaction, account);

		this.account = account;
		
		List<TransactionDetail> details = transaction.getTransactionDetails();
		if (0 < details.size()) {
			TransactionDetail detail = details.get(0);
			amount = detail.getAmount();
			individual = detail.getIndividual();
			category = detail.getCategory();
		}
		
		description = transaction.getDescription();
		payee = transaction.getPayee();
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account value) {
		account = value;
	}
	
	@Override
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal value) {
		amount = value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String value) {
		description = value;
	}
	
	public Individual getIndividual() {
		return individual;
	}
	
	public void setIndividual(Individual value) {
		individual = value;
	}

	public Payee getPayee() {
		return payee;
	}
	
	public void setPayee(Payee value) {
		payee = value;
	}

	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category value) {
		category = value;
	}
	
	@Override
	public void saveTransaction() {
		super.saveTransaction();
		
		getActualTransaction().setDescription(getDescription());
		getActualTransaction().setPayee(getPayee());

		List<TransactionDetail> details = getActualTransaction().getTransactionDetails();
		if (1 > details.size()) {
			TransactionDetail detail = new TransactionDetail();
			saveTransactionDetail(detail);
			details.add(detail);
		} else {
			TransactionDetail detail = details.get(0);
			saveTransactionDetail(detail);
			
			if (1 < details.size())
				details.subList(1, details.size()).clear();
		}
	}
	
	private void saveTransactionDetail(TransactionDetail detail) {
		detail.setAccount(getAccount());
		detail.setAmount(getAmount());
		detail.setCategory(getCategory());
		detail.setIndividual(getIndividual());
	}
}
