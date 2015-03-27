package ca.msbsoftware.accentis.sgml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComplexValue {

	private List<ComplexValue> valueList = new ArrayList<ComplexValue>();
	
	private String name;
	
	public ComplexValue(String name) {
		this.name = name;
	}
	
	public void add(ComplexValue value) {
		valueList.add(value);
	}
	
	public int getContentSize() {
		return valueList.size();
	}
	
	public List<ComplexValue> getContent() {
		return Collections.unmodifiableList(valueList);
	}

	public String getName() {
		return name;
	}
}
