package ca.msbsoftware.accentis.gui.managedialog;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.persistence.pojos.ScheduledTransaction;

import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;

public class ManageScheduledTransactionDialogPage extends AbstractManageDialogPage<ScheduledTransaction> {

	private JComponent component;

	private JTextField nameField;

	private JTextArea descriptionArea;

	private JCheckBox enterInRegisterCheckBox;

	private JFormattedTextField advanceDaysField;

	private ScheduleEditor scheduleEditor;

	private TransactionDetailEditor transactionDetailEditor;

	public ManageScheduledTransactionDialogPage(ManagePojoDialog dialog) {
		super(dialog);
	}

	JComponent getComponent() {
		if (null == component)
			createComponent();

		return component;
	}

	private void createComponent() {
		component = new JPanel(new GridBagLayout());

		component.add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.name.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		component.add(getNameField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.description.caption")), new GridBagConstraints(0, 1, 1, 1, //$NON-NLS-1$
				0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(new JScrollPane(getDescriptionArea()), new GridBagConstraints(0, 2, 2, 1, 0.0, 0.25, CENTER, BOTH, new Insets(5, 20, 0, 0), 0, 0));

		component.add(getEnterInRegisterCheckBox(), new GridBagConstraints(0, 3,
				2, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.advanceddays.caption")), new GridBagConstraints(0, 4, 1, //$NON-NLS-1$
				1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getAdvancedDaysField(), new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0,
				0));

		component.add(getScheduleEditor(), new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, CENTER, BOTH, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		
		component.add(getTransactionDetailEditor(), new GridBagConstraints(0, 6, 2, 1, 1.0, 1.0, CENTER, BOTH, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
	}

	protected JTextField getNameField() {
		if (null == nameField)
			createNameField();

		return nameField;
	}

	private void createNameField() {
		nameField = new JTextField(20);
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				handleEvent();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				handleEvent();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				handleEvent();
			}

			private void handleEvent() {
				getManagePojoDialog().updateButtons();
			}
		});
	}

	protected JTextArea getDescriptionArea() {
		if (null == descriptionArea)
			createDescriptionArea();

		return descriptionArea;
	}

	private void createDescriptionArea() {
		descriptionArea = new JTextArea(4, 40);
	}

	protected JCheckBox getEnterInRegisterCheckBox() {
		if (null == enterInRegisterCheckBox)
			createEnterInRegisterCheckBox();
		
		return enterInRegisterCheckBox;
	}
	
	private void createEnterInRegisterCheckBox() {
		enterInRegisterCheckBox = new JCheckBox(Resources.getInstance().getString("managescheduledtransactiondialog.enterinregister.caption")); //$NON-NLS-1$
		enterInRegisterCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getAdvancedDaysField().setEnabled(getEnterInRegisterCheckBox().isSelected());
			}
		});
	}
	
	protected JFormattedTextField getAdvancedDaysField() {
		if (null == advanceDaysField)
			createAdvancedDaysField();
		
		return advanceDaysField;
	}
	
	private void createAdvancedDaysField() {
		advanceDaysField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		advanceDaysField.setEnabled(false);
	}
	
	protected ScheduleEditor getScheduleEditor() {
		if (null == scheduleEditor)
			createScheduleEditor();
		
		return scheduleEditor;
	}
	
	private void createScheduleEditor() {
		scheduleEditor = new ScheduleEditor();
	}
	
	protected TransactionDetailEditor getTransactionDetailEditor() {
		if (null == transactionDetailEditor)
			createTransactionDetailEditor();
		
		return transactionDetailEditor;
	}
	
	private void createTransactionDetailEditor() {
		transactionDetailEditor = new TransactionDetailEditor();
	}
	
	@Override
	protected void doFinish() {
		boolean existing = true;
		ScheduledTransaction transaction = getCurrentPojo();

		if (null == transaction) {
			transaction = new ScheduledTransaction();
			existing = false;
		}

		transaction.setName(getNameField().getText());
		transaction.setDescription(getDescriptionArea().getText());

		if (getEnterInRegisterCheckBox().isSelected())
			transaction.setEnterInRegisterAdvancedDays(new Integer(getAdvancedDaysField().getText()));
		else
			transaction.setEnterInRegisterAdvancedDays(null);
		
		getScheduleEditor().saveSchedule(transaction);
		getTransactionDetailEditor().saveTransactionDetails(transaction);

		if (existing)
			getPersistenceManager().save(transaction);
		else
			getPersistenceManager().create(transaction);
	}

	@Override
	boolean canFinish() {
		return !getNameField().getText().isEmpty();
	}

	@Override
	protected void updateFields() {
		getNameField().setText(getCurrentPojo().getName());
		getDescriptionArea().setText(getCurrentPojo().getDescription());
		Integer value = getCurrentPojo().getEnterInRegisterAdvancedDays();
		if (null == value) {
			getEnterInRegisterCheckBox().setSelected(false);
			getAdvancedDaysField().setText(null);
			getAdvancedDaysField().setEnabled(false);
		} else {
			getEnterInRegisterCheckBox().setSelected(true);
			getAdvancedDaysField().setValue(value);
			getAdvancedDaysField().setEnabled(true);
		}
		getScheduleEditor().updateFields(getCurrentPojo().getSchedule());
		getTransactionDetailEditor().updateFields(getCurrentPojo());
	}

	@Override
	protected void clearFields() {
		getNameField().setText(null);
		getDescriptionArea().setText(null);
		getEnterInRegisterCheckBox().setSelected(false);
		getAdvancedDaysField().setText(null);
		getScheduleEditor().clearFields();
		getTransactionDetailEditor().clearFields();
	}

	@Override
	String getTitleParameter() {
		return Resources.getInstance().getString("managepojodialog.scheduledtransaction.titleparameter"); //$NON-NLS-1$
	}
}
