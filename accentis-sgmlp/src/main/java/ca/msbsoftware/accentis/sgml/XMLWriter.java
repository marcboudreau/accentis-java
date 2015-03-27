package ca.msbsoftware.accentis.sgml;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;

import ca.msbsoftware.accentis.sgml.resources.Resources;
import ca.msbsoftware.accentis.utils.logging.AccentisLogger;

public class XMLWriter {

	private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"; //$NON-NLS-1$

	public void write(Writer writer, Document document) {
		try {
			writeDeclaration(writer);
			writeElement(writer, document.getRootValue());
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			AccentisLogger.getLogger().log(Level.WARNING, Resources.getInstance().getString("message.xmlwriter.write.ioexception"), ex); //$NON-NLS-1$
		}
	}

	private static void writeDeclaration(Writer writer) throws IOException {
		writer.write(XML_DECLARATION);
	}
	
	private void writeElement(Writer writer, ComplexValue element) throws IOException {
		writer.write('<');
		writer.write(element.getName());
		writer.write('>');
		writer.write('\n');
		
		for (ComplexValue value : element.getContent())
			if (value instanceof SimpleValue)
				writeElement(writer, (SimpleValue) value);
			else
				writeElement(writer, value);
		
		writer.write('<');
		writer.write('/');
		writer.write(element.getName());
		writer.write('>');
		writer.write('\n');
	}
	
	private static void writeElement(Writer writer, SimpleValue element) throws IOException {
		writer.write('<');
		writer.write(element.getName());
		writer.write('>');
		writer.write(element.getText());
		writer.write('<');
		writer.write('/');
		writer.write(element.getName());
		writer.write('>');
		writer.write('\n');
	}
}
