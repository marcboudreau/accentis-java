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

import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
import ca.msbsoftware.accentis.persistence.pojos.DownloadedTransaction;

import ca.msbsoftware.accentis.gui.ImportTransactionDialog;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;

@SuppressWarnings("serial")
public class TransactionsDownloadedTab extends JComponent implements IPojoListener {

	private JTable transactionTable;

	private Map<String, Action> actions = new HashMap<String, Action>();

	private ActionListener actionListener;

	private ImportTransactionDialog dialog;

	public TransactionsDownloadedTab() {
		createTab();
	}

	private void createTab() {
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setLayout(new GridBagLayout());

		add(new JLabel(Resources.getInstance().getString("transactionsview.tab.downloaded.heading.caption")), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, //$NON-NLS-1$
						NONE, InsetsConstants.ZERO_INSETS, 0, 0));

		add(new JScrollPane(getTransactionTable()), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5, CENTER, BOTH, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		add(createButtonPanel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, EAST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
	}

	protected JTable getTransactionTable() {
		if (null == transactionTable)
			createTransactionTable();

		return transactionTable;
	}

	private void createTransactionTable() {
		transactionTable = new JTable(new DownloadedTransactionTableModel());
		transactionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				getAction("button.import").setEnabled(1 == transactionTable.getSelectedRowCount()); //$NON-NLS-1$
				getAction("button.discard").setEnabled(1 <= transactionTable.getSelectedRowCount()); //$NON-NLS-1$
			}
		});
	}

	private JComponent createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 3, 5, 0));

		panel.add(createButton(getAction("button.import"), false)); //$NON-NLS-1$
		panel.add(createButton(getAction("button.discard"), false)); //$NON-NLS-1$

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

				if (command.equals("button.import")) //$NON-NLS-1$
					doImport();
				else if (command.equals("button.discard")) //$NON-NLS-1$
					doDiscard();
			}
		};
	}

	public void doImport() {
		getImportTransactionDialog().importTransaction(getSelectedDownloadedTransaction());
	}

	public void doDiscard() {
		removeSelectedPojos();
	}

	protected ImportTransactionDialog getImportTransactionDialog() {
		if (null == dialog)
			createImportTransactionDialog();

		return dialog;
	}

	private void createImportTransactionDialog() {
		dialog = new ImportTransactionDialog();
	}

	protected DownloadedTransaction getSelectedDownloadedTransaction() {
		return ((DownloadedTransactionTableModel) getTransactionTable().getModel()).getPojoAt(getTransactionTable().getSelectedRow());
	}

	protected void removeSelectedPojos() {
		((DownloadedTransactionTableModel) getTransactionTable().getModel()).removePojos(getTransactionTable().getSelectedRows());
	}

	@Override
	public void pojoCreated(BaseObject object) {
	}

	@Override
	public void pojoDeleted(BaseObject object) {
	}

	@Override
	public void pojoSaved(BaseObject object) {
	}

	@Override
	public void pojoRefreshed(BaseObject object) {
//		DownloadedTransaction downloadTransaction = (DownloadedTransaction) object;
//		BankAccountIdMapping mapping = downloadTransaction.getBankAccountId();
//		
//		
	}
	
	@Override
	public boolean listensToClass(Class<? extends BaseObject> klass) {
		return DownloadedTransaction.class.isInstance(klass);
	}
}
