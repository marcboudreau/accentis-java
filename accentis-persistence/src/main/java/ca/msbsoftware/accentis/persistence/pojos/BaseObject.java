package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseObject {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (super.equals(o))
			return true;
		else if (null != o && o.getClass() == getClass())
			return ((BaseObject) o).getId() == getId();
		
		return false;
	}
}
