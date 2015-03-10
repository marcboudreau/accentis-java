package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Institution extends NamedObject {

	@Basic
	private String contactName;
	
	@Basic
	private String mailingAddress;
	
	@Basic
	private String emailAddress;
	
	@Basic
	private String website;
	
	public String getContactName() {
		return contactName;
	}
	
	public void setContactName(String value) {
		contactName = value;
	}
	
	public String getMailingAddress() {
		return mailingAddress;
	}
	
	public void setMailingAddress(String value) {
		mailingAddress = value;
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
