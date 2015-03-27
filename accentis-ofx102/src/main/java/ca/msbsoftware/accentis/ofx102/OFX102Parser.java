package ca.msbsoftware.accentis.ofx102;

import static ca.msbsoftware.accentis.ofx102.OFX102Constants.ACCTID;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.BANKACCTFROM;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.BANKID;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.BANKMSGSRSV1;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.BANKTRANLIST;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.CURDEF;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.DTPOSTED;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.NAME;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.STMTRS;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.STMTTRN;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.STMTTRNRS;
import static ca.msbsoftware.accentis.ofx102.OFX102Constants.TRNAMT;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import ca.msbsoftware.accentis.ofxparser.DownloadedTransactionData;
import ca.msbsoftware.accentis.ofxparser.OFXParser;
import ca.msbsoftware.accentis.sgml.ComplexValue;
import ca.msbsoftware.accentis.sgml.Document;
import ca.msbsoftware.accentis.sgml.SGMLParser;
import ca.msbsoftware.accentis.sgml.SimpleValue;

public class OFX102Parser extends OFXParser {

	private void parseOFX(List<DownloadedTransactionData> downloadedTransactions, ComplexValue ofx) {
		if (null == ofx)
			return;
		
		List<ComplexValue> ofxContents = ofx.getContent();
		for (ComplexValue ofxContent : ofxContents)
			if (ofxContent.getName().equals(BANKMSGSRSV1))
				parseBankMessageSet(downloadedTransactions, ofxContent);
	}

	private void parseBankMessageSet(List<DownloadedTransactionData> downloadedTransactions, ComplexValue bankMessageSet) {
		List<ComplexValue> bankMessageSetContents = bankMessageSet.getContent();
		for (ComplexValue bankMessageSetContent : bankMessageSetContents)
			if (bankMessageSetContent.getName().equals(STMTTRNRS))
				parseBankTransactionStatementResponse(downloadedTransactions, bankMessageSetContent);
	}

	private void parseBankTransactionStatementResponse(List<DownloadedTransactionData> downloadedTransactions, ComplexValue bankTransactionStatementResponse) {
		List<ComplexValue> bankTransactionStatementResponseContents = bankTransactionStatementResponse.getContent();
		for (ComplexValue bankTransactionStatementResponseContent : bankTransactionStatementResponseContents)
			if (bankTransactionStatementResponseContent.getName().equals(STMTRS))
				parseBankStatementResponse(downloadedTransactions, bankTransactionStatementResponseContent);
	}

	private void parseBankStatementResponse(List<DownloadedTransactionData> downloadedTransactions, ComplexValue bankStatementResponse) {
		String currencyCode = null;
		String bankAccountId = null;

		List<ComplexValue> bankStatementResponseContents = bankStatementResponse.getContent();
		for (ComplexValue bankStatementResponseContent : bankStatementResponseContents)
			if (bankStatementResponseContent.getName().equals(CURDEF))
				currencyCode = ((SimpleValue) bankStatementResponseContent).getText();
			else if (bankStatementResponseContent.getName().equals(BANKACCTFROM))
				bankAccountId = parseBankAccountFrom(bankStatementResponseContent);
			else if (bankStatementResponseContent.getName().equals(BANKTRANLIST))
				parseBankTransactionList(downloadedTransactions, bankStatementResponseContent);
		
		Currency currency = Currency.getInstance(currencyCode);
		for (DownloadedTransactionData downloadedTransaction : downloadedTransactions) {
			downloadedTransaction.bankAccountId = bankAccountId;
			downloadedTransaction.currency = currency;
		}
	}

	private static String parseBankAccountFrom(ComplexValue bankAccountFrom) {
		String accountId = null;
		String bankId = null;

		List<ComplexValue> bankAccountFromContents = bankAccountFrom.getContent();
		for (ComplexValue bankAccountFromContent : bankAccountFromContents)
			if (bankAccountFromContent.getName().equals(BANKID))
				bankId = ((SimpleValue) bankAccountFromContent).getText();
			else if (bankAccountFromContent.getName().equals(ACCTID))
				accountId = ((SimpleValue) bankAccountFromContent).getText();
				
		return bankId + "\n" + accountId; //$NON-NLS-1$
	}
	
	private void parseBankTransactionList(List<DownloadedTransactionData> downloadedTransactions, ComplexValue bankTransactionList) {
		List<ComplexValue> bankTransactionListContents = bankTransactionList.getContent();
		for (ComplexValue bankTransactionListContent : bankTransactionListContents)
			if (bankTransactionListContent.getName().equals(STMTTRN))
				parseStatementTransaction(downloadedTransactions, bankTransactionListContent);
	}
	
	private Date parseDateTime(SimpleValue dateTimeValue) {
		try {
			return dateTimeFormatter.parse(dateTimeValue.getText());
		} catch (ParseException e) {
			return null;
		}
	}
	
	private void parseStatementTransaction(List<DownloadedTransactionData> downloadedTransactions, ComplexValue statementTransaction) {
		DownloadedTransactionData downloadedTransaction = new DownloadedTransactionData();
		
		List<ComplexValue> statementTransactionContents = statementTransaction.getContent();
		for (ComplexValue statementTransactionContent : statementTransactionContents) {

			if (statementTransactionContent.getName().equals(DTPOSTED))
				downloadedTransaction.postedDateTime = parseDateTime((SimpleValue) statementTransactionContent);
			else if (statementTransactionContent.getName().equals(TRNAMT))
				downloadedTransaction.amount = new BigDecimal(((SimpleValue) statementTransactionContent).getText());
			else if (statementTransactionContent.getName().equals(NAME))
				downloadedTransaction.description = ((SimpleValue) statementTransactionContent).getText();
		}
		
		downloadedTransactions.add(downloadedTransaction);
	}

	@Override
	public List<DownloadedTransactionData> parseDownloadedTransactions(BufferedReader reader) {
		SGMLParser parser = new SGMLParser();
		Document document = parser.parse(reader);
		
		List<DownloadedTransactionData> downloadedTransactions = new ArrayList<DownloadedTransactionData>();
		parseOFX(downloadedTransactions,  document.getRootValue());
		
		return downloadedTransactions;
	}
}
