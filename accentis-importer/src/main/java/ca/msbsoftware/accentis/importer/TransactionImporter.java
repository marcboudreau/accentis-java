package ca.msbsoftware.accentis.importer;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.FileLock;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.BankAccountIdMapping;
import ca.msbsoftware.accentis.persistence.pojos.DownloadedTransaction;
import ca.msbsoftware.accentis.io.RandomAccessFile;
import ca.msbsoftware.accentis.ofxparser.DownloadedTransactionData;
import ca.msbsoftware.accentis.utils.logging.AccentisLogger;

public class TransactionImporter extends TimerTask implements IPersistenceManagerListener {

	private PersistenceManager persistenceManager;

	private File directory;

	private IBankAccountIdMappingHandler handler;
	
	public TransactionImporter(IBankAccountIdMappingHandler mappingHandler) {
		handler = mappingHandler;
	}
	
	@Override
	public void run() {
		if (null != persistenceManager)
			try {
				RandomAccessFile file = new RandomAccessFile(new File(directory, "importedtransactions.ser"), "rwd"); //$NON-NLS-1$ //$NON-NLS-2$
				FileLock lock = file.getChannel().lock();

				ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(file.getInputStream());
					while (true) {
						DownloadedTransactionData downloadedTransaction = (DownloadedTransactionData) ois.readObject();
						importDownloadedTransaction(downloadedTransaction);
					}
				} catch (EOFException ex) {
					/* Gobble this exception up. */
				} finally {
					if (null != ois)
						ois.close();

					file.setLength(0);
					lock.release();
					file.close();
				}
			} catch (FileNotFoundException ex) {
				AccentisLogger.getLogger().log(Level.WARNING, Resources.getInstance().getString("importer.location.readonly"), ex); //$NON-NLS-1$
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException ex) {
				AccentisLogger.getLogger().log(Level.SEVERE,
						Resources.getInstance().getString("importer.incompatibleversions"), ex); //$NON-NLS-1$
			}
	}

	void importDownloadedTransaction(DownloadedTransactionData downloadedTransaction) {
		BankAccountIdMapping mapping = null;
		List<BankAccountIdMapping> results = persistenceManager.get(BankAccountIdMapping.GET_MAPPING_FOR_ACCOUNT_QUERY, BankAccountIdMapping.class, PersistenceManager.createQueryParameterMap("bankAccountId", downloadedTransaction.bankAccountId)); //$NON-NLS-1$
		if (0 == results.size())
			mapping = createMapping(downloadedTransaction.bankAccountId);

		DownloadedTransaction transaction = new DownloadedTransaction();
		transaction.setAmount(downloadedTransaction.amount);
		transaction.setPostedDateTime(downloadedTransaction.postedDateTime);
		transaction.setDescription(downloadedTransaction.description);
		transaction.setName(downloadedTransaction.description);
		transaction.setBankAccountId(mapping);

		persistenceManager.create(transaction);
	}

	private BankAccountIdMapping createMapping(String bankAccountId) {
		BankAccountIdMapping mapping = new BankAccountIdMapping();
		
		Account account = handler.handleMapping(bankAccountId);
		
		mapping.setBankAccountId(bankAccountId);
		mapping.setAccount(account);

		return mapping;
	}

	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		if (null != persistenceManager)
			this.persistenceManager = persistenceManager;
	}
}
