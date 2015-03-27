package ca.msbsoftware.accentis.gui.managedialog;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.persistence.pojos.Institution;

import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;

public class ManageInstitutionDialogPage extends AbstractManageDialogPage<Institution> {

	private JComponent component;

	private JTextField nameField;

	private JTextArea addressArea;

	private JTextField contactNameField;

	private JTextField phoneNumberField;

	private JTextField emailField;

	private JTextField websiteField;

	private JTextArea descriptionArea;

	public ManageInstitutionDialogPage(ManagePojoDialog dialog) {
		super(dialog);
	}

	@Override
	JComponent getComponent() {
		if (null == component)
			createComponent();

		return component;
	}

	private void createComponent() {
		component = new JPanel(new GridBagLayout());

		component.add(new JLabel(Resources.getInstance().getString("manageinstitutiondialog.name.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		component.add(getNameField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageinstitutiondialog.address.caption")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(new JScrollPane(getAddressArea()), new GridBagConstraints(0, 2, 2, 1, 0.0, 0.25, CENTER, BOTH, new Insets(5, 20, 0, 0), 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageinstitutiondialog.contactname.caption")), new GridBagConstraints(0, 3, 1, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getContactNameField(),
				new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageinstitutiondialog.phonenumber.caption")), new GridBagConstraints(0, 4, 1, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getPhoneNumberField(),
				new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageinstitutiondialog.email.caption")), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getEmailField(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageinstitutiondialog.website.caption")), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getWebsiteField(), new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.description.caption")), new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(new JScrollPane(getDescriptionArea()), new GridBagConstraints(0, 8, 2, 1, 0.0, 0.75, CENTER, BOTH, new Insets(5, 20, 0, 0), 0, 0));
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

	protected JTextArea getAddressArea() {
		if (null == addressArea)
			createAddressArea();

		return addressArea;
	}

	private void createAddressArea() {
		addressArea = new JTextArea(4, 40);
	}

	protected JTextField getContactNameField() {
		if (null == contactNameField)
			createContactNameField();

		return contactNameField;
	}

	private void createContactNameField() {
		contactNameField = new JTextField(20);
	}

	protected JTextField getPhoneNumberField() {
		if (null == phoneNumberField)
			createPhoneNumberField();

		return phoneNumberField;
	}

	private void createPhoneNumberField() {
		phoneNumberField = new JTextField(20);
	}

	protected JTextField getEmailField() {
		if (null == emailField)
			createEmailField();

		return emailField;
	}

	private void createEmailField() {
		emailField = new JTextField(20);
	}

	protected JTextField getWebsiteField() {
		if (null == websiteField)
			createWebsiteField();

		return websiteField;
	}

	private void createWebsiteField() {
		websiteField = new JTextField(20);
	}

	protected JTextArea getDescriptionArea() {
		if (null == descriptionArea)
			createDescriptionArea();

		return descriptionArea;
	}

	private void createDescriptionArea() {
		descriptionArea = new JTextArea(4, 40);
	}

	@Override
	protected void doFinish() {
		boolean existing = true;
		Institution institution = getCurrentPojo();

		if (null == institution) {
			institution = new Institution();
			existing = false;
		}

		institution.setName(getNameField().getText());
		institution.setMailingAddress(getAddressArea().getText());
		institution.setContactName(getContactNameField().getText());
		institution.setPhoneNumber(getPhoneNumberField().getText());
		institution.setEmailAddress(getEmailField().getText());
		institution.setWebsite(getWebsiteField().getText());
		institution.setDescription(getDescriptionArea().getText());

		if (existing)
			getPersistenceManager().save(institution);
		else
			getPersistenceManager().create(institution);
	}

	@Override
	boolean canFinish() {
		return !getNameField().getText().isEmpty();
	}

	@Override
	protected void clearFields() {
		getNameField().setText(null);
		getAddressArea().setText(null);
		getContactNameField().setText(null);
		getPhoneNumberField().setText(null);
		getEmailField().setText(null);
		getWebsiteField().setText(null);
		getDescriptionArea().setText(null);
	}

	@Override
	protected void updateFields() {
		getNameField().setText(getCurrentPojo().getName());
		getAddressArea().setText(getCurrentPojo().getMailingAddress());
		getContactNameField().setText(getCurrentPojo().getContactName());
		getPhoneNumberField().setText(getCurrentPojo().getPhoneNumber());
		getEmailField().setText(getCurrentPojo().getEmailAddress());
		getWebsiteField().setText(getCurrentPojo().getWebsite());
		getDescriptionArea().setText(getCurrentPojo().getDescription());
	}

	@Override
	String getTitleParameter() {
		return Resources.getInstance().getString("managepojodialog.institution.titleparameter"); //$NON-NLS-1$
	}
}
