package ca.msbsoftware.accentis.importer;

import static org.easymock.EasyMock.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.database.MemoryDatabase;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.BankAccountIdMapping;
import ca.msbsoftware.accentis.persistence.pojos.DownloadedTransaction;
import ca.msbsoftware.accentis.importer.IBankAccountIdMappingHandler;
import ca.msbsoftware.accentis.importer.TransactionImporter;
import ca.msbsoftware.accentis.ofxparser.DownloadedTransactionData;

public class TransactionImporterTest {

//	private static final String EXISTING_BANKACCOUNTID = "1234567"; //$NON-NLS-1$
//
//	private static final Account EXISTING_ACCOUNT = new Account();
//
//	private TransactionImporter importer;
//
//	private PersistenceManager persistenceManager;
//
//	@Before
//	public void setUp() throws Exception {
//		persistenceManager = new PersistenceManager(new MemoryDatabase(), null);
//		importer = new TransactionImporter(new IBankAccountIdMappingHandler() {
//			@Override
//			public Account handleMapping(String bankAccountId) {
//				return new Account();
//			}
//		});
//	}

//	private PersistenceManager setupPersistenceManager(BankAccountIdMapping mapping) {
//		mockDataManager = createMock(IDataManager.class);
//
//		expect(
//				mockDataManager.runSingleResultNamedQuery(
//						"BankAccountIdMapping.findMapping", QueryParameterMap.createSingleParameterMap("bankAccountId", EXISTING_BANKACCOUNTID))).andReturn(mapping); //$NON-NLS-1$ //$NON-NLS-2$
//
//		expect(mockDataManager.persist(anyObject(DownloadedTransaction.class))).andReturn(null);
//
//		replay(mockDataManager);
//
//		Database database = new FakeDatabase(mockDataManager);
//		return database;
//	}
	
//	private Database setupDatabaseForExistingMapping() {
//		BankAccountIdMapping mapping = new BankAccountIdMapping();
//		mapping.setBankAccountId(EXISTING_BANKACCOUNTID);
//		mapping.setAccount(EXISTING_ACCOUNT);
//
//		return setupDatabase(mapping);
//	}

//	@Test
//	public void verifyImportDownloadedTransactionWithExistingBankAccountIdMapping() {
//		importer.persistenceManagerChanged(persistenceManager);
//
//		DownloadedTransactionData downloadedTransactionData = new DownloadedTransactionData();
//		downloadedTransactionData.amount = new BigDecimal(123.45);
//		downloadedTransactionData.bankAccountId = "1234567"; //$NON-NLS-1$
//		downloadedTransactionData.currency = Currency.getInstance(Locale.getDefault());
//		downloadedTransactionData.description = "Test Downloaded Transaction"; //$NON-NLS-1$
//		downloadedTransactionData.postedDateTime = new Date();
//
//		importer.importDownloadedTransaction(downloadedTransactionData);
//	}

//	private Database setupDataManagerForNonExistingMapping() {
//		return setupDatabase(null);
//	}

//	@Test
//	public void verifyImportDownloadedTransactionWithNonExistingBankAccountIdMapping() {
//		importer.databaseChanged(setupDataManagerForNonExistingMapping());
//
//		DownloadedTransactionData downloadedTransactionData = new DownloadedTransactionData();
//		downloadedTransactionData.amount = new BigDecimal(123.45);
//		downloadedTransactionData.bankAccountId = "1234567"; //$NON-NLS-1$
//		downloadedTransactionData.currency = Currency.getInstance(Locale.getDefault());
//		downloadedTransactionData.description = "Test Downloaded Transaction"; //$NON-NLS-1$
//		downloadedTransactionData.postedDateTime = new Date();
//
//		importer.importDownloadedTransaction(downloadedTransactionData);
//
//		verify(mockDataManager);
//	}

//	private class FakeDatabase extends Database {
//
//		FakeDatabase(IDataManager dm) {
//			super(dm);
//		}
//	}
}
