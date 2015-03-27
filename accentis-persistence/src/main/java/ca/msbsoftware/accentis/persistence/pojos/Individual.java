package ca.msbsoftware.accentis.persistence.pojos;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({@NamedQuery(name=Individual.GET_ALL_INDIVIDUALS_QUERY, query="SELECT o FROM Individual o"),@NamedQuery(name = Individual.GET_FILTERED_INDIVIDUALS_QUERY, query = "SELECT i FROM Individual i WHERE i <> :excludedIndividual")})
public class Individual extends NamedObject {

	public static final String GET_ALL_INDIVIDUALS_QUERY = "Individual.GET_ALL_INDIVIDUALS_QUERY";
	
	public static final String GET_FILTERED_INDIVIDUALS_QUERY = "Individual.GET_FILTERED_INDIVIDUALS_QUERY";
	
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	@Basic
	private String address;
	
	@Embedded
	private SIN sin;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Image picture;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Individual spouse;
	
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
	
	public SIN getSIN() {
		return sin;
	}
	
	public void setSIN(SIN value) {
		sin = value;
	}
	
	public Image getPicture() {
		return picture;
	}
	
	public void setPicture(Image value) {
		picture = value;
	}
	
	public Individual getSpouse() {
		return spouse;
	}
	
	public void setSpouse(Individual value) {
		unlinkSpouse(this);
		unlinkSpouse(value);
		
		linkSpouse(this, value);
	}
	
	private static void unlinkSpouse(Individual individual) {
		if (null != individual && null != individual.spouse) {
			individual.spouse.spouse = null;
			individual.spouse = null;
		}
	}
	
	private static void linkSpouse(Individual individual1, Individual individual2) {
		individual1.spouse = individual2;
		
		if (null != individual2)
			individual2.spouse = individual1;
	}
}
