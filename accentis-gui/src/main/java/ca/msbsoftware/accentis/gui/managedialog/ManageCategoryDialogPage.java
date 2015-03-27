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

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.CategoryType;
import ca.msbsoftware.accentis.persistence.pojos.Category;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.models.ParentCategoryComboBoxModel;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;

public class ManageCategoryDialogPage extends AbstractManageDialogPage<Category> {

	private JComponent component;

	private JTextField nameField;

	private JRadioButton categoryRadioButton;

	private JLabel typeLabel;

	private JComboBox<CategoryType> typeComboBox;

	private JRadioButton subcategoryRadioButton;

	private JLabel parentLabel;

	private JComboBox<Category> parentComboBox;

	private JTextArea descriptionArea;

	public ManageCategoryDialogPage(ManagePojoDialog dialog) {
		super(dialog);
	}

	JComponent getComponent() {
		if (null == component)
			createComponent();

		return component;
	}

	private void createComponent() {
		component = new JPanel(new GridBagLayout());

		component.add(new JLabel(Resources.getInstance().getString("managecategorydialog.name.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		component.add(getNameField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		component.add(getCategoryRadioButton(), new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		component.add(getTypeLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getTypeComboBox(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(getSubcategoryRadioButton(), new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		component.add(getParentLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getParentComboBox(),
				new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageaccountdialog.description.caption")), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(new JScrollPane(getDescriptionArea()), new GridBagConstraints(0, 6, 2, 1, 0.0, 1.0, CENTER, BOTH, new Insets(5, 20, 0, 0), 0, 0));
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

	protected JRadioButton getCategoryRadioButton() {
		if (null == categoryRadioButton)
			createRadioButtons();

		return categoryRadioButton;
	}

	protected JRadioButton getSubcategoryRadioButton() {
		if (null == subcategoryRadioButton)
			createRadioButtons();

		return subcategoryRadioButton;
	}

	private void createRadioButtons() {
		ButtonGroup group = new ButtonGroup();

		createCategoryRadioButton(group);
		createSubcategoryRadioButton(group);
	}

	private void createCategoryRadioButton(ButtonGroup group) {
		categoryRadioButton = new JRadioButton(Resources.getInstance().getString("managecategorydialog.categoryradiobutton.caption")); //$NON-NLS-1$
		categoryRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateEnabledComponents(categoryRadioButton.isSelected());
			}
		});
		categoryRadioButton.setSelected(true);

		group.add(categoryRadioButton);
	}

	private void createSubcategoryRadioButton(ButtonGroup group) {
		subcategoryRadioButton = new JRadioButton(Resources.getInstance().getString("managecategorydialog.subcategoryradiobutton.caption")); //$NON-NLS-1$
		subcategoryRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateEnabledComponents(!subcategoryRadioButton.isSelected());
			}
		});
		subcategoryRadioButton.setSelected(false);

		group.add(subcategoryRadioButton);
	}

	private void updateEnabledComponents(boolean categorySelected) {
		getTypeLabel().setEnabled(categorySelected);
		getTypeComboBox().setEnabled(categorySelected);
		getParentLabel().setEnabled(!categorySelected);
		getParentComboBox().setEnabled(!categorySelected);
	}

	protected JLabel getTypeLabel() {
		if (null == typeLabel)
			createTypeLabel();

		return typeLabel;
	}

	private void createTypeLabel() {
		typeLabel = new JLabel(Resources.getInstance().getString("managecategorydialog.type.caption")); //$NON-NLS-1$
	}

	protected JComboBox<CategoryType> getTypeComboBox() {
		if (null == typeComboBox)
			createTypeComboBox();

		return typeComboBox;
	}

	private void createTypeComboBox() {
		CategoryType[] values = new CategoryType[CategoryType.values().length + 1];
		System.arraycopy(CategoryType.values(), 0, values, 1, CategoryType.values().length);

		typeComboBox = new JComboBox<CategoryType>(values);
	}

	protected JLabel getParentLabel() {
		if (null == parentLabel)
			createParentLabel();

		return parentLabel;
	}

	private void createParentLabel() {
		parentLabel = new JLabel(Resources.getInstance().getString("managecategorydialog.parent.caption")); //$NON-NLS-1$
		parentLabel.setEnabled(false);
	}

	protected JComboBox<Category> getParentComboBox() {
		if (null == parentComboBox)
			createParentComboBox();

		return parentComboBox;
	}

	private void createParentComboBox() {
		PojoComboBoxModel<Category> model = new ParentCategoryComboBoxModel(Category.GET_ALL_TOP_LEVEL_CATEGORIES_QUERY);
		parentComboBox = new JComboBox<Category>(model);
		model.reload(PersistenceManager.EMPTY_PARAMETER_MAP);
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
		Category category = getCurrentPojo();

		if (null == category) {
			category = new Category();
			existing = false;
		}

		category.setName(getNameField().getText());
		category.setDescription(getDescriptionArea().getText());
		if (getCategoryRadioButton().isSelected())
			category.setType((CategoryType) getTypeComboBox().getSelectedItem());
		else
			category.setParentCategory((Category) getParentComboBox().getSelectedItem());
		
		if (existing)
			getPersistenceManager().save(category);
		else
			getPersistenceManager().create(category);
	}

	@Override
	boolean canFinish() {
		return !getNameField().getText().isEmpty();
	}

	@Override
	protected void updateFields() {
		getNameField().setText(getCurrentPojo().getName());
		if (null != getCurrentPojo().getParentCategory()) {
			getSubcategoryRadioButton().setSelected(true);
			getParentComboBox().setSelectedItem(getCurrentPojo().getParentCategory());
		} else {
			getCategoryRadioButton().setSelected(true);
			getTypeComboBox().setSelectedItem(getCurrentPojo().getType());
		}
		getDescriptionArea().setText(getCurrentPojo().getDescription());
	}

	@Override
	protected void clearFields() {
		getNameField().setText(null);
		getTypeComboBox().setSelectedItem(null);
		getParentComboBox().setSelectedItem(null);
		getCategoryRadioButton().setSelected(true);
		getDescriptionArea().setText(null);
	}

	@Override
	String getTitleParameter() {
		return Resources.getInstance().getString("managepojodialog.category.titleparameter"); //$NON-NLS-1$
	}
}
