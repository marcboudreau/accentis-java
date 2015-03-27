package ca.msbsoftware.accentis.ofx102;

import ca.msbsoftware.accentis.ofxparser.IOFXParserFactory;
import ca.msbsoftware.accentis.ofxparser.OFXParser;

public class OFX102ParserFactory implements IOFXParserFactory {

	@Override
	public OFXParser findParser(String version) {
		if (!"102".equals(version)) //$NON-NLS-1$
			return null;
		
		return new OFX102Parser();
	}

}
