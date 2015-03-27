package ca.msbsoftware.accentis.ofxparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;

import ca.msbsoftware.accentis.utils.logging.AccentisLogger;

/**
 * The {@code OFXReader} class begins reading the OFX data provided by a {@link Reader} object to determine the appropriate {@link OFXParser} implementation to
 * use.
 * 
 * @author Marc Boudreau
 * 
 */
public class OFXReader implements IOFXReader {

	private static final String PARSER_FACTORY_PROPERTY = "org.msb.accentis.ofxparser.IOFXParserFactory"; //$NON-NLS-1$

	private static final String PARSER_FACTORY_PROPERTY_DEFAULT_VALUE = "org.msb.accentis.ofxparser.OFXParserFactory"; //$NON-NLS-1$

	private IOFXParserFactory ofxParserFactory;

	public OFXReader() {
	}

	public IOFXParserFactory getOFXParserFactory() {
		if (null == ofxParserFactory)
			createOFXParserFactory();

		return ofxParserFactory;
	}

	private void createOFXParserFactory() {
		String parserFactoryClass = System.getProperty(PARSER_FACTORY_PROPERTY, PARSER_FACTORY_PROPERTY_DEFAULT_VALUE);
		Class<? extends IOFXParserFactory> factoryClass = loadParserFactoryClass(parserFactoryClass);
		ofxParserFactory = instantiateOFXParserFactory(factoryClass);
	}

	private static IOFXParserFactory instantiateOFXParserFactory(Class<? extends IOFXParserFactory> factoryClass) {
		if (null != factoryClass)
			try {
				return factoryClass.newInstance();
			} catch (InstantiationException ex) {

			} catch (IllegalAccessException ex) {

			}

		return new OFXParserFactory();
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends IOFXParserFactory> loadParserFactoryClass(String parserFactoryClass) {
		try {
			return (Class<? extends IOFXParserFactory>) Class.forName(parserFactoryClass);
		} catch (ClassNotFoundException ex) {
		}

		return null;
	}

	public void setOFXParserFactory(IOFXParserFactory factory) {
		ofxParserFactory = factory;
	}

	@Override
	public List<DownloadedTransactionData> readOFXData(File ofxFile) {
		String ofxVersion = determineOFXVersion(createBufferedFileReader(ofxFile));
		OFXParser ofxParser = getOFXParserFactory().findParser(ofxVersion);

		return ofxParser.parseDownloadedTransactions(createBufferedFileReader(ofxFile));
	}
	
	private static BufferedReader createBufferedFileReader(File file) {
		try {
			return new BufferedReader(new FileReader(file));
		} catch (IOException ex) {
			AccentisLogger.getLogger().log(Level.WARNING, String.format("The OFX file %s could not be opened for reading.", file.getPath()), ex);
		}
		
		return new BufferedReader(new StringReader(""));
	}

	static String determineOFXVersion(BufferedReader reader) {
		String version = null;
		try {
			String line = reader.readLine();
			while (null != line) {
				int pos = line.indexOf("VERSION"); //$NON-NLS-1$
				if (-1 < pos) {
					char c = line.charAt(pos + 7);

					if (':' == c) {
						version = line.substring(pos + 8);
						break;
					} else if ('=' == c) {
						version = line.substring(pos + 9, line.indexOf('"', pos + 9));
						break;
					}
				}

				line = reader.readLine();
			}
		} catch (IOException ex) {
			AccentisLogger.getLogger().log(Level.WARNING, Resources.getInstance().getString("message.ofxreader.exceptionreadingofxforversion"), ex); //$NON-NLS-1$
		}

		return version;
	}

}
