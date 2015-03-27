package ca.msbsoftware.accentis.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import ca.msbsoftware.accentis.utils.DecimalUtils;

@SuppressWarnings("serial")
public class CurrencyDecimal implements Serializable {

	private Currency currency;

	private BigDecimal value;

	public CurrencyDecimal(BigDecimal value) {
		this(value, Currency.getInstance(Locale.getDefault()));
	}

	public CurrencyDecimal(BigDecimal value, Currency currency) {
		this.currency = currency;
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		else if (obj instanceof CurrencyDecimal) {
			CurrencyDecimal other = (CurrencyDecimal) obj;
			return (null == currency ? null == other.currency : currency.equals(other.currency))
					&& (null == value ? null == other.value : 0 == value.compareTo(other.value));
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return (null == currency ? 0 : currency.hashCode()) << 24 | (null == value ? 0 : value.hashCode()) & 0xffffff;
	}

	@Override
	public String toString() {
		return DecimalUtils.format(value, currency);
	}
}
