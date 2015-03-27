package ca.msbsoftware.accentis.gui.models;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.persistence.pojos.TransactionDetail;

import ca.msbsoftware.accentis.gui.models.TransactionWithBalanceTableModel;

public class TransactionTableModelTest {

	private TransactionWithBalanceTableModel model;
	
	private Account account = createAccount(10f);
	
	private static Account createAccount(float startingBalance) {
		Account account = new Account();
		account.setStartBalance(new BigDecimal(startingBalance));
		
		return account;
	}
	
	@Before
	public void setUp() {
		model = new TransactionWithBalanceTableModel();
		model.account = account;
	}
	
	private Transaction createSimpleTransaction(Date date, float amount) {
		Transaction transaction = new Transaction();
		transaction.setDate(date);
		
		TransactionDetail detail = new TransactionDetail();
		detail.setAccount(account);
		detail.setAmount(new BigDecimal(amount));
		
		transaction.getTransactionDetails().add(detail);
		
		return transaction;
	}
	
	@Test
	public void verifyObjectCreatedWhenListEmpty() {
		Transaction transaction = createSimpleTransaction(new Date(), 50f);
		
		model.getTransactionDataManagerListener().pojoCreated(transaction);
		
		assertEquals(1, model.getRowCount());
		assertEquals(transaction.getDate(), model.getValueAt(0, 0));
	}
	
	@Test
	public void verifyObjectCreatedWhenItAlreadyExistsInList() {
		Transaction transaction = createSimpleTransaction(new Date(), 50f);
		
		model.getTransactionDataManagerListener().pojoCreated(transaction);
		model.getTransactionDataManagerListener().pojoCreated(transaction);
		
		assertEquals(1, model.getRowCount());
	}
	
	@Test
	public void verifyObjectCreatedWithZeroValueWhenItAlreadyExistsInList() {
		Transaction transaction = createSimpleTransaction(new Date(), 0f);
		
		model.getTransactionDataManagerListener().pojoCreated(transaction);
		model.getTransactionDataManagerListener().pojoCreated(transaction);
		
		assertEquals(1, model.getRowCount());
	}
	
	@Test
	public void verifyObjectCreatedWithZeroValueWhenItAlreadyExistsInListWithOthers() {
		Transaction transaction1 = createSimpleTransaction(new Date(), 35.46f);
		Transaction transaction2 = createSimpleTransaction(new Date(System.currentTimeMillis() - 10000), 0f);
		
		model.getTransactionDataManagerListener().pojoCreated(transaction1);
		model.getTransactionDataManagerListener().pojoCreated(transaction2);
		model.getTransactionDataManagerListener().pojoCreated(transaction2);
		
		assertEquals(2, model.getRowCount());
	}

	@Test
	public void verifyObjectCreatedWhenItAlreadyExistsInListWithOthers() {
		Transaction transaction1 = createSimpleTransaction(new Date(), 35.46f);
		Transaction transaction2 = createSimpleTransaction(new Date(System.currentTimeMillis() - 10000), 27.54f);
		
		model.getTransactionDataManagerListener().pojoCreated(transaction1);
		model.getTransactionDataManagerListener().pojoCreated(transaction2);
		model.getTransactionDataManagerListener().pojoCreated(transaction2);
		
		assertEquals(2, model.getRowCount());
	}
	
	@Test
	public void verifyObjectRemovedWhenNotInList() {
		Transaction transaction = createSimpleTransaction(new Date(), 125.46f);
		
		model.getTransactionDataManagerListener().pojoDeleted(transaction);
		
		assertEquals(0, model.getRowCount());
	}
	
	@Test
	public void verifyObjectRemovedWhenOnlyOneInList() {
		Transaction transaction = createSimpleTransaction(new Date(), 125.46f);

		model.getTransactionDataManagerListener().pojoCreated(transaction);
		model.getTransactionDataManagerListener().pojoDeleted(transaction);
		
		assertEquals(0, model.getRowCount());
	}
	
	@Test
	public void verifyObjectRemovedWhenNotLastInList() {
		Transaction transaction1 = createSimpleTransaction(new Date(), 125.46f);
		Transaction transaction2 = createSimpleTransaction(new Date(System.currentTimeMillis() + 5000), 495f);
		Transaction transaction3 = createSimpleTransaction(new Date(System.currentTimeMillis() + 8000), 23f);

		model.getTransactionDataManagerListener().pojoCreated(transaction1);
		model.getTransactionDataManagerListener().pojoCreated(transaction2);
		model.getTransactionDataManagerListener().pojoCreated(transaction3);
		model.getTransactionDataManagerListener().pojoDeleted(transaction1);
		
		assertEquals(2, model.getRowCount());
		assertEquals(0, new BigDecimal(528f).compareTo((BigDecimal) model.getValueAt(1, 4)));
	}
}
