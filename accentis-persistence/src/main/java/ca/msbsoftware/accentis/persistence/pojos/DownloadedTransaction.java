package ca.msbsoftware.accentis.persistence.pojos;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

/**
 * 
 * 
 * @author Marc Boudreau
 * 
 * @since 1.0
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "DownloadedTransaction.countTransactionsForAccount", query = "SELECT COUNT(dt) FROM DownloadedTransaction dt WHERE dt.bankAccountId.account = :account"),
		@NamedQuery(name = "DownloadedTransaction.getTransactionsForAccount", query = "SELECT dt FROM DownloadedTransaction dt WHERE dt.bankAccountId.account = :account"),
		@NamedQuery(name = DownloadedTransaction.GET_ALL_DOWNLOADEDTRANSACTIONS_QUERY, query = "SELECT dt FROM DownloadedTransaction dt" )})
public class DownloadedTransaction extends NamedObject {

	public static final String GET_ALL_DOWNLOADEDTRANSACTIONS_QUERY = "DownloadedTransaction.GET_ALL_DOWNLOADEDTRANSACTIONS_QUERY";
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private BankAccountIdMapping bankAccountId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date postedDateTime;

	@Basic
	private BigDecimal amount;

	public BankAccountIdMapping getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(BankAccountIdMapping bankAccountIdMapping) {
		bankAccountId = bankAccountIdMapping;
	}

	public Date getPostedDateTime() {
		return postedDateTime;
	}

	public void setPostedDateTime(Date value) {
		postedDateTime = value;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal value) {
		amount = value;
	}
}
