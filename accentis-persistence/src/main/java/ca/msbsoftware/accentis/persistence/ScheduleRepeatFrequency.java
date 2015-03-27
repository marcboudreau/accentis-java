package ca.msbsoftware.accentis.persistence;


public enum ScheduleRepeatFrequency {

	EVERY(1), EVERY_OTHER(2);

	private int factor;
	
	private ScheduleRepeatFrequency(int factor) {
		this.factor = factor;
	}
	
	public int getFactor() {
		return factor;
	}
	
	@Override
	public String toString() {
		return Resources.getInstance().getString("enumeration.schedulerepeatfrequency." + name().toLowerCase()); //$NON-NLS-1$
	}
}
