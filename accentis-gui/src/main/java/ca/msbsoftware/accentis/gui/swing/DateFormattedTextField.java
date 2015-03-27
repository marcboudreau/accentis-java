package ca.msbsoftware.accentis.gui.swing;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JFormattedTextField;

@SuppressWarnings("serial")
public class DateFormattedTextField extends JFormattedTextField {

	private static final String EMPTY_STRING = new String();
	
	private static DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
	
	public DateFormattedTextField() {
		super(new AbstractFormatterFactory() {
			@Override
			public AbstractFormatter getFormatter(JFormattedTextField tf) {
				return new AbstractFormatter() {
					@Override
					public String valueToString(Object value) throws ParseException {
						if (null == value)
							return EMPTY_STRING;
						
						String result = formatter.format((Date) value);
						return result;
					}
					
					@Override
					public Object stringToValue(String text) throws ParseException {
						if (text.isEmpty())
							return null;
						
						try {
							return formatter.parseObject(text);
						} catch (ParseException ex) {
							throw ex;
						}
					}
				};
			}
		});
		setColumns(12);
	}
	
	public Date getDateValue() {
		return (Date) super.getValue();
	}
}
