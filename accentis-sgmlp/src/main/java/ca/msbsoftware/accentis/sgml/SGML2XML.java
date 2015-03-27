package ca.msbsoftware.accentis.sgml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;

import ca.msbsoftware.accentis.sgml.resources.Resources;
import ca.msbsoftware.accentis.utils.logging.AccentisLogger;

public class SGML2XML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			convertSGML2XML(new PrintWriter(new FileWriter(args[1])), new FileReader(args[0]));
		} catch (IOException ex) {
			AccentisLogger.getLogger().log(Level.WARNING, Resources.getInstance().getString("message.sgml2xml.write.ioexception"), ex); //$NON-NLS-1$
		}
	}

	public static void convertSGML2XML(Writer writer, Reader reader) {
		new XMLWriter().write(writer, new SGMLParser().parse(new BufferedReader(reader)));
	}
}
