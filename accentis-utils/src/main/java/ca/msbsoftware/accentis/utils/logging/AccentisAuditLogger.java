package ca.msbsoftware.accentis.utils.logging;

import java.util.logging.Logger;

public class AccentisAuditLogger extends AccentisLogger {

	private static Logger logger;
	
	public static synchronized Logger getLogger() {
		if (null == logger)
			createLogger("org.msb.accentis.audit", "accentis-audit.log"); //$NON-NLS-1$ //$NON-NLS-2$
		
		return logger;
	}
}
