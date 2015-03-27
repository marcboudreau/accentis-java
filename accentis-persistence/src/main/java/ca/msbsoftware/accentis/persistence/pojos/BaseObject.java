package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import ca.msbsoftware.accentis.persistence.listeners.PojoListenerManager;

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
	
	@PostPersist
	public void objectCreated() {
		PojoListenerManager.getInstance().objectCreated(this);
	}
	
	@PostRemove
	public void objectDeleted() {
		PojoListenerManager.getInstance().objectDeleted(this);
	}
	
	@PostUpdate
	public void objectSaved() {
		PojoListenerManager.getInstance().objectSaved(this);
	}
	
	@PostLoad
	public void objectRefreshed() {
		PojoListenerManager.getInstance().objectRefreshed(this);
	}
}
