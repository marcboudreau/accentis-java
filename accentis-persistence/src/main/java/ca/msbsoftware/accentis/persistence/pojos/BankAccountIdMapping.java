package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * 
 * 
 * @author Marc Boudreau
 *
 * @since 1.0
 */
@Entity
@NamedQueries(@NamedQuery(name = BankAccountIdMapping.GET_MAPPING_FOR_ACCOUNT_QUERY, query = "SELECT m FROM BankAccountIdMapping m WHERE m.bankAccountId = :bankAccountId"))
public class BankAccountIdMapping extends BaseObject {

	public static final String GET_MAPPING_FOR_ACCOUNT_QUERY = "BankAccountIdMapping.GET_MAPPING_FOR_ACCOUNT_QUERY";
	
	@Basic
	@Column(unique=true,length=200)
	private String bankAccountId;

	@ManyToOne
	private Account account;

	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String value) {
		bankAccountId = value;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account value) {
		account = value;
	}
}
