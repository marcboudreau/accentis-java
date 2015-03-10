package ca.msbsoftware.accentis.persistence.pojos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Category extends NamedObject {

	@ManyToOne
	private Category parentCategory;
	
	@OneToMany(mappedBy="parentCategory")
	private List<Category> subCategories = new ArrayList<Category>();
	
	public Category getParentCategory() {
		return parentCategory;
	}
	
	public void setParentCategory(Category value) {
		if (null != getParentCategory() && !getParentCategory().equals(value))
			unlink();
		
		if (null != value)
			link(value);
	}
	
	private void unlink() {
		assert null != parentCategory;
		
		parentCategory.subCategories.remove(this);
		parentCategory = null;
	}
	
	private void link(Category value) {
		assert null == parentCategory;
		
		parentCategory = value;		
		parentCategory.subCategories.add(this);
	}
}
