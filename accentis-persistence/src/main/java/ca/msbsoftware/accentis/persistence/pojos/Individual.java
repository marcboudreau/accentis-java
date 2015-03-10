package ca.msbsoftware.accentis.persistence.pojos;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({@NamedQuery(name=Individual.GET_ALL_INDIVIDUALS_QUERY, query="SELECT o FROM Individual o")})
public class Individual extends NamedObject {

	public static final String GET_ALL_INDIVIDUALS_QUERY = "Individual.GET_ALL_INDIVIDUALS_QUERY";
	
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	private String address;
	
	private String socialInsuranceNumber;
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date value) {
		birthDate = value;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String value) {
		address = value;
	}
	
	public String getSocialInsuranceNumber() {
		return socialInsuranceNumber;
	}
	
	public void setSocialInsuranceNumber(String value) {
		socialInsuranceNumber = value;
	}
}
