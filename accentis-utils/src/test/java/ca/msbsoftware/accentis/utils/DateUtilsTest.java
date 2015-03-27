package ca.msbsoftware.accentis.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ca.msbsoftware.accentis.utils.DateUtils;

public class DateUtilsTest {

	private Calendar calendar;

	@Before
	public void setUp() {
		calendar = Calendar.getInstance();
	}

	@Test
	public void verifyCreateDateAtMidnightWithMidnightDate() {
		verifyDateIsAtMidnight(DateUtils.createDateAtMidnight(new Date(0)));
	}

	@Test
	public void verifyCreateDateAtMidnightWithNonMidnightDate() {
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 15);
		calendar.set(Calendar.MINUTE, 38);
		calendar.set(Calendar.SECOND, 29);

		verifyDateIsAtMidnight(DateUtils.createDateAtMidnight(calendar.getTime()));
	}

	@Test
	public void verifyCreateTodayMidnight() {
		verifyDateIsAtMidnight(DateUtils.createTodayMidnight());
	}

	private void verifyDateIsAtMidnight(Date date) {
		calendar.setTime(date);

		assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, calendar.get(Calendar.MINUTE));
		assertEquals(0, calendar.get(Calendar.SECOND));
		assertEquals(0, calendar.get(Calendar.MILLISECOND));
	}

	@Test
	public void verifyParseValidDate() throws Exception {
		calendar.set(1969, 6, 21, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		final Date expected = calendar.getTime();

		assertEquals(expected, DateUtils.parse("7/21/1969 2:56")); //$NON-NLS-1$
	}
	
	@Test
	public void verifyParseNull() throws Exception {
		assertNull(DateUtils.parse(null));
	}
	
	@Test(expected=ParseException.class)
	public void verifyParseInvalidDate() throws Exception {
		DateUtils.parse("bigDog"); //$NON-NLS-1$
	}
	
	@Test
	public void verifyFormatNull() {
		assertNull(DateUtils.format(null));
	}
	
	@Test
	public void verifyFormatDate() {
		calendar.set(1969, 6, 21, 2, 57, 0);
		final Date date = calendar.getTime();
		
		assertEquals("7/21/69", DateUtils.format(date)); //$NON-NLS-1$
	}
}

