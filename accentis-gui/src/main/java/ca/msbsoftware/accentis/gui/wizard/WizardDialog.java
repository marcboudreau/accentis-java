package ca.msbsoftware.accentis.gui.wizard;

import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;

@SuppressWarnings("serial")
public class WizardDialog extends JDialog {

	private JLabel headingLabel;

	private JLabel descriptionLabel;

	private JLabel errorLabel;

	private JPanel pageContainer;

	private AbstractWizard currentWizard;

	private Map<String, Action> actions;

	private ActionListener actionListener;

	public WizardDialog(Window owner) {
		super(owner, ModalityType.APPLICATION_MODAL);
	}

	@Override
	protected void dialogInit() {
		super.dialogInit();

		setContentPane(createContentPane());
		setTitle("Wizard -TEMP"); //$NON-NLS-1$
		
		setSize(500, 500);
	}
	
	public void setWizard(AbstractWizard wizard) {
		currentWizard = wizard;
	}

	private JComponent createContentPane() {
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(createHeaderPanel(), BorderLayout.NORTH);
		panel.add(getPageContainer(), BorderLayout.CENTER);
		panel.add(createButtonPanel(), BorderLayout.SOUTH);

		return panel;
	}

	private JComponent createHeaderPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setBackground(Color.WHITE);

		panel.add(getHeadingLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		panel.add(getDescriptionLabel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		panel.add(getErrorLabel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		return panel;
	}

	protected JLabel getHeadingLabel() {
		if (null == headingLabel)
			createHeadingLabel();

		return headingLabel;
	}

	private void createHeadingLabel() {
		headingLabel = new JLabel();
		headingLabel.setFont(headingLabel.getFont().deriveFont(Font.BOLD, 16f));
	}

	public void setHeading(String value) {
		getHeadingLabel().setText(value);
	}

	protected JLabel getDescriptionLabel() {
		if (null == descriptionLabel)
			createDescriptionLabel();

		return descriptionLabel;
	}

	private void createDescriptionLabel() {
		descriptionLabel = new JLabel();
		descriptionLabel.setFont(descriptionLabel.getFont().deriveFont(Font.PLAIN));
	}

	public void setDescription(String value) {
		getDescriptionLabel().setText(value);
	}

	protected JLabel getErrorLabel() {
		if (null == errorLabel)
			createErrorLabel();

		return errorLabel;
	}

	private void createErrorLabel() {
		errorLabel = new JLabel(" "); //$NON-NLS-1$
	}

	public void setErrorMessage(String value) {
		if (null == value)
			value = " "; //$NON-NLS-1$
		
		getErrorLabel().setText(value);
	}
	
	protected JComponent getPageContainer() {
		if (null == pageContainer)
			createPageContainer();

		return pageContainer;
	}

	private void createPageContainer() {
		pageContainer = new JPanel(new BorderLayout());
		pageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	private JComponent createButtonPanel() {
		JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		{
			JPanel innerPanel = new JPanel(new GridLayout(1, 4, 5, 0));

			innerPanel.add(new JButton(createAction("back", false))); //$NON-NLS-1$
			innerPanel.add(new JButton(createAction("next", false))); //$NON-NLS-1$
			innerPanel.add(new JButton(createAction("cancel", true))); //$NON-NLS-1$
			innerPanel.add(new JButton(createAction("finish", false))); //$NON-NLS-1$

			outerPanel.add(innerPanel);
		}

		return outerPanel;
	}

	protected Action getAction(String command) {
		Action action = getActions().get(command);
		if (null == action)
			action = createAction(command, true);

		return action;
	}

	private Action createAction(String command, boolean enabled) {
		Action action = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button." + command), command), getActionListener()); //$NON-NLS-1$
		action.setEnabled(enabled);
		getActions().put(command, action);

		return action;
	}

	protected Map<String, Action> getActions() {
		if (null == actions)
			actions = new HashMap<String, Action>();

		return actions;
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
				assert null != command : "null action command encountered in WizardDialog action listener."; //$NON-NLS-1$

				if (command.equals("back")) //$NON-NLS-1$
					doDialogBack();
				else if (command.equals("next")) //$NON-NLS-1$
					doDialogNext();
				else if (command.equals("cancel")) //$NON-NLS-1$
					doDialogCancel();
				else if (command.equals("finish")) //$NON-NLS-1$
					doDialogFinish();
			}
		};
	}

	private void doDialogBack() {
		currentWizard.doWizardBack();
		updateButtons();
	}

	private void doDialogNext() {
		currentWizard.doWizardNext();
		updateButtons();
	}

	private void doDialogCancel() {
		setWizardPage(null);
		currentWizard = null;
		setVisible(false);
	}

	private void doDialogFinish() {
		currentWizard.doWizardFinish();
		setVisible(false);
	}

	public void updateButtons() {
		getAction("back").setEnabled(canDialogBack()); //$NON-NLS-1$
		getAction("next").setEnabled(canDialogNext()); //$NON-NLS-1$
		getAction("finish").setEnabled(canDialogFinish()); //$NON-NLS-1$
	}

	private boolean canDialogBack() {
		if (null == currentWizard)
			return false;

		return currentWizard.canWizardBack();
	}

	private boolean canDialogNext() {
		if (null == currentWizard)
			return false;

		return currentWizard.canWizardNext();
	}

	private boolean canDialogFinish() {
		if (null == currentWizard)
			return false;

		return currentWizard.canWizardFinish();
	}

	void setWizardPage(AbstractWizardPage page) {
		getPageContainer().removeAll();
		
		if (null != page)
			getPageContainer().add(page.getPageComponent(), BorderLayout.CENTER);
		
		getPageContainer().validate();
	}
}
