package ca.msbsoftware.accentis.sgml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

import ca.msbsoftware.accentis.sgml.SGML2XML;

public class SGML2XMLTest {

	@Test
	public void verifyConvertSGML2XML() throws Exception {
		StringReader reader = new StringReader("\n<OFX>\n<COMPLEX>\n<SIMPLE>simple\n<SUBCOMPLEX>\n<SUBSIMPLE>2\n</SUBCOMPLEX>\n</COMPLEX>\n</OFX>\n"); //$NON-NLS-1$
		StringWriter writer = new StringWriter();

		SGML2XML.convertSGML2XML(writer, reader);

		assertEquals(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<OFX>\n<COMPLEX>\n<SIMPLE>simple</SIMPLE>\n<SUBCOMPLEX>\n<SUBSIMPLE>2</SUBSIMPLE>\n</SUBCOMPLEX>\n</COMPLEX>\n</OFX>\n", writer.getBuffer().toString()); //$NON-NLS-1$
	}
	
	@Test
	public void verifyConstructor() throws Exception {
		assertNotNull(new SGML2XML());
	}
}
