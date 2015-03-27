package ca.msbsoftware.accentis.ofxparser;

import java.io.BufferedReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class OFXParser {

	protected final DateFormat dateTimeFormatter = new SimpleDateFormat("yyyyMMddHHmmss"); //$NON-NLS-1$
	
	public abstract List<DownloadedTransactionData> parseDownloadedTransactions(BufferedReader reader);
}
