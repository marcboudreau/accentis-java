package ca.msbsoftware.accentis.gui.managedialog;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.Currency;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.AccountType;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Institution;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.models.CurrencyComboBoxModel;
import ca.msbsoftware.accentis.gui.models.CurrencyComboBoxRenderer;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;
import ca.msbsoftware.accentis.gui.swing.TwoDecimalTextField;

public class ManageAccountDialogPage extends AbstractManageDialogPage<Account> {

	private JComponent component;

	private JTextField nameField;

	private JTextField numberField;

	private JComboBox<AccountType> typeComboBox;

	private JComboBox<Institution> institutionComboBox;

	private JComboBox<Currency> currencyComboBox;

	private JTextArea descriptionArea;

	private TwoDecimalTextField startingBalanceField;

	private TwoDecimalTextField minimumBalanceField;

	public ManageAccountDialogPage(ManagePojoDialog dialog) {
		super(dialog);
	}

	JComponent getComponent() {
		if (null == component)
			createComponent();

		return component;
	}

	private void createComponent() {
		component = new JPanel(new GridBagLayout());

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.name.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		component.add(getNameField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.number.caption")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getNumberField(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.type.caption")), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getTypeComboBox(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.institution.caption")), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getInstitutionComboBox(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS,
				0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.currency.caption")), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getCurrencyComboBox(),
				new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.description.caption")), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(new JScrollPane(getDescriptionArea()), new GridBagConstraints(0, 6, 2, 1, 0.0, 1.0, CENTER, BOTH, new Insets(5, 20, 0, 0), 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.startingbalance.caption")), new GridBagConstraints(0, 7, 1, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getStartingBalanceField(), new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS,
				0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.minimumbalance.caption")), new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getMinimumBalanceField(), new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS,
				0, 0));
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

	protected JTextField getNumberField() {
		if (null == numberField)
			createNumberField();

		return numberField;
	}

	private void createNumberField() {
		numberField = new JTextField(20);
	}

	protected JComboBox<AccountType> getTypeComboBox() {
		if (null == typeComboBox)
			createTypeComboBox();

		return typeComboBox;
	}

	private void createTypeComboBox() {
		AccountType[] values = new AccountType[AccountType.values().length + 1];
		System.arraycopy(AccountType.values(), 0, values, 1, AccountType.values().length);

		typeComboBox = new JComboBox<AccountType>(values);

	}

	protected JComboBox<Institution> getInstitutionComboBox() {
		if (null == institutionComboBox)
			createInstitutionComboBox();
		
		return institutionComboBox;
	}
	
	private void createInstitutionComboBox() {
		PojoComboBoxModel<Institution> model = new PojoComboBoxModel<Institution>(Institution.GET_ALL_INSTITUTIONS_QUERY, Institution.class, true);
		institutionComboBox = new JComboBox<Institution>(model);
		model.reload(PersistenceManager.EMPTY_PARAMETER_MAP);
		
	}
	
	protected JComboBox<Currency> getCurrencyComboBox() {
		if (null == currencyComboBox)
			createCurrencyComboBox();

		return currencyComboBox;
	}

	private void createCurrencyComboBox() {
		currencyComboBox = new JComboBox<Currency>(new CurrencyComboBoxModel());
		currencyComboBox.setRenderer(new CurrencyComboBoxRenderer());
	}

	protected JTextArea getDescriptionArea() {
		if (null == descriptionArea)
			createDescriptionArea();

		return descriptionArea;
	}

	private void createDescriptionArea() {
		descriptionArea = new JTextArea(4, 40);
	}

	protected TwoDecimalTextField getStartingBalanceField() {
		if (null == startingBalanceField)
			createStartingBalanceField();

		return startingBalanceField;
	}

	private void createStartingBalanceField() {
		startingBalanceField = new TwoDecimalTextField();
	}

	protected TwoDecimalTextField getMinimumBalanceField() {
		if (null == minimumBalanceField)
			createMinimumBalanceField();

		return minimumBalanceField;
	}

	private void createMinimumBalanceField() {
		minimumBalanceField = new TwoDecimalTextField();
	}

	@Override
	protected void doFinish() {
		boolean existing = true;
		Account account = getCurrentPojo();
		
		if (null == account) {
			account = new Account();
			existing = false;
		}

		account.setName(getNameField().getText());
		account.setNumber(getNumberField().getText());
		account.setType((AccountType) getTypeComboBox().getSelectedItem());
		account.setInstitution((Institution) getInstitutionComboBox().getSelectedItem());
		account.setCurrency((Currency) getCurrencyComboBox().getSelectedItem());
		account.setDescription(getDescriptionArea().getText());
		account.setStartBalance(getStartingBalanceField().getBigDecimalValue());
		account.setMinimumBalance(getMinimumBalanceField().getBigDecimalValue());

		if (existing)
			getPersistenceManager().save(account);
		else
			getPersistenceManager().create(account);
	}

	@Override
	boolean canFinish() {
		return !getNameField().getText().isEmpty();
	}

	@Override
	protected void updateFields() {
		getNameField().setText(getCurrentPojo().getName());
		getNumberField().setText(getCurrentPojo().getNumber());
		getTypeComboBox().setSelectedItem(getCurrentPojo().getType());
		getInstitutionComboBox().setSelectedItem(getCurrentPojo().getInstitution());
		getCurrencyComboBox().setSelectedItem(getCurrentPojo().getCurrency());
		getDescriptionArea().setText(getCurrentPojo().getDescription());
		getStartingBalanceField().setValue(getCurrentPojo().getStartBalance());
		getMinimumBalanceField().setValue(getCurrentPojo().getMinimumBalance());
	}

	@Override
	protected void clearFields() {
		getNameField().setText(null);
		getNumberField().setText(null);
		getTypeComboBox().setSelectedItem(null);
		getInstitutionComboBox().setSelectedItem(null);
		getCurrencyComboBox().setSelectedItem(null);
		getDescriptionArea().setText(null);
		getStartingBalanceField().setValue(BigDecimal.ZERO);
		getMinimumBalanceField().setValue(BigDecimal.ZERO);
	}
	
	@Override
	String getTitleParameter() {
		return Resources.getInstance().getString("managepojodialog.account.titleparameter"); //$NON-NLS-1$
	}
}
