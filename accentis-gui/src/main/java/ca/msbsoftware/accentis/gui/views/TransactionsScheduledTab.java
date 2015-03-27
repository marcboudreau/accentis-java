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
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.msbsoftware.accentis.persistence.pojos.ScheduledTransaction;

import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;
import ca.msbsoftware.accentis.gui.managedialog.ManageScheduledTransactionDialogPage;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;

@SuppressWarnings("serial")
public class TransactionsScheduledTab extends JComponent {

	private JTable transactionTable;
	
	private Map<String, Action> actions = new HashMap<String, Action>();

	private ActionListener actionListener;

	private ManagePojoDialog dialog;

	private ManageScheduledTransactionDialogPage dialogPage;
	
	public TransactionsScheduledTab() {
		createTab();
	}

	private void createTab() {
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setLayout(new GridBagLayout());

		add(new JLabel(Resources.getInstance().getString("transactionsview.tab.scheduled.heading.caption")), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.ZERO_INSETS, 0, 0));

		add(new JScrollPane(getTransactionTable()), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, CENTER, BOTH, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		add(createButtonPanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, EAST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
	}

	protected JTable getTransactionTable() {
		if (null == transactionTable)
			createTransactionTable();

		return transactionTable;
	}

	private void createTransactionTable() {
		transactionTable = new JTable(new ScheduledTransactionTableModel());
		transactionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				getAction("button.modify").setEnabled(1 == transactionTable.getSelectedRowCount()); //$NON-NLS-1$
				getAction("button.remove").setEnabled(1 <= transactionTable.getSelectedRowCount()); //$NON-NLS-1$
			}
		});
	}

	private JComponent createButtonPanel() {
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

	protected ManageScheduledTransactionDialogPage getManageDialogPage() {
		if (null == dialogPage)
			createManageDialogPage();
	
		return dialogPage;
	}
	
	protected void createManageDialogPage() {
		dialogPage = new ManageScheduledTransactionDialogPage(getManagePojoDialog());
	}

	protected ManagePojoDialog getManagePojoDialog() {
		if (null == dialog)
			createManagePojoDialog();
		
		return dialog;
	}
	
	private void createManagePojoDialog() {
		dialog = new ManagePojoDialog(GUIApplication.getInstance().getFrame());
		new ManageScheduledTransactionDialogPage(dialog);
	}
	
	protected ScheduledTransaction getSelectedPojo() {
		return ((ScheduledTransactionTableModel) getTransactionTable().getModel()).getPojoAt(getTransactionTable().getSelectedRow());
	}
	
	protected void removeSelectedPojos() {
		((ScheduledTransactionTableModel) getTransactionTable().getModel()).removePojos(getTransactionTable().getSelectedRows());
	}
}
