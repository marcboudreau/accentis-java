package ca.msbsoftware.accentis.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ca.msbsoftware.accentis.persistence.pojos.BaseObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class PersistenceManager {

	private EntityManagerFactory entityManagerFactory;
	
	//private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ThreadLocal<EntityManager> entityManagers = new ThreadLocal<EntityManager>() {
		@Override
		protected EntityManager initialValue() {
			return entityManagerFactory.createEntityManager();
		}
	};
	
	public PersistenceManager(EntityManagerFactory factory) {
		entityManagerFactory = factory;
	}
	
	public <T extends BaseObject> void create(T object) {
		EntityManager em = entityManagers.get();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			
			em.persist(object);
			
			//logger.info("Entity created: %s [%d]", new Object[] { object.getClass().getName(), object.getId() });
			
			tx.commit();
		} catch (PersistenceException ex) {
			//logger.info("Entity create rolled back: %s [%d], Exception message: %s", new Object[] { object.getClass().getName(), object.getId(), ex.getMessage() });

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
			
			//logger.info("Retrieved %d %s entities with query %s", new Object[] { results.size(), klass.getName(), queryName });
			
			tx.commit();
		} catch (PersistenceException ex) {
			//logger.info("Entity retrieval rolled back.  Exception message: %s", ex.getMessage());
			
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
			
			//logger.info("Entity saved: %s [%d]", new Object[] { object.getClass().getName(), object.getId() });
			
			tx.commit();
		} catch (PersistenceException ex) {
			//logger.info("Entity save rolled back: %s [%d], Exception message: %s", new Object[] { object.getClass().getName(), object.getId(), ex.getMessage() });

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
			
			//logger.info("Entity deleted: %s [%d]", new Object[] { object.getClass().getName(), object.getId() });
			
			tx.commit();
		} catch (PersistenceException ex) {
			//logger.info("Entity delete rolled back: %s [%d], Exception message: %s", new Object[] { object.getClass().getName(), object.getId(), ex.getMessage() });

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
