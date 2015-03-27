package ca.msbsoftware.accentis.ofxparser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import ca.msbsoftware.accentis.ofxparser.OFXParserFactory;
import ca.msbsoftware.accentis.ofxparser.OFXParserFactory.JarFilenameFilter;



public class OFXParserFactoryTest {

	@Test
	public void verifyOFXParserFactoryConstructor() throws Exception {
		assertNotNull(new OFXParserFactory());
	}
	
	@Test
	public void verifyJarFilenameFilterAcceptNotMatching() throws Exception {
		JarFilenameFilter filter = new JarFilenameFilter();
		assertFalse(filter.accept(new File("."), "dog.sh")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Test
	public void verifyJarFilenameFilterAcceptJarFile() throws Exception {
		JarFilenameFilter filter = new JarFilenameFilter();
		assertTrue(filter.accept(new File("."), "abc.jar")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Test
	public void verifyJarFilenameFilterAcceptZipFile() throws Exception {
		JarFilenameFilter filter = new JarFilenameFilter();
		assertTrue(filter.accept(new File("."), "def.zip")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Test
	public void verifyFindClass() throws Exception {
		OFXParserFactory factory = new OFXParserFactory();
		
		assertNull(factory.findParser("102")); //$NON-NLS-1$
	}
}
