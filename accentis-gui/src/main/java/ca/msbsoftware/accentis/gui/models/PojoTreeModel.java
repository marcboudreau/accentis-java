package ca.msbsoftware.accentis.gui.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.persistence.pojos.NamedObject;

public abstract class PojoTreeModel<T extends NamedObject> implements TreeModel, IPersistenceManagerListener {

	private String queryName;

	private Map<String, Object> queryParameters;

	private Object root = new Object();

	private Set<TreeModelListener> listeners = new HashSet<TreeModelListener>();

	public PojoTreeModel(String query, Class<T> klass) {
		queryName = query;
		GUIApplication.getInstance().addPersistenceManagerListener(this);
	}

	public void reload(Map<String, Object> parameters) {
		clear();
		queryParameters = parameters;
		PersistenceManager persistenceManager = GUIApplication.getInstance().getPersistenceManager();
		if (null != persistenceManager) {
			if (null != queryParameters)
				load(persistenceManager);
		}
	}

	protected abstract void load(PersistenceManager persistenceManager);

	protected String getQueryName() {
		return queryName;
	}

	protected Map<String, Object> getQueryParameters() {
		return queryParameters;
	}

	public void clear() {
	}

	@Override
	public Object getRoot() {
		return root;
	}
	
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	protected void fireTreeContentChanged() {
		TreeModelEvent event = new TreeModelEvent(this, new Object[] { getRoot() }, null, null);

		for (TreeModelListener listener : listeners)
			listener.treeStructureChanged(event);
	}

	protected void fireTreeNodesInserted(Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);

		for (TreeModelListener listener : listeners)
			listener.treeNodesInserted(event);
	}

	protected void fireTreeNodesRemoved(Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);

		for (TreeModelListener listener : listeners)
			listener.treeNodesRemoved(event);
	}

	@SuppressWarnings("unchecked")
	public void removePojos(TreePath[] paths) {
		List<TreePath> pathList = new ArrayList<TreePath>();
		
		for (TreePath path : paths)
			if (2 == path.getPathCount()) {
				for (Iterator<TreePath> it = pathList.iterator(); it.hasNext(); )
					if (path.isDescendant(it.next()))
						it.remove();
				
				pathList.add(path);
			} else if (2 < path.getPathCount())
				for (Iterator<TreePath> it = pathList.iterator(); it.hasNext(); )
					if (!it.next().isDescendant(path))
						pathList.add(path);
		
		for (TreePath path : pathList)
			GUIApplication.getInstance().getPersistenceManager().delete((T) path.getLastPathComponent());
	}
}
