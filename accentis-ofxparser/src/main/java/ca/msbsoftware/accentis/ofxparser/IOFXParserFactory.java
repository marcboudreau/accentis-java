package ca.msbsoftware.accentis.ofxparser;

public interface IOFXParserFactory {

	OFXParser findParser(String version);
}
