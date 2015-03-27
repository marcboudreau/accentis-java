package ca.msbsoftware.accentis.gui.views;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import ca.msbsoftware.accentis.gui.Resources;

public class TransactionsView extends AbstractView {

	public TransactionsView() {
		super("transactions"); //$NON-NLS-1$
	}

	@Override
	public String getActionResourceKey() {
		return "action.view.transactions"; //$NON-NLS-1$
	}

	@Override
	protected JComponent getViewComponent() {
		JTabbedPane component = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		component.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		component.addTab(Resources.getInstance().getString("transactionsview.tab.register.title"), new TransactionsRegisterTab()); //$NON-NLS-1$
		component.addTab(Resources.getInstance().getString("transactionsview.tab.downloaded.title"), new TransactionsDownloadedTab()); //$NON-NLS-1$
		component.addTab(Resources.getInstance().getString("transactionsview.tab.scheduled.title"), new TransactionsScheduledTab()); //$NON-NLS-1$
		
		return component;
	}
}
