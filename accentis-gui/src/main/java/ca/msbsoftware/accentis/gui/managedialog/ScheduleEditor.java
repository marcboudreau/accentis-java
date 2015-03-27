package ca.msbsoftware.accentis.gui.managedialog;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

import ca.msbsoftware.accentis.persistence.ScheduleRepeatFrequency;
import ca.msbsoftware.accentis.persistence.ScheduleRepeatPeriod;
import ca.msbsoftware.accentis.persistence.pojos.Schedule;
import ca.msbsoftware.accentis.persistence.pojos.ScheduledTransaction;

import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.DateFormattedTextField;

@SuppressWarnings("serial")
public class ScheduleEditor extends JComponent {

	private DateFormattedTextField startingDateField;

	private JComboBox<Enum<ScheduleRepeatFrequency>> repeatFrequencyComboBox;

	private JComboBox<Enum<ScheduleRepeatPeriod>> repeatPeriodComboBox;

	private JRadioButton endsNeverRadioButton;

	private JRadioButton endsOnDateRadioButton;

	private DateFormattedTextField endsOnDateField;

	private JRadioButton endsOccurrencesRadioButton;

	private JFormattedTextField endsOccurrencesField;

	public ScheduleEditor() {
		createEditor();
	}

	private void createEditor() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Resources.getInstance().getString("managescheduledtransactiondialog.schedule.title"))); //$NON-NLS-1$
		setLayout(new GridBagLayout());

		add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.schedule.startingdate.caption")), new GridBagConstraints(0, 0, 1, 1, //$NON-NLS-1$
				0.0, 0.0, WEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		add(getStartingDateField(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.schedule.repeats.caption")), new GridBagConstraints(0, 1, 1, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		add(getRepeatFrequencyComboBox(), new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		add(getRepeatPeriodComboBox(), new GridBagConstraints(2, 1, 1, 1, 0.5, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		add(new JLabel(Resources.getInstance().getString("managescheduledtransactiondialog.schedule.ends.caption")), new GridBagConstraints(0, 2, 1, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		add(getEndsNeverRadioButton(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(5, 20, 0, 0), 0, 0));

		add(getEndsOnDateRadioButton(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(5, 20, 0, 0), 0, 0));
		add(getEndsOnDateField(), new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		add(getEndsOccurrencesRadioButton(), new GridBagConstraints(0, 5, 1, 1, 0.0, 1.0, NORTHWEST, NONE, new Insets(5, 20, 0, 0), 0, 0));
		add(getEndsOccurrencesField(), new GridBagConstraints(1, 5, 1, 1, 0.0, 1.0, NORTH, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
	}

	protected DateFormattedTextField getStartingDateField() {
		if (null == startingDateField)
			createStartingDateField();

		return startingDateField;
	}

	private void createStartingDateField() {
		startingDateField = new DateFormattedTextField();
	}

	protected JComboBox<Enum<ScheduleRepeatFrequency>> getRepeatFrequencyComboBox() {
		if (null == repeatFrequencyComboBox)
			createRepeatFrequencyComboBox();

		return repeatFrequencyComboBox;
	}

	private void createRepeatFrequencyComboBox() {
		repeatFrequencyComboBox = new JComboBox<Enum<ScheduleRepeatFrequency>>(ScheduleRepeatFrequency.values());
	}

	protected JComboBox<Enum<ScheduleRepeatPeriod>> getRepeatPeriodComboBox() {
		if (null == repeatPeriodComboBox)
			createRepeatPeriodComboBox();

		return repeatPeriodComboBox;
	}

	private void createRepeatPeriodComboBox() {
		repeatPeriodComboBox = new JComboBox<Enum<ScheduleRepeatPeriod>>(ScheduleRepeatPeriod.values());
	}

	protected JRadioButton getEndsNeverRadioButton() {
		if (null == endsNeverRadioButton)
			createRadioButtons();

		return endsNeverRadioButton;
	}

	private void createRadioButtons() {
		ButtonGroup group = new ButtonGroup();

		group.add(createEndsNeverRadioButton());
		group.add(createEndsOnDateRadioButton());
		group.add(createEndsOccurrencesRadioButton());
	}

	private JRadioButton createEndsNeverRadioButton() {
		endsNeverRadioButton = new JRadioButton(Resources.getInstance().getString("managescheduledtransactiondialog.schedule.endsnever.caption")); //$NON-NLS-1$
		endsNeverRadioButton.setSelected(true);
		endsNeverRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getEndsOnDateField().setEnabled(false);
				getEndsOccurrencesField().setEnabled(false);
			}
		});

		return endsNeverRadioButton;
	}

	protected JRadioButton getEndsOnDateRadioButton() {
		if (null == endsOnDateRadioButton)
			createRadioButtons();

		return endsOnDateRadioButton;
	}

	private JRadioButton createEndsOnDateRadioButton() {
		endsOnDateRadioButton = new JRadioButton(Resources.getInstance().getString("managescheduledtransactiondialog.schedule.endsondate.caption")); //$NON-NLS-1$
		endsOnDateRadioButton.setSelected(true);
		endsOnDateRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getEndsOnDateField().setEnabled(endsOnDateRadioButton.isSelected());
				getEndsOccurrencesField().setEnabled(!endsOnDateRadioButton.isSelected());
			}
		});

		return endsOnDateRadioButton;
	}

	protected DateFormattedTextField getEndsOnDateField() {
		if (null == endsOnDateField)
			createEndsOnDateField();

		return endsOnDateField;
	}

	private void createEndsOnDateField() {
		endsOnDateField = new DateFormattedTextField();
		endsOnDateField.setEnabled(false);
	}

	protected JRadioButton getEndsOccurrencesRadioButton() {
		if (null == endsOccurrencesRadioButton)
			createRadioButtons();

		return endsOccurrencesRadioButton;
	}

	private JRadioButton createEndsOccurrencesRadioButton() {
		endsOccurrencesRadioButton = new JRadioButton(Resources.getInstance().getString("managescheduledtransactiondialog.schedule.endsoccurrences.caption")); //$NON-NLS-1$
		endsOccurrencesRadioButton.setSelected(true);
		endsOccurrencesRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getEndsOccurrencesField().setEnabled(endsOccurrencesRadioButton.isSelected());
				getEndsOnDateField().setEnabled(!endsOccurrencesRadioButton.isSelected());
			}
		});

		return endsOccurrencesRadioButton;
	}

	protected JFormattedTextField getEndsOccurrencesField() {
		if (null == endsOccurrencesField)
			createEndsOccurrencesField();

		return endsOccurrencesField;
	}

	private void createEndsOccurrencesField() {
		endsOccurrencesField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		endsOccurrencesField.setEnabled(false);
	}

	void updateFields(Schedule schedule) {
		getStartingDateField().setValue(schedule.getStartingDate());
		getRepeatFrequencyComboBox().setSelectedItem(schedule.getRepeatFrequency());
		getRepeatPeriodComboBox().setSelectedItem(schedule.getRepeatPeriod());

		Date endsOnDate = schedule.getEndsOnDate();
		Integer endsOccurrence = schedule.getEndsOccurrences();

		if (null == endsOnDate && null == endsOccurrence) {
			getEndsNeverRadioButton().setSelected(true);
			getEndsOnDateField().setText(null);
			getEndsOccurrencesField().setText(null);
		} else if (null != endsOnDate) {
			getEndsOnDateRadioButton().setSelected(true);
			getEndsOnDateField().setValue(endsOnDate);
			getEndsOccurrencesField().setText(null);
		} else if (null != endsOccurrence) {
			getEndsOccurrencesRadioButton().setSelected(true);
			getEndsOnDateField().setText(null);
			getEndsOccurrencesField().setValue(endsOccurrence);
		}
	}

	void clearFields() {
		getStartingDateField().setValue(null);
		getRepeatFrequencyComboBox().setSelectedItem(null);
		getRepeatPeriodComboBox().setSelectedItem(null);
		getEndsNeverRadioButton().setSelected(true);
		getEndsOnDateField().setText(null);
		getEndsOccurrencesField().setText(null);
	}
	
	void saveSchedule(ScheduledTransaction transaction) {
		transaction.getSchedule().setStartingDate(getStartingDateField().getDateValue());
		transaction.getSchedule().setRepeatFrequency((ScheduleRepeatFrequency) getRepeatFrequencyComboBox().getSelectedItem());
		transaction.getSchedule().setRepeatPeriod((ScheduleRepeatPeriod) getRepeatPeriodComboBox().getSelectedItem());
		
		if (getEndsNeverRadioButton().isSelected()) {
			transaction.getSchedule().setEndsOccurrences(null);
			transaction.getSchedule().setEndsOnDate(null);
		} else if (getEndsOnDateRadioButton().isSelected()) {
			transaction.getSchedule().setEndsOccurrences(null);
			transaction.getSchedule().setEndsOnDate(getEndsOnDateField().getDateValue());
		} else {
			transaction.getSchedule().setEndsOccurrences(((Long) getEndsOccurrencesField().getValue()).intValue());
			transaction.getSchedule().setEndsOnDate(null);
		}
	}
}