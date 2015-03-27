package ca.msbsoftware.accentis.persistence.pojos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ca.msbsoftware.accentis.persistence.ScheduleRepeatFrequency;
import ca.msbsoftware.accentis.persistence.ScheduleRepeatPeriod;
import ca.msbsoftware.accentis.utils.HashUtil;

/**
 * 
 * 
 * @author Marc Boudreau
 *
 * @since 1.0
 */
@SuppressWarnings("serial")
@Embeddable
public class Schedule implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date startingDate;

	@Enumerated(EnumType.ORDINAL)
	private ScheduleRepeatFrequency repeatFrequency;

	@Enumerated(EnumType.ORDINAL)
	private ScheduleRepeatPeriod repeatPeriod;

	@Temporal(TemporalType.DATE)
	private Date endsOnDate;

	@Basic
	private Integer endsOccurrences;

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date value) {
		startingDate = value;
	}

	public ScheduleRepeatFrequency getRepeatFrequency() {
		return repeatFrequency;
	}

	public void setRepeatFrequency(ScheduleRepeatFrequency value) {
		repeatFrequency = value;
	}

	public ScheduleRepeatPeriod getRepeatPeriod() {
		return repeatPeriod;
	}

	public void setRepeatPeriod(ScheduleRepeatPeriod value) {
		repeatPeriod = value;
	}

	public Date getEndsOnDate() {
		return endsOnDate;
	}

	public void setEndsOnDate(Date value) {
		if (null != value)
			endsOccurrences = null;

		endsOnDate = value;
	}

	public Integer getEndsOccurrences() {
		return endsOccurrences;
	}

	public void setEndsOccurrences(Integer value) {
		if (null != value)
			endsOnDate = null;

		endsOccurrences = value;
	}

	@Override
	public boolean equals(Object o) {
		if (super.equals(o))
			return true;
		else if (null != o && o.getClass() == Schedule.class) {
			Schedule other = (Schedule) o;
			return compareObjects(startingDate, other.startingDate) && compareObjects(repeatFrequency, other.repeatFrequency)
					&& compareObjects(repeatPeriod, other.repeatPeriod) && compareEndsFields(other);
		}

		return false;
	}
	
	private boolean compareEndsFields(Schedule s) {
		if (null != endsOccurrences)
			return compareObjects(endsOccurrences, s.endsOccurrences);
		
		return compareObjects(endsOnDate, s.endsOnDate);
	}

	private static boolean compareObjects(Object o1, Object o2) {
		return null == o1 ? null == o2 : o1.equals(o2);
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(objectHashCode(startingDate));
		hashCode ^= HashUtil.hash(objectHashCode(endsOnDate));
		hashCode ^= HashUtil.hash(objectHashCode(endsOccurrences));
		hashCode ^= HashUtil.hash(objectHashCode(repeatPeriod));
		hashCode ^= HashUtil.hash(objectHashCode(repeatFrequency));
		
		return hashCode;
	}
	
	private static int objectHashCode(Object o) {
		return null == o ? 0 : o.hashCode();
	}
}