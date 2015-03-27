package ca.msbsoftware.accentis.gui.models;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import ca.msbsoftware.accentis.persistence.pojos.CategoryType;

import ca.msbsoftware.accentis.gui.ImageLoader;

@SuppressWarnings("serial")
public class CategoryTreeCellRenderer extends DefaultTreeCellRenderer {

	private Icon CATEGORYTYPE_ICON = ImageLoader.getIcon("categorytypes_s"); //$NON-NLS-1$
	
	private Icon CATEGORY_ICON = ImageLoader.getIcon("categories_s"); //$NON-NLS-1$
	
	private Icon SUBCATEGORY_ICON = ImageLoader.getIcon("subcategories_s"); //$NON-NLS-1$

	@Override
	public Icon getDefaultOpenIcon() {
		return CATEGORY_ICON;
	}

	@Override
	public Icon getDefaultClosedIcon() {
		return CATEGORY_ICON;
	}

	@Override
	public Icon getDefaultLeafIcon() {
		return SUBCATEGORY_ICON;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if (value instanceof CategoryType) {
			setOpenIcon(CATEGORYTYPE_ICON);
			setClosedIcon(CATEGORYTYPE_ICON);
		} else {
			setOpenIcon(CATEGORY_ICON);
			setClosedIcon(CATEGORY_ICON);
		}
		setLeafIcon(getDefaultLeafIcon());
		
		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	}
	
	
}
