package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Payee extends NamedObject {

	@Basic
	private String mailingAddress;
	
	@Basic
	private String contactName;
	
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
