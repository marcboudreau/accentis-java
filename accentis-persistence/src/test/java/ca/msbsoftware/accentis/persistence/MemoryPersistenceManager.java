package ca.msbsoftware.accentis.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Persistence;

public class MemoryPersistenceManager extends PersistenceManager {

	public MemoryPersistenceManager() {
		super(Persistence.createEntityManagerFactory("accentis", createPersistenceUnitProperties()));
	}
	
	private static Map<String, String> createPersistenceUnitProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("openjpa.ConnectionURL", "jdbc:derby:memory:accentis;create=true");
		
		return properties;
	}
}
