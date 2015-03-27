package ca.msbsoftware.accentis.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ca.msbsoftware.accentis.gui.Resources;

@SuppressWarnings("serial")
public class PreferencesDialog extends JDialog {

	public PreferencesDialog() {
	}

	@Override
	protected void dialogInit() {
		super.dialogInit();
		
		setContentPane(createContentPane());
		setTitle(Resources.getInstance().getString("preferencesdialog.title")); //$NON-NLS-1$
	}

	private JComponent createContentPane() {
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(createButtonPanel(), BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JComponent createButtonPanel() {
		JPanel outerPanel = new JPanel(new BorderLayout());
		{
			JPanel innerPanel = new JPanel(new GridLayout(1,3,5,0));
			
			outerPanel.add(innerPanel, BorderLayout.CENTER);
		}
		
		return outerPanel;
	}
}