package ca.msbsoftware.accentis.gui;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.prefs.Preferences;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
//import ca.msbsoftware.accentis.importer.TransactionImporter;
import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;
import ca.msbsoftware.accentis.gui.views.AccountsView;
import ca.msbsoftware.accentis.gui.views.CategoriesView;
import ca.msbsoftware.accentis.gui.views.IndividualsView;
import ca.msbsoftware.accentis.gui.views.InstitutionsView;
import ca.msbsoftware.accentis.gui.views.PayeesView;
import ca.msbsoftware.accentis.gui.views.TransactionsView;

public class GUIApplication {

	private static final GUIApplication instance = new GUIApplication();

	public final Preferences preferences = Preferences.userNodeForPackage(GUIApplication.class);

	private MainFrame frame;

	private List<String> arguments = new ArrayList<String>();

	private PersistenceManager persistenceManager;

	private Set<IPersistenceManagerListener> persistenceManagerListeners = new HashSet<IPersistenceManagerListener>();
	
	private ManagePojoDialog managePojoDialog;

	private Timer backgroundTransactionEntryTimer;
	
	public static GUIApplication getInstance() {
		return instance;
	}

	private GUIApplication() {
	}

	public Collection<String> getArguments() {
		return arguments;
	}

	public MainFrame getFrame() {
		return frame;
	}

	public void exit() {
		Rectangle bounds = new Rectangle();
		bounds.setLocation(frame.getLocation());
		bounds.setSize(frame.getSize());

		preferences.putInt("MainFrame.Location.X", bounds.x); //$NON-NLS-1$
		preferences.putInt("MainFrame.Location.Y", bounds.y); //$NON-NLS-1$
		preferences.putInt("MainFrame.Size.Width", bounds.width); //$NON-NLS-1$
		preferences.putInt("MainFrame.Size.Height", bounds.height); //$NON-NLS-1$
		
		backgroundTransactionEntryTimer.cancel();
	}

	public static void main(String[] args) {
		for (String arg : args)
			instance.arguments.add(arg);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				instance.frame = new MainFrame();
				instance.addViewsToFrame();
				instance.frame.setVisible(true);
				instance.frame.openRememberedDatabase();
				instance.backgroundTransactionEntryTimer = createBackgroundTransactionEntryTimer();
			}
		});
	}
	
	private static Timer createBackgroundTransactionEntryTimer() {
		Timer timer = new Timer("BackgroundTransactionEntryThread"); //$NON-NLS-1$
		timer.scheduleAtFixedRate(new EnterScheduledTransactionsInRegisterTask(), 500, 6 * 60 * 60 * 1000);
		//timer.scheduleAtFixedRate(new TransactionImporter(new BankAccountIdMappingDialog(instance.frame)), 1000, 1000 * 60);
		
		return timer;
	}

	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
	
	public void setPersistenceManager(PersistenceManager value) {
		persistenceManager = value;
		
		synchronized (persistenceManagerListeners) {
			for (IPersistenceManagerListener listener : persistenceManagerListeners)
				listener.persistenceManagerChanged(persistenceManager);
		}
	}

	public void addPersistenceManagerListener(IPersistenceManagerListener listener) {
		synchronized (persistenceManagerListeners) {
			persistenceManagerListeners.add(listener);
		}
	}
	
	public void removePersistenceManagerListener(IPersistenceManagerListener listener) {
		synchronized (persistenceManagerListeners) {
			persistenceManagerListeners.remove(listener);
		}
	}
	
	protected void addViewsToFrame() {
		frame.addView(new AccountsView(getManagePojoDialog()));
		frame.addView(new CategoriesView(getManagePojoDialog()));
		frame.addView(new IndividualsView(getManagePojoDialog()));
		frame.addView(new InstitutionsView(getManagePojoDialog()));
		frame.addView(new PayeesView(getManagePojoDialog()));
		frame.addView(new TransactionsView());
	}
	
	protected ManagePojoDialog getManagePojoDialog() {
		if (null == managePojoDialog)
			createManagePojoDialog();
			
		return managePojoDialog;
	}
	
	private void createManagePojoDialog() {
		managePojoDialog = new ManagePojoDialog(getFrame());
	}
}
