package ca.msbsoftware.accentis.persistence.pojos;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Transactions")
public class Transaction extends BaseObject {

	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Basic
	private String reference;
	
	@Basic
	private String description;

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
}