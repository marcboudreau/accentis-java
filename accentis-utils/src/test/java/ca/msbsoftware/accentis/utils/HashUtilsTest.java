package ca.msbsoftware.accentis.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.msbsoftware.accentis.utils.HashUtil;

public class HashUtilsTest {

	@Test
	public void verifyHash() {
		assertTrue(HashUtil.hash(0) == HashUtil.hash(0));
		assertFalse(HashUtil.hash(-3956) == HashUtil.hash(3956));
	}
	
	@Test
	public void verifyDigestIntoHexWithEmptyArray() {
		assertFalse(HashUtil.digestIntoHex(new char[0]).isEmpty());
	}
	
	@Test
	public void verifyDigestIntoHex() {
		assertFalse(HashUtil.digestIntoHex("Test String".toCharArray()).isEmpty()); //$NON-NLS-1$
	}
}
