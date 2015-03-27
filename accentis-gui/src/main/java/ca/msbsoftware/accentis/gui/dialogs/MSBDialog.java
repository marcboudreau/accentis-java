package ca.msbsoftware.accentis.gui.dialogs;

import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTHEAST;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.GridBagConstraints.WEST;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
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
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ca.msbsoftware.accentis.gui.ImageLoader;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;

@SuppressWarnings("serial")
public abstract class MSBDialog extends JDialog {

	private static final Icon NO_ICON = ImageLoader.getIcon("noicon"); //$NON-NLS-1$

	private static final Icon WARNING_ICON = ImageLoader.getIcon("warn"); //$NON-NLS-1$

	private static final Icon ERROR_ICON = ImageLoader.getIcon("error"); //$NON-NLS-1$

	private JLabel headingLabel;

	private JLabel descriptionIconLabel;

	private JTextArea descriptionTextArea;

	private JLabel headingIconLabel;

	private String description;

	private Map<String, Action> actions;

	private ActionListener actionListener;
	
	public MSBDialog(Dialog owner, boolean modal) {
		super(owner, modal);
	}

	public MSBDialog(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
	}

	public MSBDialog(Window owner, String title, ModalityType modalityType) {
		super(owner, title, modalityType);
	}

	public MSBDialog(Window owner) {
		super(owner);
	}
	
	protected static JLabel createLabelForComponent(String caption, char mnemonic, JComponent component) {
		JLabel label = new JLabel(caption);
		label.setDisplayedMnemonic(mnemonic);
		label.setLabelFor(component);
		
		return label;
	}

	@Override
	protected void dialogInit() {
		super.dialogInit();

		setContentPane(createContentPane());
		setSize(500, 600);
	}

	private JComponent createContentPane() {
		JPanel contentPane = new JPanel(new BorderLayout());

		contentPane.add(createHeadingPane(), BorderLayout.NORTH);
		contentPane.add(getContentComponent(), BorderLayout.CENTER);
		contentPane.add(createButtonPane(), BorderLayout.SOUTH);

		return contentPane;
	}

	private JComponent createHeadingPane() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(getHeadingLabel(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, WEST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		panel.add(getDescriptionIconLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, NORTHWEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		panel.add(getDescriptionTextArea(), new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, NORTHWEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		panel.add(getHeadingIconLabel(), new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, NORTHEAST, NONE, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		return panel;
	}

	protected JLabel getHeadingLabel() {
		if (null == headingLabel)
			createHeadingLabel();

		return headingLabel;
	}

	private void createHeadingLabel() {
		headingLabel = new JLabel();
		headingLabel.setFont(headingLabel.getFont().deriveFont(16f));
	}

	protected JLabel getDescriptionIconLabel() {
		if (null == descriptionIconLabel)
			createDescriptionIconLabel();

		return descriptionIconLabel;
	}

	private void createDescriptionIconLabel() {
		descriptionIconLabel = new JLabel(NO_ICON);
	}

	protected JTextArea getDescriptionTextArea() {
		if (null == descriptionTextArea)
			createDescriptionTextArea();

		return descriptionTextArea;
	}

	private void createDescriptionTextArea() {
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setEditable(false);
	}

	protected JLabel getHeadingIconLabel() {
		if (null == headingIconLabel)
			createHeadingIconLabel();

		return headingIconLabel;
	}

	private void createHeadingIconLabel() {
		headingIconLabel = new JLabel();
	}

	protected abstract JComponent getContentComponent();

	private JComponent createButtonPane() {
		JPanel outerPanel = new JPanel(new GridBagLayout());
		outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel innerPanel = new JPanel(new GridLayout(1, 2, 5, 0));
		innerPanel.add(new JButton(getAction("cancel"))); //$NON-NLS-1$
		innerPanel.add(createDefaultButton(getAction("ok"))); //$NON-NLS-1$

		outerPanel.add(innerPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, EAST, NONE, InsetsConstants.ZERO_INSETS, 0, 0));

		return outerPanel;
	}
	
	private JButton createDefaultButton(Action action) {
		JButton button = new JButton(action);
		getRootPane().setDefaultButton(button);
		
		return button;
	}

	protected Map<String, Action> getActions() {
		if (null == actions)
			createActionMap();

		return actions;
	}

	private void createActionMap() {
		actions = new HashMap<String, Action>();
	}

	protected Action getAction(String command) {
		Action action = getActions().get(command);
		if (null == action)
			action = createAction(command);

		return action;
	}

	private Action createAction(String command) {
		Action action = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button." + command), command), getActionListener()); //$NON-NLS-1$
		getActions().put(command, action);

		return action;
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
				assert null != command : "null action command encountered in MSBDialog action listener"; //$NON-NLS-1$

				if (command.equals("ok")) //$NON-NLS-1$
					doOK();
				else if (command.equals("cancel")) //$NON-NLS-1$
					doCancel();
			}
		};
	}

	protected void doOK() {
		setVisible(false);
	}

	protected void doCancel() {
		setVisible(false);
	}

	public void setHeading(String value) {
		getHeadingLabel().setText(value);
	}

	public void setHeadingIcon(Icon icon) {
		getHeadingIconLabel().setIcon(icon);
	}

	public void setDescription(String value) {
		getDescriptionIconLabel().setIcon(NO_ICON);
		getDescriptionTextArea().setText(value);
		description = value;
	}

	public void setWarning(String value) {
		getDescriptionIconLabel().setIcon(WARNING_ICON);
		getDescriptionTextArea().setText(value);
	}

	public void clearWarningOrError() {
		getDescriptionIconLabel().setIcon(NO_ICON);
		getDescriptionTextArea().setText(description);
	}

	public void setError(String value) {
		getDescriptionIconLabel().setIcon(ERROR_ICON);
		getDescriptionTextArea().setText(value);
	}
	
	protected void updateButtons() {
		getAction("ok").setEnabled(canFinish()); //$NON-NLS-1$
	}
	
	protected abstract boolean canFinish();
}
