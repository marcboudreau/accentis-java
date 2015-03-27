package ca.msbsoftware.accentis.persistence.pojos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({@NamedQuery(name=Category.GET_ALL_TOP_LEVEL_CATEGORIES_QUERY, query="SELECT o FROM Category o WHERE o.parentCategory IS NULL")})
public class Category extends NamedObject {

	public static final String GET_ALL_TOP_LEVEL_CATEGORIES_QUERY = "Category.GET_ALL_TOP_LEVEL_CATEGORIES_QUERY";
	
	@Enumerated(EnumType.STRING)
	private CategoryType type;
	
	@ManyToOne
	private Category parentCategory;
	
	@OneToMany(mappedBy="parentCategory")
	private List<Category> subCategories = new ArrayList<Category>();
	
	public CategoryType getType() {
		return type;
	}
	
	public void setType(CategoryType value) {
		type = value;
	}
	
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
	
	public List<Category> getSubCategories() {
		return subCategories;
	}
}
