package ca.msbsoftware.accentis.gui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.listeners.PojoListenerManager;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
import ca.msbsoftware.accentis.persistence.pojos.NamedObject;
import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.gui.Resources;

@SuppressWarnings("serial")
public abstract class BasicTableModel<T extends NamedObject> extends AbstractTableModel implements IPersistenceManagerListener {
	
	private List<T> list = new ArrayList<T>();
	
	protected BasicTableModel() {
		GUIApplication.getInstance().addPersistenceManagerListener(this);
		PojoListenerManager.getInstance().addPojoListener(getPojoListener());
	}
	
	@Override
	public int getRowCount() {
		return list.size();
	}

	protected abstract String[] getColumnNames();
	
	@Override
	public int getColumnCount() {
		return getColumnNames().length;
	}
	
	@Override
	public String getColumnName(int column) {
		return Resources.getInstance().getString(getResourcePrefix() + "table." + getColumnNames()[column] + "column.name");  //$NON-NLS-1$//$NON-NLS-2$
	}

	protected abstract String getResourcePrefix();
	
	@Override
	public Object getValueAt(int row, int column) {
		T object = list.get(row);
		
		return getValue(object, column);
	}
	
	public T getPojoAt(int row) {
		return list.get(row);
	}
	
	protected abstract Object getValue(T object, int column);

	void removePojos(int[] rows) {
		Arrays.sort(rows);

		for (int i = rows.length - 1; i >= 0; --i)
			GUIApplication.getInstance().getPersistenceManager().delete(list.get(rows[i]));
	}
	
	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		list.clear();
		
		if (null != persistenceManager)
			list.addAll(persistenceManager.<T> get(getQueryName(), getPojoClass(), PersistenceManager.EMPTY_PARAMETER_MAP));
		
		fireTableDataChanged();
	}

	protected abstract String getQueryName();
	
	protected abstract Class<T> getPojoClass();

	private IPojoListener getPojoListener() {
		return new IPojoListener() {
			@Override
			public boolean listensToClass(Class<? extends BaseObject> klass) {
				return getPojoClass().isAssignableFrom(klass);
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public void pojoCreated(BaseObject object) {
				T castedObject = (T) object;
				int index = Collections.binarySearch(list, castedObject);
				if (0 <= index)
					return;

				index = -index - 1;
				list.add(index, castedObject);
				fireTableRowsInserted(index, index);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void pojoDeleted(BaseObject object) {
				T castedObject = (T) object;
				int index = Collections.binarySearch(list, castedObject);
				if (0 > index)
					return;

				list.remove(index);
				fireTableRowsDeleted(index, index);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void pojoSaved(BaseObject object) {
				T castedObject = (T) object;
				int preSortIndex = list.indexOf(castedObject);
				Collections.sort(list);
				int postSortIndex = Collections.binarySearch(list, castedObject);

				if (preSortIndex != postSortIndex) {
					fireTableRowsDeleted(preSortIndex, preSortIndex);

					if (postSortIndex > preSortIndex)
						postSortIndex--; // Account for the smaller row index having been removed.

					fireTableRowsInserted(postSortIndex, postSortIndex);
				} else
					fireTableRowsUpdated(preSortIndex, preSortIndex);
			}

			@Override
			public void pojoRefreshed(BaseObject object) {
			}
		};
	}
}
