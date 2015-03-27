package ca.msbsoftware.accentis.ofxparser;

import java.io.File;
import java.util.List;

/**
 * Defines the interface for classes that are able to read OFX files.
 * 
 * @author Marc Boudreau
 * 
 * @since 0.6.0
 */
public interface IOFXReader {

	/**
	 * Reads and parses the data contained in an OFX file.
	 * 
	 * @param ofxFile
	 *            A {@link File} instance that refers to the file to read and
	 *            parse.
	 * 
	 * @return A {@link List} of {@link DownloadedTransactionData} objects that
	 *         consists of the objects created from the data extracted from the
	 *         OFX file.
	 */
	List<DownloadedTransactionData> readOFXData(File ofxFile);

}