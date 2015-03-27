package ca.msbsoftware.accentis.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Test;

import ca.msbsoftware.accentis.utils.DecimalUtils;

public class DecimalUtilsTest {

	private BigDecimal bigDecimal = new BigDecimal(9405723952.23034);
	
	private String bigDecimalFormatted = "9,405,723,952.23"; //$NON-NLS-1$
	
	@Test
	public void verifyFormatNull() {
		assertNull(DecimalUtils.format(null));
	}
	
	@Test
	public void verifyFormat() {
		assertEquals(bigDecimalFormatted, DecimalUtils.format(bigDecimal));
	}
	
	@Test
	public void verifyFormatWithNullCurrency() {
		final BigDecimal value = new BigDecimal(9405723952.23034);
		
		assertEquals(bigDecimalFormatted, DecimalUtils.format(value, null));
	}
	
	@Test
	public void verifyFormatWithCurrency() {
		final BigDecimal value = new BigDecimal(9405723952.23034);
		final Currency currency = Currency.getInstance(Locale.getDefault());
		
		assertTrue(DecimalUtils.format(value, currency).contains(bigDecimalFormatted));
		assertTrue(DecimalUtils.format(value, currency).contains(currency.getSymbol()));
	}
}
