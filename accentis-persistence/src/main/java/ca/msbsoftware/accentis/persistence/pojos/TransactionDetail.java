package ca.msbsoftware.accentis.persistence.pojos;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class TransactionDetail extends BaseObject {

	@ManyToOne
	private Account account;

	@ManyToOne
	private Payee payee;

	@ManyToOne
	private Individual individual;

	@ManyToOne
	private Category category;

	@Basic
	private BigDecimal amount;

	@Basic
	private String description;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account value) {
		account = value;
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee value) {
		payee = value;
	}

	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual value) {
		individual = value;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category value) {
		category = value;
	}

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
	
	public BigDecimal getValue() {
		BigDecimal value = getAmount();
		if (null != getCategory() && CategoryType.EXPENSE.equals(getCategory().getType()))
			value = value.negate();
		
		return value;
	}
}
