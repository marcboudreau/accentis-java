package ca.msbsoftware.accentis.sgml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ca.msbsoftware.accentis.sgml.SimpleValue;


public class SimpleValueTest {

	private static final String SIMPLE = "SIMPLE"; //$NON-NLS-1$

	private static final String TEST_TEXT = "test-text"; //$NON-NLS-1$

	private SimpleValue value;
	
	@Test
	public void createDefaultSimpleValue() throws Exception {
		value = new SimpleValue(SIMPLE);
		assertNull(value.toString());
	}
	
	@Test
	public void createSimpleValueWithText() throws Exception {
		value = new SimpleValue(SIMPLE, TEST_TEXT);
		assertEquals(TEST_TEXT, value.toString());
	}
	
	@Test
	public void getValue() throws Exception {
		value = new SimpleValue(SIMPLE, TEST_TEXT);
		assertEquals(TEST_TEXT, value.getText());
	}
}
