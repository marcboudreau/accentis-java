package ca.msbsoftware.accentis.ofxparser;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import ca.msbsoftware.accentis.ofxparser.DownloadedTransactionData;
import ca.msbsoftware.accentis.ofxparser.IOFXReader;
import ca.msbsoftware.accentis.ofxparser.OFXReader;
import ca.msbsoftware.accentis.ofxparser.TransactionParser;

@SuppressWarnings("nls")
public class TransactionParserTest {

	private static File getTempDirectory() {
		File file = new File("temp");
		return file;
	}

	@After
	public void tearDown() throws Exception {
		File file = getTempDirectory();
		file.delete();
	}

	@Test
	public void verifyCreateTransactionParserWithNoTempDirectory() throws Exception {
		File file = getTempDirectory();
		file.delete();

		assertNotNull(new TransactionParser());
	}

	@Test
	public void verifyCreateTransactionParserWithTempDirectory() throws Exception {
		File file = getTempDirectory();
		file.mkdir();

		assertNotNull(new TransactionParser());
	}

	@Test
	public void testParse() throws Exception {
		IOFXReader ofxReader = createMock(IOFXReader.class);
		expect(ofxReader.readOFXData((File) anyObject())).andReturn(new ArrayList<DownloadedTransactionData>());
		
		replay(ofxReader);
		
		TransactionParser parser = new TransactionParser();
		parser.setOFXReader(ofxReader);

		parser.parse(new File("target/test-classes/fake-file.xml")); //$NON-NLS-1$
		
		verify(ofxReader);
	}

	@Test
	public void verifyIOExceptionHandling() {
		List<DownloadedTransactionData> list = new ArrayList<DownloadedTransactionData>();
		list.add(new DownloadedTransactionData());
		try {
			TransactionParser.writeDownloadedTransactionsToStream(list, new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					throw new IOException();
				}
			});
			fail();
		} catch (IOException ex) {
			// Pass.
		}
	}
	
	@Test
	public void verifyDefaultOFXReaderCreated() throws Exception {
		TransactionParser parser = new TransactionParser();
		assertEquals(OFXReader.class, parser.getOFXReader().getClass());
	}
	
	@Test
	public void verifyWriteDownloadedTransactionsToStream() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		List<DownloadedTransactionData> downloadedTransactions = new ArrayList<DownloadedTransactionData>();
		downloadedTransactions.add(new DownloadedTransactionData());
		
		TransactionParser.writeDownloadedTransactionsToStream(downloadedTransactions, baos);
		
		assertTrue(0 < baos.size());
	}
	
	@Test
	public void verifySetSerializedDataFile() throws Exception {
		File file = new File("."); //$NON-NLS-1$
		
		TransactionParser parser = new TransactionParser();
		parser.setSerializedDataFile(file);
		
		assertEquals(file, parser.getSerializedDataFile());
	}
}
