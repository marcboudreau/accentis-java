package ca.msbsoftware.accentis.gui.transactions;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Individual;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.persistence.pojos.TransactionDetail;

import ca.msbsoftware.accentis.gui.Resources;

public class TransferTransactionAdapter extends AbstractTransactionAdapter {

	private Account fromAccount;

	private Account toAccount;

	private Individual individual;

	private BigDecimal amount;

	private String description;

	public TransferTransactionAdapter(Transaction transaction, Account account) {
		super(transaction, account);

		description = transaction.getDescription();
		List<TransactionDetail> details = transaction.getTransactionDetails();

		if (0 < details.size()) {
			TransactionDetail detail = details.get(0);
			fromAccount = detail.getAccount();
			individual = detail.getIndividual();
			amount = detail.getAmount();

			if (1 < details.size()) {
				detail = details.get(1);
				toAccount = detail.getAccount();
			}
		}
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

	public Account getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(Account value) {
		fromAccount = value;
	}

	public Account getToAccount() {
		return toAccount;
	}

	public void setToAccount(Account value) {
		toAccount = value;
	}

	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual value) {
		individual = value;
	}

	public Payee getPayee() {
		final boolean transferFrom = isTransferFrom();

		return new Payee() {
			@Override
			public String toString() {
				return MessageFormat
						.format(Resources.getInstance().getString("transactioneditor.transfertransaction.payee"), (transferFrom ? Resources.getInstance().getString("transactioneditor.transfertransaction.payee.from") : Resources.getInstance().getString("transactioneditor.transfertransaction.payee.to")), (transferFrom ? getFromAccount() : getToAccount())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		};
	}

	private boolean isTransferFrom() {
		return getCurrentAccount().equals(getToAccount());
	}

	@Override
	public void saveTransaction() {
		super.saveTransaction();

		List<TransactionDetail> details = getActualTransaction().getTransactionDetails();
		TransactionDetail fromDetail;
		TransactionDetail toDetail;

		if (0 < details.size()) {
			fromDetail = details.get(0);

			if (1 < details.size())
				toDetail = details.get(1);
			else {
				toDetail = new TransactionDetail();
				details.add(toDetail);
			}

			if (2 < details.size())
				details.subList(2, details.size()).clear();
		} else {
			fromDetail = new TransactionDetail();
			toDetail = new TransactionDetail();

			details.add(fromDetail);
			details.add(toDetail);
		}

		saveTransactionDetail(fromDetail, fromAccount, getAmount());
		saveTransactionDetail(toDetail, toAccount, getAmount().negate());

		getActualTransaction().setDescription(getDescription());
	}

	private void saveTransactionDetail(TransactionDetail detail, Account account, BigDecimal amount) {
		detail.setAccount(account);
		detail.setAmount(amount);
		detail.setIndividual(getIndividual());
	}
}
