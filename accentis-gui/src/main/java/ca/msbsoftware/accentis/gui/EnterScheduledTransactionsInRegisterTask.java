package ca.msbsoftware.accentis.gui;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import ca.msbsoftware.accentis.persistence.listeners.IPersistenceManagerListener;
import ca.msbsoftware.accentis.persistence.pojos.Schedule;
import ca.msbsoftware.accentis.persistence.pojos.ScheduledTransaction;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.persistence.pojos.TransactionDetail;
import ca.msbsoftware.accentis.persistence.utils.ScheduleHelper;
import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.utils.DateUtils;

public class EnterScheduledTransactionsInRegisterTask extends TimerTask implements IPersistenceManagerListener {

	private PersistenceManager persistenceManager;
	
	public EnterScheduledTransactionsInRegisterTask() {
		super();
		
		GUIApplication.getInstance().addPersistenceManagerListener(this);
	}
	
	@Override
	public void run() {
		if (null == persistenceManager)
			return;
		
		List<ScheduledTransaction> results = persistenceManager.get("ScheduledTransaction.getAllScheduledTransactionsToEnterInRegister", ScheduledTransaction.class, PersistenceManager.EMPTY_PARAMETER_MAP); //$NON-NLS-1$
		for (ScheduledTransaction transaction : results)
			processScheduledTransaction(transaction);
	}
	
	protected void processScheduledTransaction(ScheduledTransaction transaction) {
		Integer advancedDays = transaction.getEnterInRegisterAdvancedDays();
		if (null == advancedDays)
			return;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.createTodayMidnight());
		calendar.add(Calendar.DAY_OF_YEAR, -advancedDays);
		final Date thresholdDate = calendar.getTime();
		
		Date date = ScheduleHelper.calculateNextDate(transaction.getLastEntryDate(), transaction);
		while (!date.after(thresholdDate) && !haveMaximumOccurrencesHappened(transaction.getSchedule(), transaction.getEntryCount())) {
			enterTransactionInRegister(transaction, date);
			
			date = ScheduleHelper.calculateNextDate(date, transaction);
		}
	}
	
	private boolean haveMaximumOccurrencesHappened(Schedule schedule, int entryCount) {
		Integer occurrences = schedule.getEndsOccurrences();
		if (null == occurrences)
			return false;
		
		return occurrences.intValue() <= entryCount;
	}
	
	protected void enterTransactionInRegister(ScheduledTransaction transaction, Date date) {
		Transaction enteredTransaction = new Transaction();
		enteredTransaction.setDate(date);
		enteredTransaction.setDescription(transaction.getTransactionDescription());
		enteredTransaction.setPayee(transaction.getTransactionPayee());
		enteredTransaction.setReference(transaction.getTransactionReference());
		for (TransactionDetail detail : transaction.getTransactionDetails())
			enteredTransaction.getTransactionDetails().add(detail);
		
		persistenceManager.create(enteredTransaction);
		
		transaction.setLastEntryDate(date);
		persistenceManager.save(transaction);
	}

	@Override
	public void persistenceManagerChanged(PersistenceManager persistenceManager) {
		if (null != persistenceManager)
			this.persistenceManager = persistenceManager;
	}

	void setPersistenceManager(PersistenceManager value) {
		persistenceManager = value;
	}
}
