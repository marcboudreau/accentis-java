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

import ca.msbsoftware.accentis.persistence.pojos.Payee;

import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;

public class ManagePayeeDialogPage extends AbstractManageDialogPage<Payee> {

	private JComponent component;

	private JTextField nameField;

	private JTextArea addressArea;

	private JTextField contactField;

	private JTextField phoneNumberField;

	private JTextField emailField;

	private JTextField websiteField;

	private JTextArea descriptionArea;

	public ManagePayeeDialogPage(ManagePojoDialog dialog) {
		super(dialog);
	}

	JComponent getComponent() {
		if (null == component)
			createComponent();

		return component;
	}

	private void createComponent() {
		component = new JPanel(new GridBagLayout());

		component.add(new JLabel(Resources.getInstance().getString("managepayeedialog.name.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, NONE, //$NON-NLS-1$
				InsetsConstants.ZERO_INSETS, 0, 0));
		component.add(getNameField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("managepayeedialog.address.caption")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(new JScrollPane(getAddressArea()), new GridBagConstraints(0, 2, 2, 1, 0.0, 0.25, CENTER, BOTH, new Insets(5, 20, 0, 0), 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("managepayeedialog.phonenumber.caption")), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getPhoneNumberField(),
				new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("managepayeedialog.contact.caption")), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getContactField(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("managepayeedialog.email.caption")), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getEmailField(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("managepayeedialog.website.caption")), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
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

	protected JTextField getPhoneNumberField() {
		if (null == phoneNumberField)
			createPhoneNumberField();

		return phoneNumberField;
	}

	private void createPhoneNumberField() {
		phoneNumberField = new JTextField(20);
	}

	protected JTextField getContactField() {
		if (null == contactField)
			createContactField();

		return contactField;
	}

	private void createContactField() {
		contactField = new JTextField(20);

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
		Payee payee = getCurrentPojo();

		if (null == payee) {
			payee = new Payee();
			existing = false;
		}

		payee.setName(getNameField().getText());
		payee.setMailingAddress(getAddressArea().getText());
		payee.setPhoneNumber(getPhoneNumberField().getText());
		payee.setContactName(getContactField().getText());
		payee.setEmailAddress(getEmailField().getText());
		payee.setWebsite(getWebsiteField().getText());
		payee.setDescription(getDescriptionArea().getText());

		if (existing)
			getPersistenceManager().save(payee);
		else
			getPersistenceManager().create(payee);
	}

	@Override
	boolean canFinish() {
		return !getNameField().getText().isEmpty();
	}

	@Override
	protected void updateFields() {
		getNameField().setText(getCurrentPojo().getName());
		getAddressArea().setText(getCurrentPojo().getMailingAddress());
		getPhoneNumberField().setText(getCurrentPojo().getPhoneNumber());
		getContactField().setText(getCurrentPojo().getContactName());
		getEmailField().setText(getCurrentPojo().getEmailAddress());
		getWebsiteField().setText(getCurrentPojo().getWebsite());
		getDescriptionArea().setText(getCurrentPojo().getDescription());
	}

	@Override
	protected void clearFields() {
		getNameField().setText(null);
		getAddressArea().setText(null);
		getContactField().setText(null);
		getPhoneNumberField().setText(null);
		getEmailField().setText(null);
		getWebsiteField().setText(null);
		getDescriptionArea().setText(null);
	}

	@Override
	String getTitleParameter() {
		return Resources.getInstance().getString("managepojodialog.payee.titleparameter"); //$NON-NLS-1$
	}
}
