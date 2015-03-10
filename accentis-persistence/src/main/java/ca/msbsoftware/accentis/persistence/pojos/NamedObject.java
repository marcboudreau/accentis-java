package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class NamedObject extends BaseObject implements Comparable<NamedObject>{

	@Basic
	private String name;
	
	@Basic
	private String description;
	
	public String getName() {
		return name;
	}
	
	public void setName(String value) {
		name = value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String value) {
		description = value;
	}

	@Override
	public int compareTo(NamedObject o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public String toString() {
		return getName();
	}
}
