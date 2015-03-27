package ca.msbsoftware.accentis.gui.swing;

import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import ca.msbsoftware.accentis.gui.ImageLoader;

public class ActionResourceDefinition {

	private static final String SPACE = " "; //$NON-NLS-1$
	
	private MnemonicCaption mnemonicCaption;

	private String iconName;
	
	private String keystroke;

	private String command;

	public ActionResourceDefinition(String resourceString, String command) {
		StringTokenizer tokens = new StringTokenizer(resourceString, ","); //$NON-NLS-1$
		mnemonicCaption = new MnemonicCaption(tokens.nextToken());

		if (tokens.hasMoreTokens())
			iconName = tokens.nextToken();
		
		if (tokens.hasMoreTokens())
			keystroke = tokens.nextToken();

		this.command = command;
	}

	public void configureAction(Action action) {
		mnemonicCaption.configureAction(action);

		if (null != iconName && !iconName.equals(SPACE)) {
			action.putValue(MSBAction.LARGE_ICON_KEY, ImageLoader.getIcon(iconName + "_l")); //$NON-NLS-1$
			action.putValue(MSBAction.SMALL_ICON, ImageLoader.getIcon(iconName + "_s")); //$NON-NLS-1$
		}
		
		if (null != keystroke && !keystroke.equals(SPACE))
			action.putValue(MSBAction.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(keystroke));

		if (null != command)
			action.putValue(MSBAction.ACTION_COMMAND_KEY, command);
	}
	
	public void configureJMenu(JMenu menu) {
		mnemonicCaption.configureJMenu(menu);
		
		if (null != command)
			menu.setActionCommand(command);
	}

	private static class MnemonicCaption {

		private String caption;

		private char mnemonic;

		private int mnemonicIndex = -1;

		public MnemonicCaption(String annotatedCaption) {
			StringBuilder captionBuilder = new StringBuilder();

			
			for (int i = 0; i < annotatedCaption.length(); ++i) {
				char c = annotatedCaption.charAt(i);
				if ('&' == c && i < annotatedCaption.length() - 1) {
					char c2 = annotatedCaption.charAt(++i);
					captionBuilder.append(c2);

					if ('&' != c2) {
						mnemonic = c2;
						mnemonicIndex = captionBuilder.length() - 1;
						captionBuilder
								.append(annotatedCaption.substring(i + 1));
						break;
					}
				} else
					captionBuilder.append(c);
			}

			caption = captionBuilder.toString();
		}

		private void configureAction(Action action) {
			action.putValue(MSBAction.NAME, caption);

			if ('\0' != mnemonic)
				action.putValue(MSBAction.MNEMONIC_KEY, new Integer(mnemonic));

			if (-1 != mnemonicIndex)
				action.putValue(MSBAction.DISPLAYED_MNEMONIC_INDEX_KEY,
						new Integer(mnemonicIndex));
		}
		
		private void configureJMenu(JMenu menu) {
			menu.setText(caption);
			
			if ('\0' != mnemonic)
				menu.setMnemonic(mnemonic);
			
			if (-1 != mnemonicIndex)
				menu.setDisplayedMnemonicIndex(mnemonicIndex);
		}
	}
}