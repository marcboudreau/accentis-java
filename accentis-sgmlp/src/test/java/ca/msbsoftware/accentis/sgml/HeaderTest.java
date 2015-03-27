package ca.msbsoftware.accentis.sgml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import ca.msbsoftware.accentis.sgml.Header;

public class HeaderTest {

	private static final String HEADER_VALUE = "Value"; //$NON-NLS-1$
	private static final String HEADER_NAME = "headerName"; //$NON-NLS-1$
	private Header header;
	
	@Before
	public void setUp() throws Exception {
		header = new Header(HEADER_NAME, HEADER_VALUE);
	}
	
	@Test
	public void createHeaderWithNoValue() throws Exception {
		header = new Header(HEADER_NAME);
		assertEquals(HEADER_NAME + ":", header.toString()); //$NON-NLS-1$
	}

	@Test
	public void createHeaderWithNameAndValue() throws Exception {
		assertEquals(HEADER_NAME + ":" + HEADER_VALUE, header.toString()); //$NON-NLS-1$
	}

	@Test
	public void getHeaderName() throws Exception {
		assertEquals(HEADER_NAME, header.getName());
	}
	
	@Test
	public void getHeaderValueWhenNull() throws Exception {
		header = new Header(HEADER_NAME);
		assertNull(header.getValue());
	}
	
	@Test
	public void getHeaderValue() throws Exception {
		assertEquals(HEADER_VALUE, header.getValue());
	}
}
