package ca.msbsoftware.accentis.gui.views;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.RELATIVE;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Institution;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.managedialog.AbstractManageDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManageInstitutionDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;
import ca.msbsoftware.accentis.gui.models.PojoListModel;

public class InstitutionsView extends BasicTableView<Institution> {

	private JList<Account> relatedAccountList;

	public InstitutionsView(ManagePojoDialog dialog) {
		super("institutions", dialog); //$NON-NLS-1$
	}

	public String getActionResourceKey() {
		return "action.view.institutions"; //$NON-NLS-1$
	}

	protected AbstractManageDialogPage<Institution> createManageDialogPage() {
		return new ManageInstitutionDialogPage(getManagePojoDialog());
	}

	@Override
	protected void addAdditionalContent(JComponent component) {
		component.add(new JLabel(Resources.getInstance().getString("institutionsview.accountsheading.caption")), new GridBagConstraints(0, RELATIVE, 1, 1, 0.0, //$NON-NLS-1$
				0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("institutionsview.accountsdescription.caption")), new GridBagConstraints(0, RELATIVE, 1, 1, //$NON-NLS-1$
				0.0, 0.0, WEST, NONE, new Insets(5, 20, 0, 0), 0, 0));

		component.add(new JScrollPane(getRelatedAccountList()), new GridBagConstraints(0, RELATIVE, 1, 1, 0.0, 0.5, CENTER, BOTH,
				InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
	}

	@Override
	protected TableModel createTableModel() {
		return new InstitutionTableModel();
	}

	@Override
	protected String getViewTitleCaption() {
		return Resources.getInstance().getString("accountsview.title.caption"); //$NON-NLS-1$
	}

	protected JList<Account> getRelatedAccountList() {
		if (null == relatedAccountList)
			createRelatedAccountList();

		return relatedAccountList;
	}

	private void createRelatedAccountList() {
		relatedAccountList = new JList<Account>(new PojoListModel<Account>(Account.GET_ALL_ACCOUNTS_REFERENCING_INSTITUTION_QUERY, Account.class));
	}

	@Override
	protected ListSelectionListener getTableSelectionListener() {
		return new BasicTableListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				super.valueChanged(e);
				
				if (!e.getValueIsAdjusting()) {
					Map<String, Object> queryParameters = null;
					if (1 == getTable().getSelectedRowCount()) {
						Institution institution = getSelectedPojo();
						queryParameters = PersistenceManager.createQueryParameterMap("institution", institution); //$NON-NLS-1$
					} else
						
					((PojoListModel<Account>) getRelatedAccountList().getModel()).reload(queryParameters);
					
				}
			}
		};
	}
}
