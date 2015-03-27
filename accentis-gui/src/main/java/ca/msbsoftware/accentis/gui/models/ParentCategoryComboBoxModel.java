package ca.msbsoftware.accentis.gui.models;

import java.util.Collections;

import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
import ca.msbsoftware.accentis.persistence.pojos.Category;

@SuppressWarnings("serial")
public class ParentCategoryComboBoxModel extends PojoComboBoxModel<Category> {

	public ParentCategoryComboBoxModel(String queryName) {
		super(queryName, Category.class, true);
	}

	@Override
	protected IPojoListener createPojoListener() {
		return new IPojoListener() {
			@Override
			public boolean listensToClass(Class<? extends BaseObject> klass) {
				return Category.class.isInstance(klass);
			}
			@Override
			public void pojoCreated(BaseObject object) {
				Category category = (Category) object;
				if (null != category.getParentCategory())
					return;
				
				int index = Collections.binarySearch(list, category);
				if (0 <= index)
					return;
				
				index = -index - 1 + offset();
				
				list.add(index, category);
				fireIntervalAdded(this, index, index);
			}

			@Override
			public void pojoDeleted(BaseObject object) {
				Category category = (Category) object;
				if (null != category.getParentCategory())
					return;
				
				int index = Collections.binarySearch(list, category);
				if (0 > index)
					return;
				
				list.remove(index);
				
				index += offset();
				
				fireIntervalRemoved(this, index, index);
			}

			@Override
			public void pojoSaved(BaseObject object) {
				/*
				 * 1) Subcategory --> Subcategory
				 * 2) Subcategory --> Category
				 * 3) Category    --> Subcategory
				 * 4) Category    --> Category
				 * 
				 * object.isSubcategory()
				 * 		preSortIndex == -1
				 * 			1)
				 * 		else
				 * 			3)
				 *  else
				 *  	preSortIndex == -1
				 *  		2)
				 *  	else
				 *  		4)
				 */
				Category category = (Category) object;
				boolean subcategory = null != category.getParentCategory();
				int preSortIndex = list.indexOf(object);
				if (subcategory)
					if (-1 == preSortIndex)
						return;
					else {
						list.remove(preSortIndex);
						fireIntervalRemoved(this, preSortIndex, preSortIndex);
					}
				else
					if (-1 == preSortIndex) {
						int pos = Collections.binarySearch(list, category);
						pos = -pos - 1 + offset();
						list.add(pos, category);
						fireIntervalAdded(this, pos, pos);
					} else {
						Collections.sort(list);
						int postSortIndex = Collections.binarySearch(list, category);

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
			}

			@Override
			public void pojoRefreshed(BaseObject object) {
			}
		};
	}
}
