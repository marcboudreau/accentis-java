package ca.msbsoftware.accentis.gui.swing;

import java.text.ParseException;

import javax.swing.JFormattedTextField;

import ca.msbsoftware.accentis.persistence.InvalidSINException;
import ca.msbsoftware.accentis.persistence.pojos.SIN;

@SuppressWarnings("serial")
public class SINFormattedTextField extends JFormattedTextField {

	private static final String EMPTY_STRING = new String();
	
	public SINFormattedTextField() {
		super(new AbstractFormatterFactory() {
			@Override
			public AbstractFormatter getFormatter(JFormattedTextField tf) {
				return new AbstractFormatter() {
					@Override
					public String valueToString(Object value) throws ParseException {
						if (null == value)
							return EMPTY_STRING;
						
						return value.toString();
					}
					
					@Override
					public Object stringToValue(String text) throws ParseException {
						if (text.isEmpty())
							return null;
						
						try {
							return new SIN(text);
						} catch (InvalidSINException ex) {
							throw new ParseException(ex.getLocalizedMessage(), 0);
						}
					}
				};
			}
		});
	}
	
	public SIN getSINValue() {
		return (SIN) super.getValue();
	}
}
