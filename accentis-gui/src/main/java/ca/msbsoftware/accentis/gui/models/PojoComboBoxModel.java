package ca.msbsoftware.accentis.gui.models;

import javax.swing.ComboBoxModel;

import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
import ca.msbsoftware.accentis.persistence.pojos.NamedObject;

@SuppressWarnings("serial")
public class PojoComboBoxModel<T extends NamedObject> extends PojoListModel<T> implements ComboBoxModel<T> {

	public PojoComboBoxModel(String queryName, Class<T> klass) {
		super(queryName, klass);
	}
	
	public PojoComboBoxModel(String queryName, Class<T> klass, boolean includeNull) {
		super(queryName, klass, includeNull);
	}

	private Object selected;
	
	@Override
	public void setSelectedItem(Object anItem) {
		selected = anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@Override
	protected IPojoListener createPojoListener() {
		final IPojoListener superListener = super.createPojoListener();
		
		return new IPojoListener() {
			
			@Override
			public void pojoSaved(BaseObject object) {
				superListener.pojoSaved(object);
			}
			
			@Override
			public void pojoDeleted(BaseObject object) {
				superListener.pojoDeleted(object);
				
				if (object.equals(selected))
					selected = null;
			}
			
			@Override
			public void pojoRefreshed(BaseObject object) {
				superListener.pojoRefreshed(object);
			}
			
			@Override
			public void pojoCreated(BaseObject object) {
				superListener.pojoCreated(object);
			}
			
			@Override
			public boolean listensToClass(Class<? extends BaseObject> klass) {
				return superListener.listensToClass(klass);
			}
		};
	}
}
