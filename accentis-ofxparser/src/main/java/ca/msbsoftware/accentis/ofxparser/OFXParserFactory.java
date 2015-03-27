package ca.msbsoftware.accentis.ofxparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import ca.msbsoftware.accentis.utils.HomeDirectory;
import ca.msbsoftware.accentis.utils.logging.AccentisLogger;

public class OFXParserFactory implements IOFXParserFactory {

	private Map<String, Class<? extends OFXParser>> parserClasses;

	static class JarFilenameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jar") || name.endsWith(".zip"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private final JarFilenameFilter JAR_FILE_FILENAMEFILTER = new JarFilenameFilter();

	public OFXParserFactory() {
		loadRegisteredParsers();
	}

	@Override
	public OFXParser findParser(String version) {
		Class<? extends OFXParser> klass = parserClasses.get(version);
		if (null != klass) {
			try {
				return klass.newInstance();
			} catch (InstantiationException ex) {
				AccentisLogger.getLogger().log(Level.WARNING,
						MessageFormat.format(Resources.getInstance().getString("message.ofxparserfactory.failedtoinstantiate.wrongtype"), klass.getName()), ex); //$NON-NLS-1$
			} catch (IllegalAccessException ex) {
				AccentisLogger.getLogger().log(
						Level.WARNING,
						MessageFormat.format(
								Resources.getInstance().getString("message.ofxparserfactory.failedtoinstantiate.constructorinaccessible"), klass.getName()), ex); //$NON-NLS-1$
			}
		}

		return null;
	}

	private void loadRegisteredParsers() {
		parserClasses = new HashMap<String, Class<? extends OFXParser>>();

		File ofxDirectory = new File(HomeDirectory.getAccentisHomeDirectory(), "ofx"); //$NON-NLS-1$
		File[] jarFiles = ofxDirectory.listFiles(JAR_FILE_FILENAMEFILTER);
		searchJarFiles(jarFiles);
	}

	private void searchJarFiles(File[] files) {
		if (null == files)
			return;

		for (File file : files)
			searchJarFile(file);
	}

	private void searchJarFile(File file) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);
			ZipEntry entry = jarFile.getEntry("META-INF/parser.ofx"); //$NON-NLS-1$
			if (null != entry) {
				readZipEntry(jarFile.getInputStream(entry), file);
			}
		} catch (ZipException ex) {
			AccentisLogger.getLogger().log(Level.WARNING,
					MessageFormat.format(Resources.getInstance().getString("message.ofxparserfactory.filecorrupt"), file.getPath()), ex); //$NON-NLS-1$
		} catch (IOException ex) {
			AccentisLogger.getLogger().log(Level.WARNING,
					MessageFormat.format(Resources.getInstance().getString("message.ofxparserfactory.ioexceptionreadingfile"), file.getPath()), ex); //$NON-NLS-1$
		} finally {
			if (null != jarFile)
				try {
					jarFile.close();
				} catch (IOException ex) {
					
				}
		}
	}

	private void readZipEntry(InputStream stream, File jarFile) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			String line = reader.readLine();
			while (null != line) {
				int pos = line.indexOf(':');
				if (-1 < pos) {
					String version = line.substring(0, pos);
					String className = line.substring(pos + 1);

					Class<? extends OFXParser> klass = loadParserClass(className, jarFile);
					if (null != klass)
						parserClasses.put(version, klass);
				}
				line = reader.readLine();
			}
		} catch (IOException ex) {
			AccentisLogger.getLogger().log(Level.WARNING,
					MessageFormat.format(Resources.getInstance().getString("message.ofxparserfactory.ioexceptionreadingparserofx"), jarFile.getPath())); //$NON-NLS-1$
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends OFXParser> loadParserClass(String className, File jarFile) {
		URLClassLoader urlClassLoader = new URLClassLoader(createURLArrayFromFile(jarFile));
		try {
			Class<?> klass = Class.forName(className, true, urlClassLoader);
			if (OFXParser.class.isAssignableFrom(klass))
				return (Class<? extends OFXParser>) klass;
		} catch (ClassNotFoundException ex) {
			AccentisLogger.getLogger().log(Level.WARNING,
					MessageFormat.format(Resources.getInstance().getString("message.ofxparserfactory.couldnotlocateclass"), className), ex); //$NON-NLS-1$
		}

		return null;
	}

	private static URL[] createURLArrayFromFile(File file) {
		try {
			URL[] urls = new URL[1];
			urls[0] = file.toURI().toURL();
			return urls;
		} catch (MalformedURLException ex) {
			AccentisLogger.getLogger().log(Level.WARNING,
					MessageFormat.format(Resources.getInstance().getString("message.ofxparserfactory.failedtoconvertpathtourl"), file.getPath()), ex); //$NON-NLS-1$
		}

		return null;
	}

}
