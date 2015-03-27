package ca.msbsoftware.accentis.sgml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ca.msbsoftware.accentis.sgml.Document;


public class DocumentTest {

	@Test
	public void createDefaultDocument() throws Exception {
		Document document = new Document();
		
		assertEquals(0, document.getHeadersCount());
		assertNull(document.getRootValue());
	}
	
}
