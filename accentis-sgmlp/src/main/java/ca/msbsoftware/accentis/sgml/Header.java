package ca.msbsoftware.accentis.sgml;

public class Header {

	private String name;

	private String value;

	public Header(String name) {
		this(name, null);
	}
	
	public Header(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return name + ":" + (null == value ? new String() : value); //$NON-NLS-1$
	}

}
