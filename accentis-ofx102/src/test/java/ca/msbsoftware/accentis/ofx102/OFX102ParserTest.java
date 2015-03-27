package ca.msbsoftware.accentis.ofx102;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.msbsoftware.accentis.ofx102.OFX102Parser;
import ca.msbsoftware.accentis.ofxparser.DownloadedTransactionData;


public class OFX102ParserTest {

	private OFX102Parser parser;
	
	@Before
	public void setUp() throws Exception {
		parser = new OFX102Parser();
	}
	
	@Test
	public void verifyParseDownloadedTransactionsWithEmptyReader() throws Exception {
		List<DownloadedTransactionData> list = parser.parseDownloadedTransactions(new BufferedReader(new StringReader(new String())));
		
		assertEquals(0, list.size());
	}
	
	@Test
	public void verifyParseDownloadedTransactionsWithIncompleteReader() throws Exception {
		List<DownloadedTransactionData> list = parser.parseDownloadedTransactions(new BufferedReader(new StringReader("<OFX>\n<BANKMSGSRSV1>\n<STMTTRNRS>\n<TRNUID>123\n</STMTTRNRS>\n<STMTRS>\n"))); //$NON-NLS-1$
		
		assertEquals(0, list.size());
	}
	
	@Test
	public void verifyParseDownloadedTransactionsWithOneTransaction() throws Exception {
		final String ofxData = "OFXHEADER:100\nDATA:OFXSGML\nVERSION:102\nSECURITY:TYPE1\nENCODING:USASCII\nCHARSET:1252\nCOMPRESSION:NONE\nOLDFILEUID:NONE\nNEWFILEUID:NONE\n\n<OFX>\n<SIGNONMSGSRSV1>\n<SONRS>\n<STATUS>\n<CODE>0\n<SEVERITY>INFO\n<MESSAGE>OK\n</STATUS>\n<DTSERVER>20100310155751[-5:EST]\n<USERKEY>--NoUserKey--\n<LANGUAGE>ENG\n<INTU.BID>00002\n</SONRS>\n</SIGNONMSGSRSV1>\n<BANKMSGSRSV1>\n<STMTTRNRS>\n<TRNUID>QWEB - 201003101557516081\n<STATUS>\n<CODE>0\n<SEVERITY>INFO\n<MESSAGE>OK\n</STATUS>\n<STMTRS>\n<CURDEF>CAD\n<BANKACCTFROM>\n<BANKID>300000100\n<ACCTID>3217439\n<ACCTTYPE>CHECKING\n</BANKACCTFROM>\n<BANKTRANLIST>\n<DTSTART>20100301\n<DTEND>20100309020000[-5:EST]\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100301020000[-5:EST]\n<TRNAMT>-60.12\n<FITID>02010030110058094003340010\n<NAME>BULK BARN #504\n</STMTTRN>\n</BANKTRANLIST>\n<LEDGERBAL>\n<BALAMT>263474.10\n<DTASOF>20100309020000[-5:EST]\n</LEDGERBAL>\n<AVAILBAL>\n<BALAMT>28613.75\n<DTASOF>20100310020000[-5:EST]\n</AVAILBAL>\n</STMTRS>\n</STMTTRNRS>\n</BANKMSGSRSV1>\n</OFX>\n"; //$NON-NLS-1$
		
		List<DownloadedTransactionData> list = parser.parseDownloadedTransactions(new BufferedReader(new StringReader(ofxData)));
		
		assertEquals(1, list.size());
	}
	
	@Test
	public void verifyParseDownloadedTransactionsWithMultipleTransactions() throws Exception {
		final String ofxData = "OFXHEADER:100\nDATA:OFXSGML\nVERSION:102\nSECURITY:TYPE1\nENCODING:USASCII\nCHARSET:1252\nCOMPRESSION:NONE\nOLDFILEUID:NONE\nNEWFILEUID:NONE\n\n<OFX>\n<SIGNONMSGSRSV1>\n<SONRS>\n<STATUS>\n<CODE>0\n<SEVERITY>INFO\n<MESSAGE>OK\n</STATUS>\n<DTSERVER>20100310155751[-5:EST]\n<USERKEY>--NoUserKey--\n<LANGUAGE>ENG\n<INTU.BID>00002\n</SONRS>\n</SIGNONMSGSRSV1>\n<BANKMSGSRSV1>\n<STMTTRNRS>\n<TRNUID>QWEB - 201003101557516081\n<STATUS>\n<CODE>0\n<SEVERITY>INFO\n<MESSAGE>OK\n</STATUS>\n<STMTRS>\n<CURDEF>CAD\n<BANKACCTFROM>\n<BANKID>300000100\n<ACCTID>3217439\n<ACCTTYPE>CHECKING\n</BANKACCTFROM>\n<BANKTRANLIST>\n<DTSTART>20100301\n<DTEND>20100309020000[-5:EST]\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100301020000[-5:EST]\n<TRNAMT>-60.12\n<FITID>02010030110058094003340010\n<NAME>BULK BARN #504\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100301020000[-5:EST]\n<TRNAMT>-16.02\n<FITID>02010030110058102747940020\n<NAME>BULK BARN #504\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100301020000[-5:EST]\n<TRNAMT>-114.37\n<FITID>02010030110059135213890030\n<NAME>TWEED &amp; HICKORY\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100301020000[-5:EST]\n<TRNAMT>-141.33\n<FITID>02010030110059145350920040\n<NAME>METRO #256\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100301020000[-5:EST]\n<TRNAMT>-141.77\n<FITID>02010030110060103347480050\n<NAME>WAL-MART #3638\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100301020000[-5:EST]\n<TRNAMT>-400.00\n<FITID>02010030110060132152690060\n<NAME>RW215 TFR-TO 6340110\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100303020000[-5:EST]\n<TRNAMT>-29.27\n<FITID>02010030310062201203790010\n<NAME>IKEA OTTAWA\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100303020000[-5:EST]\n<TRNAMT>-45.82\n<FITID>02010030310062212200560020\n<NAME>TD MORTGAGE\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>CREDIT\n<DTPOSTED>20100305020000[-5:EST]\n<TRNAMT>2354.85\n<FITID>02010030510064030445600010\n<NAME>IBM CANADA LTD   PAY\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100305020000[-5:EST]\n<TRNAMT>-400.00\n<FITID>02010030510064134355210020\n<NAME>JW435 TFR-TO 6340048\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>CREDIT\n<DTPOSTED>20100308020000[-5:EST]\n<TRNAMT>100.00\n<FITID>02010030810065155830630010\n<NAME>GM DEPOSIT    009184\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>CHECK\n<DTPOSTED>20100308020000[-5:EST]\n<TRNAMT>-420.00\n<FITID>02010030810067170335700020\n<CHECKNUM>000038\n<NAME>CHQ#00038- GC 2831\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>CREDIT\n<DTPOSTED>20100309020000[-5:EST]\n<TRNAMT>1981.15\n<FITID>02010030910068132106600010\n<NAME>GM DEPOSIT    001049\n</STMTTRN>\n<STMTTRN>\n<TRNTYPE>DEBIT\n<DTPOSTED>20100309020000[-5:EST]\n<TRNAMT>-34.65\n<FITID>02010030910068180051940020\n<NAME>BULK BARN # 523\n</STMTTRN>\n</BANKTRANLIST>\n<LEDGERBAL>\n<BALAMT>263474.10\n<DTASOF>20100309020000[-5:EST]\n</LEDGERBAL>\n<AVAILBAL>\n<BALAMT>28613.75\n<DTASOF>20100310020000[-5:EST]\n</AVAILBAL>\n</STMTRS>\n</STMTTRNRS>\n</BANKMSGSRSV1>\n</OFX>\n"; //$NON-NLS-1$
	
		List<DownloadedTransactionData> list = parser.parseDownloadedTransactions(new BufferedReader(new StringReader(ofxData)));
		
		assertEquals(14, list.size());
	}
}
