package ca.msbsoftware.accentis.persistence.pojos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 
 * @author Marc Boudreau
 *
 * @since 1.0
 */
@Entity
@NamedQueries({
	@NamedQuery(name=ScheduledTransaction.GET_ALL_SCHEDULEDTRANSACTIONS_QUERY, query="SELECT st FROM ScheduledTransaction st ORDER BY st.name"),
	@NamedQuery(name="ScheduledTransaction.getAllScheduledTransactionsToEnterInRegister", query="SELECT st FROM ScheduledTransaction st WHERE st.enterInRegisterAdvancedDays IS NOT NULL AND (st.lastEntryDate <= CURRENT_DATE OR st.lastEntryDate IS NULL)")
})
public class ScheduledTransaction extends NamedObject {

	public static final String GET_ALL_SCHEDULEDTRANSACTIONS_QUERY = "ScheduledTransaction.GET_ALL_SCHEDULEDTRANSACTION_QUERY";
	
	@Basic
	private Integer enterInRegisterAdvancedDays;
	
	@Temporal(TemporalType.DATE)
	private Date lastEntryDate;
	
	@Basic
	private int entryCount;
	
	@Embedded
	private Schedule schedule = new Schedule();
	
	@ManyToOne
	private Payee transactionPayee;
	
	@Basic
	private String transactionReference;
	
	@OneToMany(cascade = { CascadeType.ALL })
	private List<TransactionDetail> transactionDetails = new ArrayList<TransactionDetail>();
	
	@Lob
	private String transactionDescription;
	
	public Integer getEnterInRegisterAdvancedDays() {
		return enterInRegisterAdvancedDays;
	}
	
	public void setEnterInRegisterAdvancedDays(Integer value) {
		enterInRegisterAdvancedDays = value;
	}
	
	public Schedule getSchedule() {
		return schedule;
	}
	
	public Payee getTransactionPayee() {
		return transactionPayee;
	}
	
	public void setTransactionPayee(Payee value) {
		transactionPayee = value;
	}
	
	public String getTransactionReference() {
		return transactionReference;
	}
	
	public void setTransactionReference(String value) {
		transactionReference = value;
	}
	
	public List<TransactionDetail> getTransactionDetails() {
		return transactionDetails;
	}
	
	public String getTransactionDescription() {
		return transactionDescription;
	}
	
	public void setTransactionDescription(String value) {
		transactionDescription = value;
	}
	
	public BigDecimal getValue() {
		BigDecimal value = BigDecimal.ZERO;
		
		for (TransactionDetail detail : transactionDetails)
			value = value.add((null == detail.getAmount() ? BigDecimal.ZERO : detail.getAmount()));
		
		return value;
	}
	
	public Date getLastEntryDate() {
		return lastEntryDate;
	}
	
	public void setLastEntryDate(Date value) {
		lastEntryDate = value;
	}
	
	public int getEntryCount() {
		return entryCount;
	}
	
	public void setEntryCount(int count) {
		entryCount = count;
	}
}
