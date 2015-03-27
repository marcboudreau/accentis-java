package ca.msbsoftware.accentis.gui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.msbsoftware.accentis.gui.models.PojoTreeModel;
import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
import ca.msbsoftware.accentis.persistence.pojos.Category;
import ca.msbsoftware.accentis.persistence.pojos.CategoryType;

public class CategoryTreeModel extends PojoTreeModel<Category> {

	private Map<CategoryType, List<Category>> categoryTree = new HashMap<CategoryType, List<Category>>();
	
	public CategoryTreeModel() {
		super(Category.GET_ALL_TOP_LEVEL_CATEGORIES_QUERY, Category.class); //$NON-NLS-1$
		
		for (CategoryType type : CategoryType.values())
			categoryTree.put(type, new ArrayList<Category>());
	}
	
	@Override
	public Object getChild(Object parent, int index) {
		if (getRoot().equals(parent))
			return CategoryType.values()[index];
		
		if (parent.getClass() == CategoryType.class)
			return categoryTree.get(parent).get(index);
		
		if (parent instanceof Category)
			return ((Category) parent).getSubCategories().get(index);
		
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if (getRoot().equals(parent))
			return CategoryType.values().length;
		
		if (parent.getClass() == CategoryType.class) {
			List<Category> list = categoryTree.get(parent);
			return list.size();
		}
		
		if (parent instanceof Category)
			return ((Category) parent).getSubCategories().size();

		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof Category)
			return ((Category) node).getSubCategories().size() == 0;
		
		return false;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (getRoot().equals(parent))
			return getIndexOfCategoryType((CategoryType) child);
		
		if (parent.getClass() == CategoryType.class)
			return categoryTree.get(parent).indexOf(child);
		
		if (parent instanceof Category)
			return ((Category) parent).getSubCategories().indexOf(child);
		
		return -1;
	}
	
	private int getIndexOfCategoryType(CategoryType type) {
		for (int i = 0; i < CategoryType.values().length; ++i)
			if (CategoryType.values()[i].equals(type))
				return i;
		
		return -1;
	}

	@Override
	protected void load(PersistenceManager persistenceManager) {
		List<Category> list = persistenceManager.get(getQueryName(), Category.class, getQueryParameters());
		
		for (Category category : list)
			addCategoryToTree(category);
		
		fireTreeContentChanged();
	}
	
	private void addCategoryToTree(Category category) {
		CategoryType type = category.getType();
		List<Category> categorySubTree = categoryTree.get(type);
		
		if (null == categorySubTree) {
			categorySubTree = new ArrayList<Category>();
			categoryTree.put(type, categorySubTree);
		}
		
		categorySubTree.add(category);
	}

	protected IPojoListener getDataManagerListener() {
		return new IPojoListener() {
			@Override
			public boolean listensToClass(Class<? extends BaseObject> klass) {
				return Category.class.isInstance(klass);
			}
			
			@Override
			public void pojoCreated(BaseObject object) {
//				Category category = (Category) object;
//				List<Object> pathList = new ArrayList<Object>();
//				pathList.add(getRoot());
//				pathList.add(category.getType());
//				
//				int[] childIndex = new int[1];
//				Object[] child = new Object[] { object };
//				
//				if (category.isSubCategory()) {
//					pathList.add(category.getParentCategory());
//					childIndex[0] = category.getParentCategory().getSubCategories().indexOf(object);
//				} else {
//					List<Category> list = categoryTree.get(category.getType());
//					int pos = Collections.binarySearch(list, category);
//					
//					if (0 > pos) {
//						pos = -pos - 1;
//						list.add(pos, category);
//						childIndex[0] = pos;
//					}
//				}
//				
//				fireTreeNodesInserted(pathList.toArray(), childIndex, child);
			}

			@Override
			public void pojoDeleted(BaseObject object) {
//				Category category = (Category) object;
//				List<Object> pathList = new ArrayList<Object>();
//				pathList.add(getRoot());
//				pathList.add(category.getType());
//				
//				int[] childIndex = new int[1];
//				Object[] child = new Object[] { object };
//				
//				if (category.isSubCategory()) {
//					pathList.add(category.getParentCategory());
//					childIndex[0] = -Collections.binarySearch(category.getParentCategory().getSubCategories(), category) - 1;
//				}
//				
//				fireTreeNodesRemoved(pathList.toArray(), childIndex, child);
			}

			@Override
			public void pojoSaved(BaseObject object) {
			}

			@Override
			public void pojoRefreshed(BaseObject object) {
			}
		};
	}

	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		reload(PersistenceManager.EMPTY_PARAMETER_MAP);
	}
	
	@Override
	public void clear() {
		for (List<Category> list : categoryTree.values())
			list.clear();
	}
}
