package ca.msbsoftware.accentis.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

public class DecimalUtils {

	private static ThreadLocal<NumberFormat> decimalFormats = new ThreadLocal<NumberFormat>() {
		@Override
		public NumberFormat initialValue() {
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setGroupingUsed(true);
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);
			format.setMinimumIntegerDigits(1);
			
			return format;
		}
	};
	
	private static ThreadLocal<NumberFormat> currencyFormats = new ThreadLocal<NumberFormat>() {
		@Override
		protected NumberFormat initialValue() {
			NumberFormat format = NumberFormat.getCurrencyInstance();
			
			return format;
		}
	};
	
	public static String format(BigDecimal value) {
		if (null == value)
			return null;
		
		return decimalFormats.get().format(value);
	}
	
	public static String format(BigDecimal value, Currency currency) {
		if (null == currency)
			return format(value);
		
		NumberFormat format = currencyFormats.get();
		format.setCurrency(currency);
		
		return format.format(value);
	}
}
