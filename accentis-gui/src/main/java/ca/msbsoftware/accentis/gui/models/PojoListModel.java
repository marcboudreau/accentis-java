package ca.msbsoftware.accentis.gui.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;

import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.listeners.PojoListenerManager;
import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
import ca.msbsoftware.accentis.persistence.pojos.NamedObject;

@SuppressWarnings("serial")
public class PojoListModel<T extends NamedObject> extends AbstractListModel<T> implements IPersistenceManagerListener {

	private final String pojoQueryName;
	
	private Map<String, Object> pojoQueryParameters;
	
	private final Class<T> pojoClass;
	
	protected List<T> list = new ArrayList<T>();
	
	private int includeNullOffset;
	
	public PojoListModel(String queryName, Class<T> klass) {
		this(queryName, klass, false);
	}
	
	public PojoListModel(String queryName, Class<T> klass, boolean includeNull) {
		GUIApplication.getInstance().addPersistenceManagerListener(this);
		PojoListenerManager.getInstance().addPojoListener(createPojoListener());
		pojoQueryName = queryName;
		pojoClass = klass;
		includeNullOffset = includeNull ? 1 : 0;
	}

	protected Class<T> getPojoClass() {
		return pojoClass;
	}
	
	public void reload(Map<String, Object> queryParameters) {
		clear();
		pojoQueryParameters = queryParameters;
		load();
	}
	
	@Override
	public int getSize() {
		return list.size() + offset();
	}

	@Override
	public T getElementAt(int index) {
		if (1 == offset() && 0 == index)
			return null;
		
		return list.get(index - offset());
	}

	public T lookupByName(String name) {
		int index = Collections.binarySearch(list, name, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				String name1 = getName(o1);
				String name2 = getName(o2);
				
				return name1.compareTo(name2);
			}
			
			@SuppressWarnings("unchecked")
			private String getName(Object o) {
				if (o instanceof String)
					return (String) o;
				
				return ((T) o).getName();
			}
		});
		
		if (0 > index)
			return null;
		
		return list.get(index);
	}
	
	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		reload(getPojoQueryParameters());
	}

	private void clear() {
		int size = getSize();
		list.clear();
		fireIntervalRemoved(this, offset(), size);
	}

	private void load() {
		PersistenceManager persistenceManager = GUIApplication.getInstance().getPersistenceManager();
		if (null != persistenceManager && null != getPojoQueryParameters()) {
			list.addAll(persistenceManager.<T>get(pojoQueryName, getPojoClass(), getPojoQueryParameters()));
			fireIntervalAdded(this, offset(), getSize() + offset());
		}
	}
	
	protected Map<String, Object> getPojoQueryParameters() {
		return pojoQueryParameters;
	}
	
	protected IPojoListener createPojoListener() {
		return new IPojoListener() {
			@Override
			public boolean listensToClass(Class<? extends BaseObject> klass) {
				return pojoClass.isInstance(klass);
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public void pojoCreated(BaseObject object) {
				T castedObject = (T) object;
				int index = Collections.binarySearch(list, castedObject);
				if (0 <= index)
					return;
				
				index = -index - 1 + offset();
				
				list.add(index, castedObject);
				fireIntervalAdded(this, index, index);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void pojoDeleted(BaseObject object) {
				T castedObject = (T) object;
				int index = Collections.binarySearch(list, castedObject);
				if (0 > index)
					return;
				
				list.remove(index);
				
				index += offset();
				
				fireIntervalRemoved(this, index, index);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void pojoSaved(BaseObject object) {
				T castedObject = (T) object;
				int preSortIndex = list.indexOf(object);
				Collections.sort(list);
				int postSortIndex = Collections.binarySearch(list, castedObject);

				preSortIndex += offset();
				postSortIndex += offset();
				
				if (preSortIndex != postSortIndex) {
					fireIntervalRemoved(this, preSortIndex, preSortIndex);

					if (postSortIndex > preSortIndex)
						postSortIndex--; // Account for the smaller row index having been removed.

					fireIntervalAdded(this, postSortIndex, postSortIndex);
				} else
					fireContentsChanged(this, preSortIndex, preSortIndex);
			}

			@Override
			public void pojoRefreshed(BaseObject object) {
			}
		};
	}
	
	protected int offset() {
		return includeNullOffset;
	}
}
