package ca.msbsoftware.accentis.ofxparser.resources;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ca.msbsoftware.accentis.ofxparser.Resources;

public class MessagesTest {

	@Test
	public void verifyGetInstance() throws Exception {
		assertNotNull(Resources.getInstance());
	}
}
