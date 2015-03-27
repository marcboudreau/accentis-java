package ca.msbsoftware.accentis.sgml;

import java.util.HashMap;
import java.util.Map;

public class Document {

	private Map<String, String> headers = new HashMap<String, String>();
	
	private ComplexValue rootValue;
	
	public int getHeadersCount() {
		return headers.size();
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
	
	public void putHeader(String name, String value) {
		headers.put(name, value);
	}
	
	public ComplexValue getRootValue() {
		return rootValue;
	}
	
	public void setRootValue(ComplexValue complexValue) {
		rootValue = complexValue;
	}
}
