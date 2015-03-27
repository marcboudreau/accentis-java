package ca.msbsoftware.accentis.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class RandomAccessFile extends java.io.RandomAccessFile {

	class RandomAccessFileInputStream extends InputStream {

		@Override
		public int read() throws IOException {
			return RandomAccessFile.this.read();
		}
	}

	private InputStream is;
	
	public RandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

	public RandomAccessFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}

	public InputStream getInputStream() {
		if (null == is)
			is = new RandomAccessFileInputStream();
		
		return is;
	}
}
