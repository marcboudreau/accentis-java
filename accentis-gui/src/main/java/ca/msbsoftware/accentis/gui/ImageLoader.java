package ca.msbsoftware.accentis.gui;

import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageLoader {

	public static Icon getIcon(String basename) {
		return getImageIcon(basename);
	}
	
	public static Image getImage(String basename) {
		ImageIcon imageIcon = getImageIcon(basename);
		if (null == imageIcon)
			return null;
		
		return imageIcon.getImage();
	}
	
	private static ImageIcon getImageIcon(String basename) {
		URL url = ImageLoader.class.getResource(basename + ".png"); //$NON-NLS-1$
		if (null == url)
			return null;
		
		return new ImageIcon(url);
	}
}
