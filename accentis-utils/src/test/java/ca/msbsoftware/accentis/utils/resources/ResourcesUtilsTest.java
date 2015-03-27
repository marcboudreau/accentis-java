package ca.msbsoftware.accentis.utils.resources;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.msbsoftware.accentis.utils.resources.ResourcesUtils;

public class ResourcesUtilsTest {

	@Test
	public void verifyEmptyString() throws Exception {
		assertArrayEquals(new String[] { new String() }, ResourcesUtils.splitList(new String()));
	}

	@Test
	public void verifyStringWithNoDelimeter() throws Exception {
		final String value = "abcdef"; //$NON-NLS-1$

		assertArrayEquals(new String[] { value }, ResourcesUtils.splitList(value));
	}

	@Test
	public void verifyStringWithOneDelimiter() throws Exception {
		final String value1 = "abc"; //$NON-NLS-1$
		final String value2 = "def"; //$NON-NLS-1$

		assertArrayEquals(new String[] { value1, value2 },
				ResourcesUtils.splitList(value1 + "," + value2)); //$NON-NLS-1$
	}

	@Test
	public void verifyStringWithOneDelimiterAtBegin() throws Exception {
		final String value = "abcdef"; //$NON-NLS-1$

		assertArrayEquals(new String[] { new String(), value },
				ResourcesUtils.splitList("," + value)); //$NON-NLS-1$
	}

	@Test
	public void verifyStringWithOneDelimiterAtEnd() throws Exception {
		final String value = "abcdef"; //$NON-NLS-1$

		assertArrayEquals(new String[] { value, new String() },
				ResourcesUtils.splitList(value + ",")); //$NON-NLS-1$
	}

	@Test
	public void verifyStringWithEscapedDelimiter() throws Exception {
		final String value1 = "abc"; //$NON-NLS-1$
		final String value2 = "def"; //$NON-NLS-1$

		assertArrayEquals(new String[] { value1 + "," + value2 }, //$NON-NLS-1$
				ResourcesUtils.splitList(value1 + "\\," + value2)); //$NON-NLS-1$
	}

	@Test
	public void verifyStringWithEscapedDelimiterAtBeginning() throws Exception {
		final String value = "abcdef"; //$NON-NLS-1$

		assertArrayEquals(new String[] { "," + value }, //$NON-NLS-1$
				ResourcesUtils.splitList("\\," + value)); //$NON-NLS-1$
	}

	@Test
	public void verifyStringWithEscapedDelimiterAtEnd() throws Exception {
		final String value = "abcdef"; //$NON-NLS-1$

		assertArrayEquals(new String[] { value + "," }, //$NON-NLS-1$
				ResourcesUtils.splitList(value + "\\,")); //$NON-NLS-1$
	}

	@Test
	public void verifyStringWithDoubleEscapeAndDelimiter() throws Exception {
		final String value1 = "abc"; //$NON-NLS-1$
		final String value2 = "def"; //$NON-NLS-1$

		assertArrayEquals(new String[] { value1 + "\\", value2 }, //$NON-NLS-1$
				ResourcesUtils.splitList(value1 + "\\\\," + value2)); //$NON-NLS-1$
	}

	@Test
	public void verifyStringWithDoubleEscapeAndDelimiterAtBeginning()
			throws Exception {
		final String value = "abcdef"; //$NON-NLS-1$

		assertArrayEquals(new String[] { "\\", value }, //$NON-NLS-1$
				ResourcesUtils.splitList("\\\\," + value)); //$NON-NLS-1$
	}

	@Test
	public void verifyStringWithDoubleEscapeAndDelimiterAtEnd()
			throws Exception {
		final String value = "abcdef"; //$NON-NLS-1$

		assertArrayEquals(new String[] { value + "\\", new String() }, //$NON-NLS-1$
				ResourcesUtils.splitList(value + "\\\\,")); //$NON-NLS-1$
	}
}
