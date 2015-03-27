package ca.msbsoftware.accentis.gui.views;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.Category;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;

public class CategoriesCellEditor  {

	private TableCellEditor categoryCellEditor;
	
	private TableCellEditor subcategoryCellEditor;
	
	private JComboBox<Category> subcategoryComboBox;
	
	public TableCellEditor getCategoryCellEditor() {
		if (null == categoryCellEditor)
			createCategoryCellEditor();
		
		return categoryCellEditor;
	}
	
	private void createCategoryCellEditor() {
		JComboBox<Category> comboBox = new JComboBox<Category>(new PojoComboBoxModel<Category>(Category.GET_ALL_TOP_LEVEL_CATEGORIES_QUERY, Category.class, true));
		categoryCellEditor = new DefaultCellEditor(comboBox);
		((PojoComboBoxModel<Category>) comboBox.getModel()).reload(PersistenceManager.EMPTY_PARAMETER_MAP);
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				getSubcategoryComboBox().removeAllItems();
				getSubcategoryComboBox().addItem(null);
				
				if (null != e.getItem())
					for (Category item : ((Category) e.getItem()).getSubCategories())
						getSubcategoryComboBox().addItem(item);
			}
		});
	}
	
	public TableCellEditor getSubcategoryCellEditor() {
		if (null == subcategoryCellEditor)
			createSubcategoryCellEditor();
		
		return subcategoryCellEditor;
	}
	
	private void createSubcategoryCellEditor() {
		subcategoryComboBox = new JComboBox<Category>();
		subcategoryCellEditor = new DefaultCellEditor(subcategoryComboBox);
	}
	
	private JComboBox<Category> getSubcategoryComboBox() {
		if (null == subcategoryComboBox)
			createSubcategoryCellEditor();
		
		return subcategoryComboBox;
	}
}
