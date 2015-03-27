package ca.msbsoftware.accentis.persistence.listeners;

import ca.msbsoftware.accentis.persistence.pojos.BaseObject;

public interface IPojoListener {

	void pojoCreated(BaseObject object);
	
	void pojoDeleted(BaseObject object);
	
	void pojoSaved(BaseObject object);
	
	void pojoRefreshed(BaseObject object);
	
	boolean listensToClass(Class<? extends BaseObject> klass);
}
