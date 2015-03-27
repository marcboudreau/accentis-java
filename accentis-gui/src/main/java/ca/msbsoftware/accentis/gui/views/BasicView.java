package ca.msbsoftware.accentis.gui.views;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ca.msbsoftware.accentis.persistence.pojos.NamedObject;

import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.managedialog.AbstractManageDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;

public abstract class BasicView<T extends NamedObject> extends AbstractView {

	private ActionListener actionListener;

	private Map<String, Action> actions = new HashMap<String, Action>();

	protected ManagePojoDialog managePojoDialog;

	protected AbstractManageDialogPage<T> dialogPage;

	protected BasicView(String id, ManagePojoDialog dialog) {
		super(id);
		
		managePojoDialog = dialog;
	}

	protected JComponent getViewComponent() {
		JComponent component = new JPanel();
	
		component.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		component.setLayout(new GridBagLayout());
	
		component.add(new JLabel(getViewTitleCaption()), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, NONE,
				InsetsConstants.ZERO_INSETS, 0, 0));
	
		component.add(new JScrollPane(getPojoListComponent()), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, CENTER, BOTH, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
	
		component.add(createButtonPanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, EAST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
	
		addAdditionalContent(component);
		
		return component;
	}
	
	protected abstract String getViewTitleCaption();

	protected abstract JComponent getPojoListComponent();
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 3, 5, 0));
	
		panel.add(createButton(getAction("button.create"), true)); //$NON-NLS-1$
		panel.add(createButton(getAction("button.modify"), false)); //$NON-NLS-1$
		panel.add(createButton(getAction("button.remove"), false)); //$NON-NLS-1$
	
		return panel;
	}

	private JButton createButton(Action action, boolean enabled) {
		action.setEnabled(enabled);
	
		return new JButton(action);
	}

	public Action getAction(String actionName) {
		Action action = actions.get(actionName);
		if (null == action)
			action = createAction(actionName);
	
		return action;
	}

	private Action createAction(String actionName) {
		Action action = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action." + actionName), actionName), getActionListener()); //$NON-NLS-1$
		actions.put(actionName, action);
	
		return action;
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
				assert null != command : "null action command encountered in BasicView action listener."; //$NON-NLS-1$
	
				if (command.equals("button.create")) //$NON-NLS-1$
					doCreate();
				else if (command.equals("button.modify")) //$NON-NLS-1$
					doModify();
				else if (command.equals("button.remove")) //$NON-NLS-1$
					doRemove();
			}
		};
	}

	public void doCreate() {
		getManageDialogPage().createPojo();
	}
	
	public void doModify() {
		getManageDialogPage().editPojo(getSelectedPojo());
	}
	
	public void doRemove() {
		removeSelectedPojos();
	}

	protected abstract T getSelectedPojo();
	
	protected abstract void removeSelectedPojos();
	
	protected AbstractManageDialogPage<T> getManageDialogPage() {
		if (null == dialogPage)
			dialogPage = createManageDialogPage();
	
		return dialogPage;
	}
	
	protected abstract AbstractManageDialogPage<T> createManageDialogPage();
	
	protected ManagePojoDialog getManagePojoDialog() {
		return managePojoDialog;
	}
	
	protected void addAdditionalContent(JComponent component) {
	}
}
