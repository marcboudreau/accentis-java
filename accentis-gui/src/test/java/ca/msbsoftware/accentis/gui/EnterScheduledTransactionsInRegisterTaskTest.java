package ca.msbsoftware.accentis.gui;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.ScheduleRepeatFrequency;
import ca.msbsoftware.accentis.persistence.ScheduleRepeatPeriod;
import ca.msbsoftware.accentis.persistence.pojos.Payee;
import ca.msbsoftware.accentis.persistence.pojos.ScheduledTransaction;

import ca.msbsoftware.accentis.gui.EnterScheduledTransactionsInRegisterTask;

public class EnterScheduledTransactionsInRegisterTaskTest {

//	private IDataManager mockDataManager;
//
//	private EnterScheduledTransactionsInRegisterTask task;
//
//	@Before
//	public void setUp() {
//		mockDataManager = createMock(IDataManager.class);
//
//		task = new EnterScheduledTransactionsInRegisterTask();
//	}
//
//	@Test
//	public void verifyNoScheduledTransactions() {
//		expect(mockDataManager.<ScheduledTransaction> runNamedQuery("ScheduledTransaction.getAllScheduledTransactionsToEnterInRegister")).andReturn(new ArrayList<ScheduledTransaction>()); //$NON-NLS-1$
//		replay(mockDataManager);
//
//		task.setDataManager(mockDataManager);
//		task.run();
//
//		verify(mockDataManager);
//	}
//
//	@Test
//	public void verifyScheduledTransactionWithoutAutoEntryInRegister() {
//		List<ScheduledTransaction> list = new ArrayList<ScheduledTransaction>();
//		ScheduledTransaction transaction = new ScheduledTransaction();
//		transaction.setTransactionDescription("TestTransactionDescription"); //$NON-NLS-1$
//		transaction.setTransactionPayee(new Payee());
//		transaction.setTransactionReference("TestTransactionReference"); //$NON-NLS-1$
//		transaction.getSchedule().setEndsOccurrences(10);
//		transaction.getSchedule().setRepeatFrequency(ScheduleRepeatFrequency.EVERY);
//		transaction.getSchedule().setRepeatPeriod(ScheduleRepeatPeriod.MONTH);
//		transaction.getSchedule().setStartingDate(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 30));
//		list.add(transaction);
//		expect(mockDataManager.<ScheduledTransaction> runNamedQuery("ScheduledTransaction.getAllScheduledTransactionsToEnterInRegister")).andReturn(list); //$NON-NLS-1$
//		replay(mockDataManager);
//
//		task.setDataManager(mockDataManager);
//		task.run();
//
//		verify(mockDataManager);
//	}
}
