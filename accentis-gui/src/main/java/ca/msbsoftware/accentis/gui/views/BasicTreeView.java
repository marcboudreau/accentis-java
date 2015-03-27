package ca.msbsoftware.accentis.gui.views;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ca.msbsoftware.accentis.persistence.pojos.NamedObject;

import ca.msbsoftware.accentis.gui.managedialog.ManagePojoDialog;
import ca.msbsoftware.accentis.gui.models.PojoTreeModel;

public abstract class BasicTreeView<T extends NamedObject> extends BasicView<T> {

	private JTree tree;
	
	protected BasicTreeView(String id, ManagePojoDialog dialog) {
		super(id, dialog);
	}

	@Override
	protected JComponent getPojoListComponent() {
		return getTree();
	}

	protected JTree getTree() {
		if (null == tree)
			createTree();
		
		return tree;
	}
	
	private void createTree() {
		tree = new JTree(createTreeModel());
		tree.getSelectionModel().addTreeSelectionListener(getTreeSelectionListener());
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		additionalTreeCustomizations(tree);
	}
	
	class BasicTreeListSelectionListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			getAction("button.modify").setEnabled(1 == tree.getSelectionCount()); //$NON-NLS-1$
			getAction("button.remove").setEnabled(1 <= tree.getSelectionCount()); //$NON-NLS-1$
		}
	}
	
	protected TreeSelectionListener getTreeSelectionListener() {
		return new BasicTreeListSelectionListener();
	}
	
	protected abstract TreeModel createTreeModel();
	
	@SuppressWarnings("unchecked")
	@Override
	protected T getSelectedPojo() {
		return (T) getTree().getSelectionPath().getLastPathComponent();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void removeSelectedPojos() {
		TreePath[] paths = getTree().getSelectionPaths();
		((PojoTreeModel<T>) getTree().getModel()).removePojos(paths);
	}
	
	protected void additionalTreeCustomizations(JTree tree) {
	}
}
