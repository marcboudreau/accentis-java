package ca.msbsoftware.accentis.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.msbsoftware.accentis.io.RandomAccessFile;


public class RandomAccessFileTest {

	private File file;
	
	@Before
	public void setUp() throws Exception {
		file = File.createTempFile("wetutil", ".tmp"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@After
	public void tearDown() throws Exception {
		if (!file.delete())
			file.deleteOnExit();
	}
	
	@Test
	public void testGetInputStreamOnEmptyFile() throws Exception {
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"); //$NON-NLS-1$
		InputStream is = randomAccessFile.getInputStream();
		
		assertNotNull(is);
		assertStreamContains(new int[0], is);
		
		randomAccessFile.close();
	}

	@Test
	public void testGetInputStreamOnFileWithContent() throws Exception {
		final int[] data = { 11, 15, 19 };
		
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw"); //$NON-NLS-1$
		for (int i : data)
			randomAccessFile.write(i);
		randomAccessFile.seek(0);
		
		InputStream is = randomAccessFile.getInputStream();
		assertStreamContains(data, is);
		
		randomAccessFile.close();
	}
	
	@Test
	public void testConstructorWithFilename() throws Exception {
		RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "r"); //$NON-NLS-1$
		InputStream is = randomAccessFile.getInputStream();
		
		assertNotNull(is);
		assertStreamContains(new int[0], is);
		
		randomAccessFile.close();
	}
	
	@Test
	public void testGetInputStreamAlwaysReturnsSameInputStream() throws Exception {
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"); //$NON-NLS-1$
		InputStream is = randomAccessFile.getInputStream();
		
		assertSame(is, randomAccessFile.getInputStream());
		
		randomAccessFile.close();
	}
	
	private static void assertStreamContains(int[] expected, InputStream stream) throws Exception {
		for (int i : expected)
			assertEquals(i, stream.read());
		
		assertEquals(-1, stream.read());
	}
}
