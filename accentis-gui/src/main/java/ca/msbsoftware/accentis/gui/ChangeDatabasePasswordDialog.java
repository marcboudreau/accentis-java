package ca.msbsoftware.accentis.gui;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.GridBagConstraints.WEST;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.gui.dialogs.MSBDialog;
import ca.msbsoftware.accentis.gui.swing.PasswordStrengthLabel;
import ca.msbsoftware.accentis.persistence.database.DatabaseException;
import ca.msbsoftware.accentis.persistence.database.DatabaseWrongPasswordException;

@SuppressWarnings("serial")
public class ChangeDatabasePasswordDialog extends MSBDialog {

	private JTextArea locationTextArea;

	private JPasswordField currentPasswordField;
	
	private JPasswordField firstPasswordField;

	private JPasswordField secondPasswordField;

	private PasswordStrengthLabel passwordStrengthLabel;

	private boolean passwordValid;

	public ChangeDatabasePasswordDialog(Window owner) {
		super(owner, Resources.getInstance().getString("changedatabasepassworddialog.title"), ModalityType.APPLICATION_MODAL); //$NON-NLS-1$

		setHeading(Resources.getInstance().getString("changedatabasepassworddialog.heading.caption")); //$NON-NLS-1$
		setDescription(Resources.getInstance().getString("changedatabasepassworddialog.description.caption")); //$NON-NLS-1$
	}

	@Override
	protected JComponent getContentComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(new JLabel(Resources.getInstance().getString("changedatabasepassworddialog.locationlabel.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		panel.add(getLocationTextArea(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		panel.add(
				createLabelForComponent(Resources.getInstance().getString("changedatabasepassworddialog.oldpassword.caption"), Resources.getInstance() //$NON-NLS-1$
						.getCharacter("changedatabasepassworddialog.oldpassword.mnemonic"), getCurrentPasswordField()), new GridBagConstraints(0, 1, 1, 1, 0.0, //$NON-NLS-1$
						0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		panel.add(getCurrentPasswordField(),
				new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		panel.add(new JLabel(Resources.getInstance().getString("changedatabasepassworddialog.minimumrequirement.caption")), new GridBagConstraints(0, 2, 2, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		panel.add(
				createLabelForComponent(Resources.getInstance().getString("changedatabasepassworddialog.newpassword.caption"), //$NON-NLS-1$
						Resources.getInstance().getCharacter("changedatabasepassworddialog.newpassword.mnemonic"), getFirstPasswordField()), new GridBagConstraints(0, 3, //$NON-NLS-1$
						1, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		panel.add(getFirstPasswordField(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		panel.add(getSecondPasswordField(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		panel.add(new JLabel(Resources.getInstance().getString("changedatabasepassworddialog.passwordstrength.caption")), new GridBagConstraints(0, 5, 1, 1, 0.0, 1.0, //$NON-NLS-1$
				NORTHWEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		panel.add(getPasswordStrengthLabel(), new GridBagConstraints(1, 5, 1, 1, 0.0, 1.0, NORTHWEST, NONE, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		return panel;
	}

	protected JTextArea getLocationTextArea() {
		if (null == locationTextArea)
			createLocationTextArea();

		return locationTextArea;
	}

	private void createLocationTextArea() {
		locationTextArea = new JTextArea();
		locationTextArea.setLineWrap(true);
		locationTextArea.setWrapStyleWord(true);
		locationTextArea.setEditable(false);
		locationTextArea.setOpaque(false);
	}

	protected JPasswordField getCurrentPasswordField() {
		if (null == currentPasswordField)
			createCurrentPasswordField();
		
		return currentPasswordField;
	}
	
	private void createCurrentPasswordField() {
		currentPasswordField = new JPasswordField();
	}
	
	protected JPasswordField getFirstPasswordField() {
		if (null == firstPasswordField)
			firstPasswordField = createPasswordField();

		return firstPasswordField;
	}

	private JPasswordField createPasswordField() {
		JPasswordField passwordField = new JPasswordField();
		passwordField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				handleModification();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				handleModification();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				handleModification();
			}

			private void handleModification() {
				evaluatePasswordStrength();
			}
		});

		return passwordField;
	}

	protected JPasswordField getSecondPasswordField() {
		if (null == secondPasswordField)
			secondPasswordField = createPasswordField();

		return secondPasswordField;
	}

	protected PasswordStrengthLabel getPasswordStrengthLabel() {
		if (null == passwordStrengthLabel)
			createPasswordStrengthLabel();

		return passwordStrengthLabel;
	}

	private void createPasswordStrengthLabel() {
		passwordStrengthLabel = new PasswordStrengthLabel();
	}

	private void evaluatePasswordStrength() {
		char[] password1 = getFirstPasswordField().getPassword();
		char[] password2 = getSecondPasswordField().getPassword();

		if (!Arrays.equals(password1, password2)) {
			setError(Resources.getInstance().getString("encryptdatabasedialog.passwordsdontmatch.message")); //$NON-NLS-1$
			getPasswordStrengthLabel().setPasswordStrength(null);
			passwordValid = false;
		} else if (8 > password1.length) {
			setError(Resources.getInstance().getString("encryptdatabasedialog.passwordsdontmeetrequirement.message")); //$NON-NLS-1$
			getPasswordStrengthLabel().setPasswordStrength(null);
			passwordValid = false;
		} else {
			clearWarningOrError();
			getPasswordStrengthLabel().setPasswordStrength(PasswordStrengthLabel.analyze(password1));
			passwordValid = true;
		}

		updateButtons();
	}

	@Override
	public void setVisible(boolean b) {
		getLocationTextArea().setText(GUIApplication.getInstance().getPersistenceManager().getDatabaseLocation());

		super.setVisible(b);
	}

	@Override
	protected void doOK() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			GUIApplication.getInstance().getPersistenceManager().changePassword(getCurrentPasswordField().getPassword(), getFirstPasswordField().getPassword());
		} catch (DatabaseWrongPasswordException ex) {
			setError(Resources.getInstance().getString("changedatabasedialog.wrongcurrentpassword.message")); //$NON-NLS-1$
			getCurrentPasswordField().requestFocus();
			getCurrentPasswordField().setSelectionStart(0);
			getCurrentPasswordField().setSelectionEnd(getCurrentPasswordField().getPassword().length);
		} catch (DatabaseException ex) {
			// Log this exception.
		}
		
		setCursor(Cursor.getDefaultCursor());
		
		super.doOK();
	}

	@Override
	protected boolean canFinish() {
		return passwordValid;
	}
}
