package ca.msbsoftware.accentis.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.msbsoftware.accentis.persistence.database.MemoryDatabase;
import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Individual;

public class PersistenceManagerTest {

	private PersistenceManager manager;
	
	@Before
	public void setUp() throws Exception {
		manager = new PersistenceManager(new MemoryDatabase(), null);
	}
	
	@After
	public void tearDown() throws Exception {
		manager.clear();
	}
	
	@Test
	public void verifyCreate() throws Exception {
		Account account = new Account();
		account.setName("TestAccount");
		
		manager.create(account);
		
		assertTrue(0 != account.getId());
	}
	
	@Test
	public void verifyRetrieve() throws Exception {
		Individual individual = new Individual();
		individual.setName("Bob Johnson");
		individual.setBirthDate(new Date());
		
		manager.create(individual);
		
		List<Individual> results = manager.get(Individual.GET_ALL_INDIVIDUALS_QUERY, Individual.class, PersistenceManager.EMPTY_PARAMETER_MAP);
		
		assertEquals(1, results.size());
		assertEquals(individual, results.get(0));
	}
	
	@Test
	public void verifyClear() throws Exception {
		Account account = new Account();
		account.setName("TestAccount");
		
		manager.create(account);
		manager.clear();
		
		List<Account> results = manager.get(Account.GET_ALL_ACCOUNTS_QUERY, Account.class, PersistenceManager.EMPTY_PARAMETER_MAP);
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void verifySave() throws Exception {
		Individual individual = new Individual();
		individual.setName("Bob Johnson");
		individual.setDescription("Old Description");
		
		manager.create(individual);
		individual.setDescription("New Description");
		manager.save(individual);

		List<Individual> results = manager.get(Individual.GET_ALL_INDIVIDUALS_QUERY, Individual.class, PersistenceManager.EMPTY_PARAMETER_MAP);
		Individual retrievedIndividual = results.get(0);
		
		assertEquals("New Description", retrievedIndividual.getDescription());
	}
	
	@Test
	public void verifyDelete() throws Exception {
		Individual individual = new Individual();
		individual.setName("Bob Johnson");
		
		manager.create(individual);
		manager.delete(individual);

		List<Individual> results = manager.get(Individual.GET_ALL_INDIVIDUALS_QUERY, Individual.class, PersistenceManager.EMPTY_PARAMETER_MAP);
		
		assertEquals(0, results.size());
	}
}
