package ca.msbsoftware.accentis.gui.managedialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.swing.ActionResourceDefinition;
import ca.msbsoftware.accentis.gui.swing.MSBAction;

@SuppressWarnings("serial")
public class ManagePojoDialog extends JDialog {

	private Action okAction;
	
	private ActionListener actionListener;
	
	private JComponent contentArea;
	
	private AbstractManageDialogPage<?> page;
	
	public ManagePojoDialog(Frame parent) {
		super(parent, Resources.getInstance().getString("managepojodialog.title"), ModalityType.APPLICATION_MODAL); //$NON-NLS-1$
	}

	@Override
	protected void dialogInit() {
		super.dialogInit();
		
		setContentPane(createContentPane());
		setSize(400, 600);
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				
				clearPage();
			}
		});
	}
	
	private JComponent createContentPane() {
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		contentPane.add(getContentArea(), BorderLayout.CENTER);
		contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
		
		return contentPane;
	}
	
	private JComponent getContentArea() {
		if (null == contentArea)
			createContentArea();
		
		return contentArea;
	}
	
	private void createContentArea() {
		contentArea = new JPanel(new BorderLayout());
	}
	
	private JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(1,2,5,0));
		okAction = new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button.ok"), "ok"), getActionListener()); //$NON-NLS-1$ //$NON-NLS-2$
		buttonPanel.add(new JButton(okAction));
		buttonPanel.add(new JButton(new MSBAction(new ActionResourceDefinition(Resources.getInstance().getString("action.button.cancel"), "cancel"), getActionListener()))); //$NON-NLS-1$ //$NON-NLS-2$
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(buttonPanel);
		
		return box;
	}
	
	void updateButtons() {
		okAction.setEnabled(page.canFinish());
	}
	
	private ActionListener getActionListener() {
		if (null == actionListener)
			actionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String command = e.getActionCommand();
					assert null != command : "null action command encountered in ManagePojoDialog action listener."; //$NON-NLS-1$
					
					if (command.equals("ok")) //$NON-NLS-1$
						doFinish();
					else if (command.equals("cancel")) //$NON-NLS-1$
						doCancel();
				}
			};
			
		return actionListener;
	}
	
	private void doFinish() {
		if (null != page) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			page.doFinish();
			setCursor(Cursor.getDefaultCursor());
		}
		
		setVisible(false);
	}
	
	private void doCancel() {
		clearPage();
		setVisible(false);
	}

	private void clearPage() {
		if (null != page) {
			getContentArea().remove(page.getComponent());
			page = null;
		}
	}
	
	public void setPage(AbstractManageDialogPage<?> page) {
		assert !isVisible() : "ManagePojoDialog.setPage() was called when the dialog was visible."; //$NON-NLS-1$
		
		clearPage();
		
		if (null != page) {
			this.page = page;
			getContentArea().add(page.getComponent(), BorderLayout.CENTER);
		}
	}

	@Override
	public void setVisible(boolean b) {
		adjustTitle();
		
		super.setVisible(b);
	}

	private void adjustTitle() {
		String titleParameter = (null == page ? null : page.getTitleParameter());
		String title;
		if (null == titleParameter)
			title = Resources.getInstance().getString("managepojodialog.title"); //$NON-NLS-1$
		else 
			title = MessageFormat.format(Resources.getInstance().getString("managepojodialog.title.parameterized"), titleParameter); //$NON-NLS-1$
		
		setTitle(title);
	}
}
