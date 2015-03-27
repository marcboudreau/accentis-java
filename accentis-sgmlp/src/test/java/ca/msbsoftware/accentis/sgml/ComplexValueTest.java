package ca.msbsoftware.accentis.sgml;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ca.msbsoftware.accentis.sgml.ComplexValue;
import ca.msbsoftware.accentis.sgml.SimpleValue;


public class ComplexValueTest {

	private static final String SUBCOMPLEX = "SUBCOMPLEX"; //$NON-NLS-1$
	private static final String SIMPLE = "SIMPLE"; //$NON-NLS-1$
	private static final String COMPLEX = "COMPLEX"; //$NON-NLS-1$
	private ComplexValue complexValue;
	
	@Test
	public void createDefaultComplexValue() throws Exception {
		complexValue = new ComplexValue(COMPLEX);
		assertEquals(0, complexValue.getContentSize());
	}
	
	@Test
	public void addSimpleValueToComplexValue() throws Exception {
		complexValue = new ComplexValue(COMPLEX);
		SimpleValue simpleValue = new SimpleValue(SIMPLE, "simple"); //$NON-NLS-1$
		
		complexValue.add(simpleValue);
		assertEquals(1, complexValue.getContentSize());
	}
	
	@Test
	public void addComplexValueToComplexValue() throws Exception {
		complexValue = new ComplexValue(COMPLEX);
		ComplexValue subComplexValue = new ComplexValue(SUBCOMPLEX);
		
		complexValue.add(subComplexValue);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void ensureValueListIsUnmodifiable() throws Exception {
		complexValue = new ComplexValue(COMPLEX);
		List<ComplexValue> values = complexValue.getContent();
		values.add(new SimpleValue(SIMPLE));
	}
}
