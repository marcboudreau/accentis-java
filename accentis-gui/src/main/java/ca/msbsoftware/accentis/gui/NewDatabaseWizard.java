package ca.msbsoftware.accentis.gui;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.GridBagConstraints.WEST;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;
import ca.msbsoftware.accentis.gui.swing.PasswordStrengthLabel;
import ca.msbsoftware.accentis.gui.wizard.AbstractWizard;
import ca.msbsoftware.accentis.gui.wizard.AbstractWizardPage;
import ca.msbsoftware.accentis.gui.wizard.WizardDialog;
import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.database.DatabaseWrongPasswordException;
import ca.msbsoftware.accentis.persistence.database.FileDatabase;

public class NewDatabaseWizard extends AbstractWizard {

	private String location;
	
	private char[] password;
	
	protected NewDatabaseWizard(WizardDialog dialog) {
		super(dialog);
		dialog.setWizard(this);
		dialog.setHeading(Resources.getInstance().getString("newdatabasewizard.heading")); //$NON-NLS-1$
		dialog.setDescription(Resources.getInstance().getString("newdatabasewizard.description")); //$NON-NLS-1$
	}

	@Override
	protected void createWizardPages(List<AbstractWizardPage> pages) {
		pages.add(new AbstractWizardPage() {

			private JTextField locationField;

			private Action browseAction;

			@Override
			protected JComponent getPageComponent() {
				JPanel panel = new JPanel(new GridBagLayout());

				panel.add(createLabel("newdatabasewizard.location", getLocationField()), new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, NORTHWEST, NONE, //$NON-NLS-1$
						InsetsConstants.ZERO_INSETS, 0, 0));
				panel.add(getLocationField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, NORTH, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));
				panel.add(new JButton(getBrowseAction()), new GridBagConstraints(2, 0, 1, 1, 0.0, 1.0, NORTHWEST, NONE, InsetsConstants.FIVE_ON_LEFT_INSETS, 0,
						0));
				return panel;
			}

			protected JTextField getLocationField() {
				if (null == locationField)
					createLocationField();

				return locationField;
			}

			private void createLocationField() {
				locationField = new JTextField(20);
				locationField.getDocument().addDocumentListener(new DocumentListener() {
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
						getWizardDialog().updateButtons();
					}
				});
			}

			private Action createBrowseAction() {
				browseAction = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button.browse"), "browse"), //$NON-NLS-1$ //$NON-NLS-2$
						new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setFileSelectionMode(DIRECTORIES_ONLY);
								String directory = Preferences.userNodeForPackage(GUIApplication.class)
										.get("NewDatabaseWizard.Location", null); //$NON-NLS-1$
								if (null != directory)
									fileChooser.setCurrentDirectory(new File(directory));

								if (APPROVE_OPTION == fileChooser.showOpenDialog(getWizardDialog())) {
									directory = fileChooser.getSelectedFile().getAbsolutePath();
									getLocationField().setText(directory);
									Preferences.userNodeForPackage(GUIApplication.class).put("NewDatabaseWizard.Location", directory); //$NON-NLS-1$
								}
							}
						});

				return browseAction;
			}

			protected Action getBrowseAction() {
				if (null == browseAction)
					createBrowseAction();

				return browseAction;
			}
			
			protected void recordInputs() {
				location = getLocationField().getText();
			}
		});
		pages.add(new AbstractWizardPage() {

			private JCheckBox encryptCheckBox;

			private JLabel passwordLabel;

			private JPasswordField firstPasswordField;

			private JPasswordField secondPasswordField;

			private JLabel passwordStrengthLabelLabel;

			private PasswordStrengthLabel passwordStrengthLabel;

			@Override
			protected JComponent getPageComponent() {
				JPanel panel = new JPanel(new GridBagLayout());

				panel.add(getEncryptCheckBox(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));

				panel.add(getPasswordLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(5, 20, 0, 0), 0, 0));
				panel.add(getFirstPasswordField(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL,
						InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

				panel.add(getSecondPasswordField(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL,
						InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

				panel.add(getPasswordStrengthLabelLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 1.0, NORTHWEST, NONE, new Insets(5, 20, 0, 0), 0, 0));
				panel.add(getPasswordStrengthLabel(), new GridBagConstraints(1, 3, 1, 1, 0.0, 1.0, NORTHWEST, NONE,
						InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

				return panel;
			}

			protected JCheckBox getEncryptCheckBox() {
				if (null == encryptCheckBox)
					createEncryptCheckBox();

				return encryptCheckBox;
			}

			private void createEncryptCheckBox() {
				encryptCheckBox = new JCheckBox(new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.checkbox.encrypt"), //$NON-NLS-1$
						"encrypt"), new ActionListener() { //$NON-NLS-1$
					@Override
					public void actionPerformed(ActionEvent e) {
						boolean selected = encryptCheckBox.isSelected();

						getPasswordLabel().setEnabled(selected);
						getFirstPasswordField().setEnabled(selected);
						getSecondPasswordField().setEnabled(selected);
						getPasswordStrengthLabelLabel().setEnabled(selected);
						getPasswordStrengthLabel().setEnabled(selected);
					}
				}));
				encryptCheckBox.setSelected(Preferences.userNodeForPackage(GUIApplication.class).getBoolean("NewDatabaseDialog.EncryptData", true)); //$NON-NLS-1$
			}

			protected JLabel getPasswordLabel() {
				if (null == passwordLabel)
					createPasswordLabel();

				return passwordLabel;
			}

			private void createPasswordLabel() {
				passwordLabel = createLabel("newdatabasewizard.password", getFirstPasswordField()); //$NON-NLS-1$
				passwordLabel.setEnabled(getEncryptCheckBox().isSelected());
			}

			protected JPasswordField getFirstPasswordField() {
				if (null == firstPasswordField)
					createFirstPasswordField();

				return firstPasswordField;
			}

			private void createFirstPasswordField() {
				firstPasswordField = new JPasswordField(20);
				firstPasswordField.getDocument().addDocumentListener(new DocumentListener() {
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
						evaluatePasswords();
					}
				});
				firstPasswordField.setEnabled(getEncryptCheckBox().isSelected());
			}

			protected JPasswordField getSecondPasswordField() {
				if (null == secondPasswordField)
					createSecondPasswordField();

				return secondPasswordField;
			}

			private void createSecondPasswordField() {
				secondPasswordField = new JPasswordField(20);
				secondPasswordField.getDocument().addDocumentListener(new DocumentListener() {
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
						evaluatePasswords();
					}
				});
				secondPasswordField.setEnabled(getEncryptCheckBox().isSelected());
			}

			protected JLabel getPasswordStrengthLabelLabel() {
				if (null == passwordStrengthLabelLabel)
					createPasswordStrengthLabelLabel();

				return passwordStrengthLabelLabel;
			}

			private void createPasswordStrengthLabelLabel() {
				passwordStrengthLabelLabel = new JLabel(Resources.getInstance().getString("newdatabasewizard.passwordstrength.caption")); //$NON-NLS-1$
				passwordStrengthLabelLabel.setEnabled(getEncryptCheckBox().isSelected());
			}

			protected PasswordStrengthLabel getPasswordStrengthLabel() {
				if (null == passwordStrengthLabel)
					createPasswordStrengthLabel();

				return passwordStrengthLabel;
			}

			private void createPasswordStrengthLabel() {
				passwordStrengthLabel = new PasswordStrengthLabel();
				passwordStrengthLabel.setEnabled(getEncryptCheckBox().isSelected());
			}

			private void evaluatePasswords() {
				if (Arrays.equals(getFirstPasswordField().getPassword(), getSecondPasswordField().getPassword()))
					if (0 == getFirstPasswordField().getPassword().length) {
						getPasswordStrengthLabel().setPasswordStrength(null);
						getWizardDialog().setErrorMessage(Resources.getInstance().getString("newdatabasewizard.providepasswords.message")); //$NON-NLS-1$
					} else {
						getPasswordStrengthLabel().setPasswordStrength(PasswordStrengthLabel.analyze(getFirstPasswordField().getPassword()));
						getWizardDialog().setErrorMessage(null);
					}
				else {
					getWizardDialog().setErrorMessage(Resources.getInstance().getString("newdatabasewizard.passwordsdontmatch.message")); //$NON-NLS-1$
					getPasswordStrengthLabel().setPasswordStrength(null);
				}
			}
			
			protected void recordInputs() {
				if (getEncryptCheckBox().isSelected()) { 
					char[] array = getFirstPasswordField().getPassword();
					password = Arrays.copyOf(array, array.length);
				} else
					password = null;
			}
		});

	}

	private JLabel createLabel(String resourceKeyBase, JComponent labelFor) {
		JLabel label = new JLabel(Resources.getInstance().getString(resourceKeyBase + ".caption")); //$NON-NLS-1$
		label.setDisplayedMnemonic(Resources.getInstance().getCharacter(resourceKeyBase + ".mnemonic")); //$NON-NLS-1$
		label.setLabelFor(labelFor);

		return label;
	}

	@Override
	public boolean canWizardFinish() {
		return null != location;
	}

	@Override
	protected void processInputs() {
		getWizardDialog().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		PersistenceManager persistenceManager = null;
		try {
			persistenceManager = new PersistenceManager(new FileDatabase(new File(location)), password);
			GUIApplication.getInstance().setPersistenceManager(persistenceManager);
		} catch (DatabaseWrongPasswordException ex) {
			// Log this error.
		}		
		
		getWizardDialog().setCursor(Cursor.getDefaultCursor());
	}
}
