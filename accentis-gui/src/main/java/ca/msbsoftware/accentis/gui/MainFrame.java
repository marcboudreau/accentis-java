package ca.msbsoftware.accentis.gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.gui.dialogs.OpenDatabaseDialog;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;
import ca.msbsoftware.accentis.gui.views.AbstractView;
import ca.msbsoftware.accentis.gui.views.ViewContainer;
import ca.msbsoftware.accentis.gui.wizard.WizardDialog;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private ViewContainer viewContainer;

	private JMenu viewMenu;
	
	private ActionListener actionListener;

	private Map<String, Action> actions;

	public MainFrame() {
		super(Resources.getInstance().getString("mainframe.title")); //$NON-NLS-1$
	}
	
	@Override
	protected void frameInit() {
		super.frameInit();

		createMenuBar();
		createWindowListener();
		setContentPane(getViewContainer());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setRememberedLocationAndSize();
		
		createDataManagerFactoryListener();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(createFileMenu());
		menuBar.add(getViewMenu());

		setJMenuBar(menuBar);
	}

	private JMenu createFileMenu() {
		JMenu menu = createMenu("action.menu.file"); //$NON-NLS-1$

		menu.add(createMenuItem("action.menu.file.new", true)); //$NON-NLS-1$
		menu.add(createMenuItem("action.menu.file.open", true)); //$NON-NLS-1$
		menu.add(createMenuItem("action.menu.file.close", false)); //$NON-NLS-1$
		menu.addSeparator();
		menu.add(createMenuItem("action.menu.file.password", false)); //$NON-NLS-1$
		menu.addSeparator();
		menu.add(createMenuItem("action.menu.file.exit", true)); //$NON-NLS-1$

		return menu;
	}

	private JMenu getViewMenu() {
		if (null == viewMenu)
			createViewMenu();
		
		return viewMenu;
	}
	
	private void createViewMenu() {
		viewMenu = createMenu("action.menu.view"); //$NON-NLS-1$
	}
	
	private static JMenu createMenu(String resourceKey) {
		JMenu menu = new JMenu();
		new ActionResourceDefinition(Resources.getInstance().getString(resourceKey), null).configureJMenu(menu);

		return menu;
	}
	
	private JMenuItem createMenuItem(String command, boolean enabled) {
		Action action = createAction(command, getActionListener());
		action.setEnabled(enabled);
		JMenuItem menuItem = createMenuItem(action);

		return menuItem;
	}
	
	private JMenuItem createMenuItem(Action action) {
		return new JMenuItem(action);
	}

	private Action createAction(String actionCommand, ActionListener listener) {
		ActionResourceDefinition definition = new ActionResourceDefinition(Resources.getInstance().getString(actionCommand), actionCommand);
		Action action = new MSBAction(definition, listener);
		getActions().put((String) action.getValue(Action.ACTION_COMMAND_KEY), action);
		
		return action;
	}

	private Map<String, Action> getActions() {
		if (null == actions)
			actions = new HashMap<String, Action>();
		
		return actions;
	}

	private ActionListener getActionListener() {
		if (null == actionListener)
			createActionListener();

		return actionListener;
	}

	private void createActionListener() {
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				assert null != command : "null action command encountered in MainFrame."; //$NON-NLS-1$

				if (command.equals("action.menu.file.new")) //$NON-NLS-1$
					doNew();
				else if (command.equals("action.menu.file.open")) //$NON-NLS-1$
					doOpen();
				else if (command.equals("action.menu.file.exit")) //$NON-NLS-1$
					doExit();
				else if (command.equals("action.menu.file.password")) //$NON-NLS-1$
					doPasswordChange();
			}
		};
	}
	
	private void createWindowListener() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				GUIApplication.getInstance().exit();
			}
		});
	}
	
	private void setRememberedLocationAndSize() {
		Rectangle bounds = getRememberedBounds();
		setLocation(bounds.getLocation());

		if (0 == bounds.getSize().height * bounds.getSize().width)
			pack();
		else
			setSize(bounds.getSize());
	}

	private static Rectangle getRememberedBounds() {
		Rectangle bounds = new Rectangle();
		bounds.x = GUIApplication.getInstance().preferences.getInt("MainFrame.Location.X", 0); //$NON-NLS-1$
		bounds.y = GUIApplication.getInstance().preferences.getInt("MainFrame.Location.Y", 0); //$NON-NLS-1$
		bounds.width = GUIApplication.getInstance().preferences.getInt("MainFrame.Size.Width", 0); //$NON-NLS-1$
		bounds.height = GUIApplication.getInstance().preferences.getInt("MainFrame.Size.Height", 0); //$NON-NLS-1$

		return bounds;
	}

	protected ViewContainer getViewContainer() {
		if (null == viewContainer)
			createViewContainer();
		
		return viewContainer;
	}
	
	private void createViewContainer() {
		viewContainer = new ViewContainer();
		viewContainer.setViewMenu(getViewMenu());
		viewContainer.setEnabled(false);
		viewContainer.setVisible(false);
	}
	
	public void addView(AbstractView view) {
		getViewContainer().addView(view);
	}
	
	
	public Action getAction(String command) {
		return getActions().get(command);
	}
	
	private void doNew() {
		WizardDialog dialog = new WizardDialog(this);
		new NewDatabaseWizard(dialog);
		dialog.setVisible(true);
		
	}
	
	private void doOpen() {
		OpenDatabaseDialog openDatabaseDialog = new OpenDatabaseDialog(this);
		openDatabaseDialog.show();
	}
	
	private void doPasswordChange() {
		ChangeDatabasePasswordDialog dialog = new ChangeDatabasePasswordDialog(this);
		dialog.setVisible(true);
	}
	
	private void doExit() {
		setVisible(false);
		GUIApplication.getInstance().exit();
		dispose();
	}
	
	private void createDataManagerFactoryListener() {
		GUIApplication.getInstance().addPersistenceManagerListener(new IPersistenceManagerListener() {
			@Override
			public void persistenceManagerChanged(PersistenceManager value) {
				Action action = actions.get("action.menu.file.close"); //$NON-NLS-1$
				action.setEnabled(null != value);
				
				action = actions.get("action.menu.file.password"); //$NON-NLS-1$
				action.setEnabled(null != value);
				
				getViewContainer().setVisible(null != value);
				getViewContainer().setEnabled(null != value);
			}
		});
	}
	
	public void openRememberedDatabase() {
		Preferences prefs = Preferences.userNodeForPackage(GUIApplication.class);
		if (prefs.getBoolean("Database.Open.Startup", false)) { //$NON-NLS-1$
			String location = prefs.get("Database.Location", null); //$NON-NLS-1$
			if (null != location) {
				OpenDatabaseDialog dialog = new OpenDatabaseDialog(this);
				dialog.open(new File(location));
			}
		}
	}
}
