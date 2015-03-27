package ca.msbsoftware.accentis.gui.swing;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;

@SuppressWarnings("serial")
public class TwoDecimalTextField extends JFormattedTextField {

	private static NumberFormat format = createNumberFormat();

	public TwoDecimalTextField() {
		super(new AbstractFormatterFactory() {
			@Override
			public AbstractFormatter getFormatter(JFormattedTextField tf) {
				return new AbstractFormatter() {
					@Override
					public Object stringToValue(String text) throws ParseException {
						if (null == text || text.isEmpty())
							return null;
						
						try {
							Number number = (Number) format.parseObject(text);
							return new BigDecimal(number.doubleValue());
						} catch (ParseException ex) {
							System.err.println(ex);
							ex.printStackTrace();

							throw ex;
						}
					}

					@Override
					public String valueToString(Object value) throws ParseException {
						if (null == value)
							value = BigDecimal.ZERO;

						return format.format(value);
					}
				};
			}
		});

		setColumns(20);
	}

	private static NumberFormat createNumberFormat() {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(2);
		format.setMinimumIntegerDigits(1);

		return format;
	}

	public BigDecimal getBigDecimalValue() {
		return (BigDecimal) getValue();
	}
}
