package ca.msbsoftware.accentis.persistence.utils;

import java.util.Calendar;
import java.util.Date;

import ca.msbsoftware.accentis.persistence.pojos.Schedule;
import ca.msbsoftware.accentis.persistence.pojos.ScheduledTransaction;

public class ScheduleHelper {

	public static Date calculateNextDate(Date fromDate, ScheduledTransaction scheduledTransaction) {
		Schedule schedule = scheduledTransaction.getSchedule();
		
		if (null == fromDate)
			return schedule.getStartingDate();
		
		Calendar calendar = Calendar.getInstance();
		int factor = schedule.getRepeatFrequency().getFactor();
		int field = schedule.getRepeatPeriod().getCalendarField();
		Date date = schedule.getStartingDate();
		
		while (date.before(fromDate)) {
			calendar.setTime(date);
			calendar.add(field, factor);
			date = calendar.getTime();
			
			if (isDatePastEndOfSchedule(date, schedule)) {
				date = null;
				break;
			}
		}
		
		return date;
	}
	
	private static boolean isDatePastEndOfSchedule(Date date, Schedule schedule) {
		Date endsOnDate = schedule.getEndsOnDate();
		if (null != endsOnDate)
			return endsOnDate.before(date);
		
		return false;
	}
}
