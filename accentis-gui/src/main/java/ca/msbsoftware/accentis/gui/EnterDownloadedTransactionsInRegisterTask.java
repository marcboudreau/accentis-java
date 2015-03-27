package ca.msbsoftware.accentis.gui;

import java.util.TimerTask;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;

public class EnterDownloadedTransactionsInRegisterTask extends TimerTask implements IPersistenceManagerListener {

	private PersistenceManager persistenceManager;
	
	@Override
	public void run() {
		if (null == persistenceManager)
			return;
		
		

	}

	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

}
