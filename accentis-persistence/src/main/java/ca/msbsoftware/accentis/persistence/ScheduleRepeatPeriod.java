package ca.msbsoftware.accentis.persistence;

import java.util.Calendar;

public enum ScheduleRepeatPeriod {

	DAY(Calendar.DAY_OF_YEAR), WEEK(Calendar.WEEK_OF_YEAR), MONTH(Calendar.MONTH), YEAR(Calendar.YEAR);
	
	private int field;
	
	private ScheduleRepeatPeriod(int field) {
		this.field = field;
	}
	
	public int getCalendarField() {
		return field;
	}
	
	@Override
	public String toString() {
		return Resources.getInstance().getString("enumeration.schedulerepeatperiod." + name().toLowerCase()); //$NON-NLS-1$
	}
}
