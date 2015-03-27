package ca.msbsoftware.accentis.ofxparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Test;

import ca.msbsoftware.accentis.ofxparser.OFXReader;

public class OFXReaderTest {

	@Test
	public void verifyDetermineOFXVersionWithEmptyReader() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader(new String()));
		
		assertNull(OFXReader.determineOFXVersion(reader));
	}
	
	@Test
	public void verifyDetermineOFXVersionFromSGMLWithNoVersionSpecified() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("OFXHEADER:100\n\n<OFX></OFX>")); //$NON-NLS-1$
		
		assertNull(OFXReader.determineOFXVersion(reader));
	}
	
	@Test
	public void verifyDetermineOFXVersionFromSGMLWithVersionSpecified() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("OFXHEADER:100\nVERSION:102\n\n<OFX></OFX>")); //$NON-NLS-1$
		
		assertEquals("102", OFXReader.determineOFXVersion(reader)); //$NON-NLS-1$
	}
	
	@Test
	public void verifyDetermineOFXVersionFromXML() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?OFX HEADER=\"200\" VERSION=\"203\"?>\n<OFX></OFX>")); //$NON-NLS-1$
		
		assertEquals("203", OFXReader.determineOFXVersion(reader)); //$NON-NLS-1$
	}
	
	@Test
	public void verifyDetermineOFXVersionFromSGMLWithVersionInOtherContext() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("OFXHEADER:100\nVERSIONA:100\n\n<OFX></OFX>")); //$NON-NLS-1$
		
		assertNull(OFXReader.determineOFXVersion(reader));
	}
}
