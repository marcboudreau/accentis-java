package ca.msbsoftware.accentis.persistence.pojos;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({@NamedQuery(name=Account.GET_ALL_ACCOUNTS_QUERY, query="SELECT o FROM Account o")})
public class Account extends NamedObject {

	public static final String GET_ALL_ACCOUNTS_QUERY = "Account.GET_ALL_ACCOUNTS_QUERY";
	
	@Enumerated(EnumType.STRING)
	private AccountType type;
	
	@Basic
	private String number;
	
	@ManyToOne
	private Institution institution;
	
	@Basic
	private BigDecimal startBalance;
	
	@Basic
	private BigDecimal minimumBalance;
	
	@Basic
	private boolean closed;
	
	public AccountType getType() {
		return type;
	}
	
	public void setType(AccountType value) {
		type = value;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String value) {
		number = value;
	}
	
	public Institution getInstitution() {
		return institution;
	}
	
	public void setInstitution(Institution value) {
		institution = value;
	}
	
	public BigDecimal getStartBalance() {
		return startBalance;
	}
	
	public void setStartBalance(BigDecimal value) {
		startBalance = value;
	}
	
	public BigDecimal getMinimumBalance() {
		return minimumBalance;
	}
	
	public void setMinimumBalance(BigDecimal value) {
		minimumBalance = value;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void setClosed(boolean value) {
		closed = value;
	}
}
