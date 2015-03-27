package ca.msbsoftware.accentis.utils.logging;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.msbsoftware.accentis.utils.Resources;

public class AccentisLogger {

	private static Logger logger;

	public static Logger getLogger() {
		if (null == logger)
			createLogger();

		return logger;
	}

	protected static void createLogger() {
		createLogger("Accentis", "accentis.log"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	protected static void createLogger(String loggerName, String filepath) {
		logger = Logger.getLogger(loggerName);
		try {
			File accentisDirectory = new File(System.getProperty("user.home"), "accentis"); //$NON-NLS-1$ //$NON-NLS-2$
			if (!accentisDirectory.exists())
				accentisDirectory.mkdir();

			logger.addHandler(new FileHandler(accentisDirectory.getPath() + File.separator + filepath));
		} catch (SecurityException ex) {
			logger.log(Level.WARNING, MessageFormat.format(Resources.getInstance().getString("message.logger.addhandler.securityexception"), loggerName), //$NON-NLS-1$
					ex);
		} catch (IOException ex) {
			logger.log(Level.WARNING, MessageFormat.format(Resources.getInstance().getString("message.logger.addhandler.ioexception"), loggerName, filepath), //$NON-NLS-1$
					ex);
		}
	}
}
