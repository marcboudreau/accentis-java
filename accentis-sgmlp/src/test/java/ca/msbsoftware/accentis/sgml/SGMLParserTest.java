package ca.msbsoftware.accentis.sgml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Test;

import ca.msbsoftware.accentis.sgml.Document;
import ca.msbsoftware.accentis.sgml.SGMLParser;
import ca.msbsoftware.accentis.sgml.SimpleValue;

public class SGMLParserTest {

	@Test
	public void parseEmptyStream() throws Exception {
		SGMLParser parser = new SGMLParser();
		BufferedReader reader = new BufferedReader(new StringReader(new String()));
		Document document = parser.parse(reader);
		assertNotNull(document);
		assertEquals(0, document.getHeadersCount());
		assertNull(document.getRootValue());
	}

	@Test
	public void parseHeaderOnlyStream() throws Exception {
		SGMLParser parser = new SGMLParser();
		BufferedReader reader = new BufferedReader(new StringReader("OFXHEADER:100\nDATA:OFXSGML\n\n")); //$NON-NLS-1$
		Document document = parser.parse(reader);
		assertEquals("100", document.getHeader("OFXHEADER")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("OFXSGML", document.getHeader("DATA")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void parseNoHeaderStream() throws Exception {
		SGMLParser parser = new SGMLParser();
		BufferedReader reader = new BufferedReader(new StringReader("\n<OFX>\n</OFX>\n")); //$NON-NLS-1$
		Document document = parser.parse(reader);
		assertEquals(0, document.getHeadersCount());
		assertEquals("OFX", document.getRootValue().getName()); //$NON-NLS-1$
	}

	@Test
	public void parseSimpleStream() throws Exception {
		SGMLParser parser = new SGMLParser();
		BufferedReader reader = new BufferedReader(new StringReader("\n<OFX>\n<SIMPLE>simple\n</OFX>\n")); //$NON-NLS-1$
		Document document = parser.parse(reader);
		assertEquals(0, document.getHeadersCount());
		assertEquals("OFX", document.getRootValue().getName()); //$NON-NLS-1$
		assertEquals(1, document.getRootValue().getContentSize());
		assertEquals("simple", ((SimpleValue) document.getRootValue().getContent().get(0)).getText()); //$NON-NLS-1$
	}
	
	@Test
	public void parseComplexStream() throws Exception {
		SGMLParser parser = new SGMLParser();
		BufferedReader reader = new BufferedReader(new StringReader("\n<OFX>\n<COMPLEX>\n<SIMPLE>simple\n<SUBCOMPLEX>\n<SUBSIMPLE>2\n</SUBCOMPLEX>\n</COMPLEX>\n</OFX>\n")); //$NON-NLS-1$
		Document document = parser.parse(reader);
		assertEquals(0, document.getHeadersCount());
		assertEquals("OFX", document.getRootValue().getName()); //$NON-NLS-1$
		assertEquals(1, document.getRootValue().getContentSize());
		assertEquals("COMPLEX", document.getRootValue().getContent().get(0).getName()); //$NON-NLS-1$
		assertEquals(2, document.getRootValue().getContent().get(0).getContentSize());
		assertEquals("SIMPLE", document.getRootValue().getContent().get(0).getContent().get(0).getName()); //$NON-NLS-1$
		assertEquals("simple", ((SimpleValue) document.getRootValue().getContent().get(0).getContent().get(0)).getText()); //$NON-NLS-1$
		assertEquals("SUBCOMPLEX", document.getRootValue().getContent().get(0).getContent().get(1).getName()); //$NON-NLS-1$
		
	}
}
