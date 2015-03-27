package ca.msbsoftware.accentis.gui.dialogs;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.NORTHWEST;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.io.File;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.database.DatabaseWrongPasswordException;
import ca.msbsoftware.accentis.persistence.database.FileDatabase;

public class OpenDatabaseDialog {

	private JFileChooser fileChooser;

	private PasswordDialog passwordDialog;

	private Window parent;

	public OpenDatabaseDialog(Window owner) {
		parent = owner;
	}

	public void show() {
		JFileChooser fileChooser = getFileChooser();

		if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(parent))
			open(fileChooser.getSelectedFile());
	}

	private JFileChooser getFileChooser() {
		if (null == fileChooser)
			createFileChooser();

		return fileChooser;
	}

	private void createFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle(Resources.getInstance().getString("mainframe.opendialog.title")); //$NON-NLS-1$
		fileChooser.setCurrentDirectory(new File(Preferences.userNodeForPackage(GUIApplication.class).get("Database.Location", System.getProperty("user.home")))); //$NON-NLS-1$
	}

	private PasswordDialog getPasswordDialog() {
		if (null == passwordDialog)
			createPasswordDialog();

		return passwordDialog;
	}

	private void createPasswordDialog() {
		passwordDialog = new PasswordDialog(parent);
	}

	@SuppressWarnings("serial")
	public class PasswordDialog extends MSBDialog {

		private JPasswordField passwordField;

		private char[] password;

		public PasswordDialog(Window owner) {
			super(owner, Resources.getInstance().getString("opendatabasedialog.passworddialog.title"), ModalityType.APPLICATION_MODAL); //$NON-NLS-1$

			setHeading(Resources.getInstance().getString("opendatabasedialog.passworddialog.heading.caption")); //$NON-NLS-1$
			setDescription(Resources.getInstance().getString("opendatabasedialog.passworddialog.description.caption")); //$NON-NLS-1$

			setSize(350, 200);
			updateButtons();
		}

		protected JComponent getContentComponent() {
			JPanel contentPane = new JPanel(new GridBagLayout());
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			contentPane.add(new JLabel(Resources.getInstance().getString("opendatabasedialog.password.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, //$NON-NLS-1$
					NORTHWEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));
			contentPane.add(getPasswordField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, NORTH, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

			return contentPane;
		}

		protected JPasswordField getPasswordField() {
			if (null == passwordField)
				createPasswordField();

			return passwordField;
		}

		private void createPasswordField() {
			passwordField = new JPasswordField(16);
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
					updateButtons();
				}
			});
		}

		protected void doOK() {
			password = Arrays.copyOf(getPasswordField().getPassword(), getPasswordField().getPassword().length);

			super.doOK();
		}

		public char[] getPassword() {
			return password;
		}

		@Override
		protected boolean canFinish() {
			return 0 < getPasswordField().getPassword().length;
		}

		@Override
		public void setVisible(boolean value) {
			if (value)
				password = null;

			super.setVisible(value);
		}
	}

	public void open(File location) {
		char[] password = null;
		PasswordDialog dialog = getPasswordDialog();
		dialog.setVisible(true);
		password = dialog.getPassword();

		if (null == password)
			return;

		parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			GUIApplication.getInstance().setPersistenceManager(new PersistenceManager(new FileDatabase(location), password));
		} catch (DatabaseWrongPasswordException ex) {
				JOptionPane.showMessageDialog(parent, Resources.getInstance().getString("opendatabasedialog.wrongpassword.message"), Resources.getInstance() //$NON-NLS-1$
						.getString("opendatabasedialog.wrongpassword.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
		}
		parent.setCursor(Cursor.getDefaultCursor());
	}
}