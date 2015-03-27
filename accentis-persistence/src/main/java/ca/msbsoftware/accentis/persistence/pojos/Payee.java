package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({@NamedQuery(name=Payee.GET_ALL_PAYEES_QUERY, query="SELECT p FROM Payee p")})
public class Payee extends NamedObject {

	public static final String GET_ALL_PAYEES_QUERY = "Payee.GET_ALL_PAYEES_QUERY";
	
	@Basic
	private String mailingAddress;
	
	@Basic
	private String contactName;
	
	@Basic
	private String phoneNumber;
	
	@Basic
	private String emailAddress;
	
	@Basic
	private String website;
	
	public String getMailingAddress() {
		return mailingAddress;
	}
	
	public void setMailingAddress(String value) {
		mailingAddress = value;
	}
	
	public String getContactName() {
		return contactName;
	}
	
	public void setContactName(String value) {
		contactName = value;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String value) {
		phoneNumber = value;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String value) {
		emailAddress = value;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String value) {
		website = value;
	}
}
