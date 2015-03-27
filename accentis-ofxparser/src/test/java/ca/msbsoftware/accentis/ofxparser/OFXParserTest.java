package ca.msbsoftware.accentis.ofxparser;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.util.List;

import org.junit.Test;

import ca.msbsoftware.accentis.ofxparser.DownloadedTransactionData;
import ca.msbsoftware.accentis.ofxparser.OFXParser;



public class OFXParserTest {

	@Test
	public void verifyCreateOFXParser() throws Exception {
		assertNotNull(new OFXParser() {
			@Override
			public List<DownloadedTransactionData> parseDownloadedTransactions(BufferedReader reader) {
				return null;
			}
		});
	}
}
