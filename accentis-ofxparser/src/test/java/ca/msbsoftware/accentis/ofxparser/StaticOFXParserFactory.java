package ca.msbsoftware.accentis.ofxparser;

import ca.msbsoftware.accentis.ofxparser.IOFXParserFactory;
import ca.msbsoftware.accentis.ofxparser.OFXParser;

@SuppressWarnings("nls")
public class StaticOFXParserFactory implements IOFXParserFactory {

	@Override
	public OFXParser findParser(String version) {
		if ("102".equals(version))
			return createParser("org.msb.accentis.ofx102.OFX102Parser");
		return null;
	}

	private static OFXParser createParser(String className) {
		Class<? extends OFXParser> parserClass = loadParserClass(className);
		
		try {
			return parserClass.newInstance();
		} catch (InstantiationException ex) {
		} catch (IllegalAccessException ex) {
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static Class<? extends OFXParser> loadParserClass(String className) {
		try {
			return (Class<? extends OFXParser>) Class.forName(className);
		} catch (ClassNotFoundException ex) {
		}
		
		return null;
	}
}
