package ca.msbsoftware.accentis.persistence.pojos;

import java.text.DecimalFormat;

import javax.persistence.Embeddable;

import ca.msbsoftware.accentis.persistence.InvalidSINException;

/**
 * 
 * 
 * @author Marc Boudreau
 *
 * @since 1.0
 */
@Embeddable
public class SIN {

	private int numberValue;

	public SIN() throws InvalidSINException {
		this(0);
	}

	public SIN(int number) throws InvalidSINException {
		if (!luhn(number))
			throw new InvalidSINException(number);

		numberValue = number;
	}

	public SIN(String source) throws InvalidSINException {
		this(SINFormatter.parseSINValue(source));
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		else if (obj instanceof SIN)
			return numberValue == ((SIN) obj).numberValue;
		
		return false;
	}

	@Override
	public int hashCode() {
		return numberValue;
	}

	@Override
	public String toString() {
		return SINFormatter.format(this);
	}

	private static boolean luhn(int value) {
		int sum = 0;
		for (int i = 0; i < 9; ++i) {
			int digit = (value / (int) Math.pow(10, i)) % 10;
			int mult = i % 2 + 1;
			int factor = digit * mult;
			if (factor > 9)
				factor -= 9;
			sum += factor;
		}

		return 0 == sum % 10;
	}

	private static class SINFormatter {

		private static int parseSINValue(String source) {
			int number  = 0;
			for (int i = 0, j = 0; j < source.length(); ++j) {
				char c = source.charAt(j);
				if (Character.isDigit(c))
					number += Character.digit(c, 10) * (int) Math.pow(10, 8 - i++);
			}

			return number;
		}

		private static String format(SIN sin) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(padWithZeros(sin.numberValue / 1000000));
			buffer.append('-');
			buffer.append(padWithZeros(sin.numberValue / 1000 % 1000));
			buffer.append('-');
			buffer.append(padWithZeros(sin.numberValue % 1000));

			return buffer.toString();
		}

		private static String padWithZeros(int value) {
			DecimalFormat format = new DecimalFormat("000"); //$NON-NLS-1$
			return format.format(value);
		}
	}
}
