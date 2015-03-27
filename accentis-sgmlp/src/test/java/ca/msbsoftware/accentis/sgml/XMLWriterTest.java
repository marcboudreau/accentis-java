package ca.msbsoftware.accentis.sgml;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

import ca.msbsoftware.accentis.sgml.SGMLParser;
import ca.msbsoftware.accentis.sgml.XMLWriter;


public class XMLWriterTest {

	@Test
	public void parseAndWriteInXML() throws Exception {
		SGMLParser parser = new SGMLParser();
		BufferedReader reader = new BufferedReader(new StringReader("OFXHEADER:102\nDATA:OFXSGML\n\n<OFX>\n<COMPLEX>\n<SIMPLE>simple\n<SUBCOMPLEX>\n<SUBSIMPLE>2\n</SUBCOMPLEX>\n</COMPLEX>\n</OFX>\n")); //$NON-NLS-1$
		StringWriter writer = new StringWriter();
		XMLWriter xmlwriter = new XMLWriter();
		xmlwriter.write(writer, parser.parse(reader));
		assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<OFX>\n<COMPLEX>\n<SIMPLE>simple</SIMPLE>\n<SUBCOMPLEX>\n<SUBSIMPLE>2</SUBSIMPLE>\n</SUBCOMPLEX>\n</COMPLEX>\n</OFX>\n", writer.toString()); //$NON-NLS-1$
	}
}
