package ca.msbsoftware.accentis.gui.swing;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class PlainLabel extends JLabel {

	public PlainLabel() {
		super();
		makeFontPlain();
	}

	public PlainLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		makeFontPlain();
	}

	public PlainLabel(Icon image) {
		super(image);
		makeFontPlain();
	}

	public PlainLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		makeFontPlain();
	}

	public PlainLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		makeFontPlain();
	}

	public PlainLabel(String text) {
		super(text);
		makeFontPlain();
	}

	private void makeFontPlain() {
		setFont(getFont().deriveFont(Font.PLAIN));
	}
}
