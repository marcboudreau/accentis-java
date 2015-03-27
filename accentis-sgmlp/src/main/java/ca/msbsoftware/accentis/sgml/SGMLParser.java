package ca.msbsoftware.accentis.sgml;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import ca.msbsoftware.accentis.sgml.resources.Resources;
import ca.msbsoftware.accentis.utils.logging.AccentisLogger;

public class SGMLParser {

	public Document parse(BufferedReader reader) {
		Document document = new Document();
		try {
			parseHeaders(reader, document);
			parseContents(reader, document);
		} catch (Throwable th) {
			AccentisLogger.getLogger().log(Level.WARNING, Resources.getInstance().getString("message.sgmlparser.parse.ioexception"), th); //$NON-NLS-1$
		}

		return document;
	}

	private static void parseHeaders(BufferedReader reader, Document document) throws IOException {
		String line = reader.readLine();
		while (null != line && 0 != line.length()) {
			parseHeaderLine(line, document);
			line = reader.readLine();
		}
	}

	private static void parseHeaderLine(String line, Document document) {
		int pos = line.indexOf(':');
		if (-1 != pos) {
			String name = line.substring(0, pos);
			String value = line.substring(pos + 1);
			document.putHeader(name, value);
		}
	}

	private static void parseContents(BufferedReader reader, Document document) throws IOException {
		ArrayList<ComplexValue> values = new ArrayList<ComplexValue>();
		String line = reader.readLine();
		while (null != line) {
			line = line.trim();
			if (line.startsWith("</")) { //$NON-NLS-1$
				String tag = line.substring(2, line.indexOf('>'));
				ArrayList<ComplexValue> subValues = new ArrayList<ComplexValue>();
				for (int i = values.size() - 1; 0 <= i; --i) {
					ComplexValue cv = values.get(i);
					if (cv.getName().equals(tag)) {
						for (int j = subValues.size() - 1; 0 <= j; --j)
							cv.add(subValues.get(j));
						
						break;
					}
					
					subValues.add(values.remove(i));
				}
			} else if (line.startsWith("<")) { //$NON-NLS-1$
				String tag = line.substring(1, line.indexOf('>'));
				String val = line.substring(line.indexOf('>') + 1);
				ComplexValue cv = null;
				if (0 == val.length()) {
					cv = new ComplexValue(tag);
				} else {
					cv = new SimpleValue(tag, val);
				}
				values.add(cv);
			}

			line = reader.readLine();
		}

		if (0 < values.size())
			document.setRootValue(values.get(0));
	}
}
