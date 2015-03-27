package ca.msbsoftware.accentis.sgml;

public class SimpleValue extends ComplexValue {

	private String text;
	
	public SimpleValue(String name) {
		this(name, null);
	}
	
	public SimpleValue(String name, String value) {
		super(name);
		text = value;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
