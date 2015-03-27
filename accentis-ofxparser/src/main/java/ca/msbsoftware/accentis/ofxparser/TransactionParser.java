package ca.msbsoftware.accentis.ofxparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileLock;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;

import ca.msbsoftware.accentis.utils.HomeDirectory;
import ca.msbsoftware.accentis.utils.logging.AccentisLogger;

public class TransactionParser {

	private File serializedDataFile;

	private IOFXReader ofxReader;

	TransactionParser() {
	}

	public void parse(File file) {
		List<DownloadedTransactionData> downloadedTransactions = getOFXReader().readOFXData(file);
		writeDownloadedTransactionsToTemporaryFile(downloadedTransactions);
	}

	public IOFXReader getOFXReader() {
		if (null == ofxReader)
			createDefaultOFXReader();

		return ofxReader;
	}
	
	private void createDefaultOFXReader() {
		ofxReader = new OFXReader();
	}

	public void setOFXReader(IOFXReader reader) {
		ofxReader = reader;
	}

	private void writeDownloadedTransactionsToTemporaryFile(List<DownloadedTransactionData> downloadedTransactions) {
		try {
			FileOutputStream fos = new FileOutputStream(getSerializedDataFile());
			FileLock lock = getFileLock(fos);
			writeDownloadedTransactionsToLockedFile(downloadedTransactions, fos, lock);
		} catch (FileNotFoundException ex) {
			AccentisLogger.getLogger().log(Level.SEVERE, MessageFormat.format(Resources.getInstance().getString("message.transactionparser.cantopenfileforwriting"), getSerializedDataFile().getPath()), ex); //$NON-NLS-1$
		}
	}

	static FileLock getFileLock(FileOutputStream fos) {
		try {
			return fos.getChannel().lock();
		} catch (IOException ex) {
			AccentisLogger.getLogger().log(Level.SEVERE, Resources.getInstance().getString("message.transactionparser.failtolockfile"), ex); //$NON-NLS-1$
		}

		return null;
	}

	private static void writeDownloadedTransactionsToLockedFile(List<DownloadedTransactionData> downloadedTransactions,
			FileOutputStream fos, FileLock lock) {
		try {
			writeDownloadedTransactionsToStream(downloadedTransactions, fos);
			lock.release();
			fos.close();
		} catch (IOException ex) {
			AccentisLogger.getLogger().log(Level.SEVERE, Resources.getInstance().getString("message.transactionparser.downloadedbankstatement.storefailed"), ex); //$NON-NLS-1$
		}
	}

	static void writeDownloadedTransactionsToStream(List<DownloadedTransactionData> downloadedTransactions,
			OutputStream os) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(os);
		for (DownloadedTransactionData downloadedTransaction : downloadedTransactions)
			oos.writeObject(downloadedTransaction);

		oos.flush();
	}

	File getSerializedDataFile() {
		if (null == serializedDataFile)
			createSerializedDataFile();
		
		return serializedDataFile;
	}
	
	private void createSerializedDataFile() {
		serializedDataFile = new File(HomeDirectory.getAccentisHomeTempDirectory(), "importedtransactions.ser"); //$NON-NLS-1$
	}
	
	void setSerializedDataFile(File file) {
		serializedDataFile = file;
	}

	private static void displayNotEnoughArgumentsError() {
		AccentisLogger.getLogger().severe(Resources.getInstance().getString("message.transactionparser.missingargument")); //$NON-NLS-1$
	}

	public static void main(String[] args) {
		if (1 > args.length) {
			displayNotEnoughArgumentsError();
			System.exit(-1);
		}

		TransactionParser transactionParser = new TransactionParser();
		for (String arg : args)
			transactionParser.parse(new File(arg));
	}
}
