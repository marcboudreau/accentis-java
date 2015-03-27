package ca.msbsoftware.accentis.persistence.pojos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Transactions")
@NamedQueries({@NamedQuery(name=Transaction.GET_TRANSACTION_DETAILS_FOR_ACCOUNT_QUERY,query="SELECT t FROM Transaction t, IN(t.details) td WHERE td.account = :account ORDER BY t.date, t.id")})
public class Transaction extends BaseObject implements Comparable<Transaction> {

	public static final String GET_TRANSACTION_DETAILS_FOR_ACCOUNT_QUERY = "Transaction.Get_Transaction_Details_For_Account_Query";
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Basic
	private String reference;
	
	@Basic
	private String description;
	
	@OneToMany
	private List<TransactionDetail> details = new ArrayList<TransactionDetail>();

	@ManyToOne
	private Payee payee;
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date value) {
		date = value;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String value) {
		reference = value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String value) {
		description = value;
	}
	
	public List<TransactionDetail> getTransactionDetails() {
		return details;
	}
	
	public Payee getPayee() {
		return payee;
	}
	
	public void setPayee(Payee value) {
		payee = value;
	}
	
	public BigDecimal getValueForAccount(Account account) {
		BigDecimal result = BigDecimal.ZERO;
		
		for (TransactionDetail detail : details)
			if (detail.getAccount().equals(account))
				result = result.add(detail.getAmount());
		
		return result;
	}

	@Override
	public int compareTo(Transaction o) {
		if (null == o)
			return -1;

		int result = compareToByDate(o.getDate());
		if (0 != result)
			return result;

		return getId() - o.getId();
	}

	private int compareToByDate(Date otherDate) {
		Date thisDate = getDate();
		if (null == thisDate)
			if (null == otherDate)
				return 0;
			else
				return 1;
		else if (null == otherDate)
			return -1;
		else
			return thisDate.compareTo(otherDate);
	}
}