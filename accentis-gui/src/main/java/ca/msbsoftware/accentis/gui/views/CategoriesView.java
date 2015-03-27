package ca.msbsoftware.accentis.gui.views;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import ca.msbsoftware.accentis.persistence.pojos.Category;

import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.managedialog.AbstractManageDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManageCategoryDialogPage;
import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;
import ca.msbsoftware.accentis.gui.models.CategoryTreeCellRenderer;

public class CategoriesView extends BasicTreeView<Category> {

	public CategoriesView(ManagePojoDialog dialog) {
		super("categories", dialog); //$NON-NLS-1$
	}

	@Override
	public String getActionResourceKey() {
		return "action.view.categories"; //$NON-NLS-1$
	}
		
	@Override
	protected TreeModel createTreeModel() {
		return new CategoryTreeModel();
	}
	
	@Override
	public AbstractManageDialogPage<Category> createManageDialogPage() {
		return new ManageCategoryDialogPage(getManagePojoDialog());
	}

	@Override
	protected String getViewTitleCaption() {
		return Resources.getInstance().getString("categoriesview.title.caption"); //$NON-NLS-1$
	}

	@Override
	protected void additionalTreeCustomizations(JTree tree) {
		tree.setCellRenderer(new CategoryTreeCellRenderer());
	}
}
