package ca.msbsoftware.accentis.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.msbsoftware.accentis.persistence.database.Database;
import ca.msbsoftware.accentis.persistence.database.DatabaseException;
import ca.msbsoftware.accentis.persistence.database.DatabaseWrongPasswordException;
import ca.msbsoftware.accentis.persistence.listeners.IPojoListener;
import ca.msbsoftware.accentis.persistence.pojos.BaseObject;

public class PersistenceManager {

	private EntityManagerFactory entityManagerFactory;
	
	private Database database;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ThreadLocal<EntityManager> entityManagers = new ThreadLocal<EntityManager>() {
		@Override
		protected EntityManager initialValue() {
			return entityManagerFactory.createEntityManager();
		}
	};

	protected PersistenceManager(EntityManagerFactory factory) {
		entityManagerFactory = factory;
	}
	
	public PersistenceManager(Database db, char[] password) throws DatabaseWrongPasswordException {
		entityManagerFactory = Persistence.createEntityManagerFactory("accentis", createPersistenceUnitProperties(db, password));
		
		try {
			entityManagers.get();
		} catch (PersistenceException ex) {
			if (null != ex.getCause() && ex.getCause().getClass() == SQLException.class)
				if (((SQLException) ex.getCause()).getSQLState().equals("XBCXA"))
					throw new DatabaseWrongPasswordException();
			
			throw ex;
		}
		database = db;
	}
	
	private static Map<String, String> createPersistenceUnitProperties(Database db, char[] password) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("openjpa.ConnectionURL", db.getConnectionURL(password));

		return properties;
	}
	
	public <T extends BaseObject> void create(T object) {
		EntityManager em = entityManagers.get();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			
			em.persist(object);
			
			logger.info("Entity created: {} [{}]", object.getClass().getName(), object.getId());
			
			tx.commit();
		} catch (PersistenceException ex) {
			logger.info("Entity create rolled back: {} [{}], Exception message: {}", new Object[] { object.getClass().getName(), object.getId(), ex.getMessage() });

			tx.rollback();
		}
	}

	public <T extends BaseObject> List<T> get(String queryName, Class<T> klass, Map<String, Object> parameters) {
		EntityManager em = entityManagers.get();
		EntityTransaction tx = em.getTransaction();
		List<T> results = new ArrayList<T>();
		
		try {
			tx.begin();
			
			TypedQuery<T> query = em.createNamedQuery(queryName, klass);
			for (String key : parameters.keySet())
				query.setParameter(key, parameters.get(key));
			
			results.addAll(query.getResultList());
			
			logger.info("Retrieved {} {} entities with query {}", new Object[] { results.size(), klass.getName(), queryName });
			
			tx.commit();
		} catch (PersistenceException ex) {
			logger.info("Entity retrieval rolled back.  Exception message: {}", ex.getMessage());
			
			tx.rollback();
		}
		
		return results;
	}
	
	public <T extends BaseObject> T save(T object) {
		EntityManager em = entityManagers.get();
		EntityTransaction tx = em.getTransaction();
		T result = null;
		
		try {
			tx.begin();
			
			result = em.merge(object);
			
			logger.info("Entity saved: {} [{}]", object.getClass().getName(), object.getId());
			
			tx.commit();
		} catch (PersistenceException ex) {
			logger.info("Entity save rolled back: {} [{}], Exception message: {}", new Object[] { object.getClass().getName(), object.getId(), ex.getMessage() });

			tx.rollback();
		}
		
		return result;
	}
	
	public <T extends BaseObject> void delete(T object) {
		EntityManager em = entityManagers.get();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			em.remove(object);
			
			logger.info("Entity deleted: {} [{}]", object.getClass().getName(), object.getId());
			
			tx.commit();
		} catch (PersistenceException ex) {
			logger.info("Entity delete rolled back: {} [{}], Exception message: {}", new Object[] { object.getClass().getName(), object.getId(), ex.getMessage() });

			tx.rollback();
		}
	}
	
	public <T extends BaseObject> void refresh(T object) {
		EntityManager em = entityManagers.get();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			em.refresh(object);
			
			logger.info("Entity refreshed: {} [{}]", object.getClass(), object.getId());
			
			tx.commit();
		} catch (PersistenceException ex) {
			logger.info("Entity refresh rolled back: {} [{}], Exception message: {}", new Object[] { object.getClass().getName(), object.getId(), ex.getMessage() });
			
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	public void clear() {
		EntityManager em = entityManagers.get();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			List<BaseObject> objects = new ArrayList<BaseObject>();
			Query q = em.createQuery("SELECT o FROM Account o");
			objects.addAll(q.getResultList());
			
			q = em.createQuery("SELECT o FROM Category o WHERE o.parentCategory IS NULL");
			objects.addAll(q.getResultList());
			
			q = em.createQuery("SELECT o FROM Individual o");
			objects.addAll(q.getResultList());
			
			q = em.createQuery("SELECT o FROM Institution o");
			objects.addAll(q.getResultList());
			
			q = em.createQuery("SELECT o FROM Payee o");
			objects.addAll(q.getResultList());
			
			q = em.createQuery("SELECT o FROM Transaction o");
			objects.addAll(q.getResultList());
			
			for (BaseObject object : objects)
				em.remove(object);
			
			tx.commit();
		} catch (PersistenceException ex) {
			tx.rollback();
		}
	}
	
	public void changePassword(char[] oldPassword, char[] newPassword) throws DatabaseException {
		Connection connection = (Connection) ((OpenJPAEntityManager) entityManagers.get()).getConnection();
		database.changePassword(connection, oldPassword, newPassword);
	}
	
	public String getDatabaseLocation() {
		return database.getLocation();
	}
	
	public static final Map<String, Object> EMPTY_PARAMETER_MAP = new HashMap<String, Object>();
	
	public static Map<String, Object> createQueryParameterMap(String name, Object value) {
		return createQueryParameterMap(new String[] { name }, new Object[] { value });
	}
	
	public static Map<String, Object> createQueryParameterMap(String name1, Object value1, String name2, Object value2) {
		return createQueryParameterMap(new String[] { name1, name2 },  new Object[] { value1, value2 }); 
	}
	
	public static Map<String, Object> createQueryParameterMap(String[] names, Object[] values) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < names.length; ++i)
			map.put(names[i], values[i]);
		
		return map;
	}
}
