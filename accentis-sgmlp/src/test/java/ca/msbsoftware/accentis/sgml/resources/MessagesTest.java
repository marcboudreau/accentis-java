package ca.msbsoftware.accentis.sgml.resources;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ca.msbsoftware.accentis.sgml.resources.Resources;

public class MessagesTest {

	@Test
	public void verifyGetInstance() throws Exception {
		assertNotNull(Resources.getInstance());
	}
}
