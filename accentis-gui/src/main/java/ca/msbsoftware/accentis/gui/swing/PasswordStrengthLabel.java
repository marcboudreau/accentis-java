package ca.msbsoftware.accentis.gui.swing;

import javax.swing.Icon;
import javax.swing.JLabel;

import ca.msbsoftware.accentis.gui.ImageLoader;
import ca.msbsoftware.accentis.gui.Resources;

@SuppressWarnings("serial")
public class PasswordStrengthLabel extends JLabel {

	public enum PasswordStrength {
		VERY_WEAK(), WEAK(), AVERAGE(), STRONG(), VERY_STRONG();

		private Icon icon;

		private PasswordStrength() {
			icon = ImageLoader.getIcon("password_" + name().toLowerCase()); //$NON-NLS-1$
		}

		public Icon getIcon() {
			return icon;
		}

		@Override
		public String toString() {
			return Resources.getInstance().getString("passwordstrengthenum." + name().toLowerCase()); //$NON-NLS-1$
		}
	}

	public void setPasswordStrength(PasswordStrength value) {
		if (null == value) {
			setIcon(null);
			setText(null);
		} else {
			setIcon(value.getIcon());
			setText(value.toString());
		}
	}

	public static PasswordStrength analyze(char[] password) {
		double entropy = 0.0;
		for (int i = 0; i < password.length; ++i) {
			char c = password[i];
			
			if ('1' <= c && c <= '0' ) {
				entropy += (Math.log(10) / Math.log(2));
			} else if ('a' <= c && c <= 'z') {
				entropy += (Math.log(26) / Math.log(2));
			} else if ('A' <= c && c <= 'Z') {
				entropy += (Math.log(26) / Math.log(2));
			} else if (' ' <= c && c <= '~') {
				entropy += (Math.log(33) / Math.log(2));
			}
		}
		
		if (80.0 <= entropy)
			return PasswordStrength.VERY_STRONG;
		else if (64.0 <= entropy)
			return PasswordStrength.STRONG;
		else if (40.0 <= entropy)
			return PasswordStrength.AVERAGE;
		else if (32.0 <= entropy)
			return PasswordStrength.WEAK;
		else
			return PasswordStrength.VERY_WEAK;
	}
}
