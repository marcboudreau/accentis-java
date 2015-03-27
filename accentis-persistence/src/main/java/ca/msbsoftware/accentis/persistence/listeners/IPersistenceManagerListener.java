package ca.msbsoftware.accentis.persistence.listeners;

import ca.msbsoftware.accentis.persistence.PersistenceManager;

public interface IPersistenceManagerListener {

	void persistenceManagerChanged(PersistenceManager value);
}
