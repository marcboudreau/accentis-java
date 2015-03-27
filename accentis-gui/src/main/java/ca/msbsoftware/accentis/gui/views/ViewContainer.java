package ca.msbsoftware.accentis.gui.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;


@SuppressWarnings("serial")
public class ViewContainer extends JComponent {

	private ViewToolBar toolBar;
	
	private JPanel viewPanel;
	
	private String currentView;
	
	private ActionListener actionListener;
	
	private Map<String, Action> viewActionMap;
	
	private JMenu viewMenu;
	
	public ViewContainer() {
		setLayout(new BorderLayout());
		
		add(getToolBar(), BorderLayout.NORTH);
		add(getViewPanel(), BorderLayout.CENTER);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				switchToView(currentView);
			}
		});
	}
	
	protected ActionListener getActionListener() {
		if (null == actionListener)
			createActionListener();
		
		return actionListener;
	}
	
	private void createActionListener() {
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				assert null != command : "null action command encountered in ViewContainer action listener."; //$NON-NLS-1$
				
				if (command.startsWith("action.view.")) { //$NON-NLS-1$
					currentView = command.substring("action.view.".length()); //$NON-NLS-1$
					switchToView(currentView);
				}
			}
		};
	}
	
	public void addView(AbstractView view) {
		String command = "action.view." + view.getViewID(); //$NON-NLS-1$
		Action action = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString(view.getActionResourceKey()), command), getActionListener());
		
		getViewActionMap().put(command, action);		
		getToolBar().addAction(action);
		getViewPanel().add(view.getViewComponent(), view.getViewID());
		getViewMenu().add(new JMenuItem(action));
		
		if (null == currentView)
			currentView = view.getViewID();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		getToolBar().setEnabled(enabled);
	}
	
	
	protected ViewToolBar getToolBar() {
		if (null == toolBar)
			createToolBar();
		
		return toolBar;
	}
	
	private void createToolBar() {
		toolBar = new ViewToolBar();
	}
	
	protected JPanel getViewPanel() {
		if (null == viewPanel)
			createViewPanel();
		
		return viewPanel;
	}
	
	private void createViewPanel() {
		viewPanel = new JPanel(new CardLayout());
	}
	
	protected void switchToView(String viewID) {
		CardLayout layout = (CardLayout) getViewPanel().getLayout();
		layout.show(getViewPanel(), viewID);
		Action action = getViewActionMap().get("action.view." + viewID); //$NON-NLS-1$
		action.putValue(Action.SELECTED_KEY, true);
	}
	
	protected Map<String, Action> getViewActionMap() {
		if (null == viewActionMap)
			createViewActionMap();
		
		return viewActionMap;
	}
	
	private void createViewActionMap() {
		viewActionMap = new HashMap<String, Action>();
	}
	
	protected JMenu getViewMenu() {
		return viewMenu;
	}
	
	public void setViewMenu(JMenu menu) {
		viewMenu = menu;
	}
}
