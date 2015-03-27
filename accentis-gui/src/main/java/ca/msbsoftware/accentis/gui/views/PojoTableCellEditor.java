package ca.msbsoftware.accentis.gui.views;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.NamedObject;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;

@SuppressWarnings("serial")
public class PojoTableCellEditor<T extends NamedObject> extends DefaultCellEditor {

	@SuppressWarnings("unchecked")
	public PojoTableCellEditor(String queryName, Class<T> klass, boolean includeNull) {
		super(new JComboBox<T>(new PojoComboBoxModel<T>(queryName, klass, includeNull)));
		
		((PojoComboBoxModel<T>) ((JComboBox<T>) getComponent()).getModel()).reload(PersistenceManager.EMPTY_PARAMETER_MAP);
	}
}
