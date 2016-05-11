package org.dis.sheet02.dal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.Appartment;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.dis.sheet02.entities.PurchaseContract;
import org.junit.Test;

/**
 * Test for {@link RealEstateContextImpl}.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 */
public class RealEstateContextTest {

	@Test
	public void testRealEstateContext() {
		try {
			RealEstateContext ctx = CreateContext();
			ctx.Close();
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}

	private RealEstateContext CreateContext() {
		return ContextBuilder.build();
	}

	@Test
	public void testGetPersons() {
		try {
			RealEstateContext ctx = CreateContext();
			EntitySet<Person> persons = ctx.getPersons();
			// save:
			Person p = new Person("Carl", "Nielson", "Nämmerlynd");
			p = persons.save(p);
			assertNotEquals("ID was not set on after save.", p.getId(), 0);
			Person p2 = persons.get(p.getId());
			assertEquals("Wrong person retrieved.", p2.getId(), p.getId());
			assertEquals("Saved first name incorrect.", p2.getFirstName(), p.getFirstName());
			assertEquals("Saved last name incorrect.", p2.getLastName(), p.getLastName());
			assertEquals("Saved address incorrect.", p2.getAddress(), p.getAddress());
			
			// get all:
			List<Person> allPersons = persons.getAll();
			assertNotNull("GetAll returned null", allPersons);
			int personCount = allPersons.size();
			assertNotEquals("GetAll retuned no persons.", personCount, 0);
			assertTrue("Created person not found.", allPersons.contains(p));
			
			// update:
			p.setLastName("Weber");
			p = persons.save(p);
			p2 = persons.get(p.getId());
			assertEquals("Updated column incorrect.", p.getLastName(), p2.getLastName());
			
			// delete:
			persons.delete(p2);
			allPersons = persons.getAll();
			assertEquals("Wrong number of entities after delete.", personCount - 1, allPersons.size());
			ctx.Close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGetHouses() {
		try {
			RealEstateContext ctx = CreateContext();
			EntitySet<House> houses = ctx.getHouses();
			EntitySet<EstateAgent> agents = ctx.getAgents();

			// create manager for testing:
			EstateAgent agent = new EstateAgent("a", "b", "c", "d");
			agent = agents.save(agent);
			
			// save:
			House h = new House("Hamburg", 
            					"1245", 
            					"b street", 
            					"34b", 
            					345.62, 
            					2, 
            					350000.42, 
            					true, 
            					agent);
			h = houses.save(h);
			
			// get all:
			List<House> all = houses.getAll();
			int count = all.size();
			assertTrue("GetAll did not find new entity.", !all.isEmpty());
			
			// update:
			h.setPrice(1000000.01);
			h = houses.save(h);
			House h2 = houses.get(h.getId());
			assertTrue("Updated column incorrect.", h.getPrice() == h2.getPrice());
			
			// delete:
			houses.delete(h2);
			all = houses.getAll();
			assertEquals("Wrong number of entities after delete.", count - 1, all.size());
			
			agents.delete(agent);
			ctx.Close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGetAppartments() {
		try {
			RealEstateContext ctx = CreateContext();
			EntitySet<Appartment> appartments = ctx.getAppartments();
			EntitySet<EstateAgent> agents = ctx.getAgents();

			// create manager for testing:
			EstateAgent agent = new EstateAgent("a", "b", "c", "d");
			agent = agents.save(agent);
			
			// save:
			Appartment a = new Appartment("Hamburg", 
            					"1245", 
            					"b street", 
            					"34b", 
            					345.62, 
            					2, 
            					3500.42,
            					4,
            					false,
            					true, 
            					agent);
			a = appartments.save(a);
			
			// get all:
			List<Appartment> all = appartments.getAll();
			int count = all.size();
			assertTrue("GetAll did not find new entity.", !all.isEmpty());
			
			// update:
			a.hasBalcony(true);
			a = appartments.save(a);
			Appartment a2 = appartments.get(a.getId());
			assertTrue("Updated column incorrect.", a.hasBalcony() == a2.hasBalcony());
			
			// delete:
			appartments.delete(a2);
			all = appartments.getAll();
			assertEquals("Wrong number of entities after delete.", count - 1, all.size());

			agents.delete(agent);
			ctx.Close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGetAgents() {
		try {
			RealEstateContext ctx = CreateContext();
			EntitySet<EstateAgent> agents = ctx.getAgents();
			
			// save:
			EstateAgent a = new EstateAgent("a", "b", "c", "d");
			a = agents.save(a);
			
			// get all:
			List<EstateAgent> all = agents.getAll();
			int count = all.size();
			assertTrue("GetAll did not find new entity.", !all.isEmpty());
			
			// update:
			a.setPassword("qwertz");
			a = agents.save(a);
			EstateAgent a2 = agents.get(a.getId());
			assertTrue("Updated column incorrect.", a2.getPassword().equals(a.getPassword()));
			
			// delete:
			agents.delete(a2);
			all = agents.getAll();
			assertEquals("Wrong number of entities after delete.", count - 1, all.size());
			
			ctx.Close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGetTenancyContracts() {
		
	}

	@Test
	public void testGetPurchaseContracts() {
		try {
			RealEstateContext ctx = CreateContext();
			EntitySet<PurchaseContract> contracts = ctx.getPurchaseContracts();
			EntitySet<House> houses = ctx.getHouses();
			EntitySet<EstateAgent> agents = ctx.getAgents();
			EntitySet<Person> persons = ctx.getPersons();

			// create dependencies for testing:
			EstateAgent agent = new EstateAgent("a", "b", "c", "d");
			agent = agents.save(agent);
			House house = new House("Hamburg", 
					"1245", 
					"b street", 
					"34b", 
					345.62, 
					2, 
					350000.42, 
					true, 
					agent);
			house = houses.save(house);
			Person person = new Person("Carl", "Nielson", "Nämmerlynd");
			person = persons.save(person);
			
			// save:
			PurchaseContract c = new PurchaseContract(	1, 
														new Date(),
														"Hamburg",
														person.getId(),
														house.getId(),
														5,
														0.03);
			c = contracts.save(c);
			
			// get all:
			List<PurchaseContract> all = contracts.getAll();
			int count = all.size();
			assertTrue("GetAll did not find new entity.", !all.isEmpty());
			
			// update:
			c.setInterestRate(0.0255);
			c = contracts.save(c);
			PurchaseContract c2 = contracts.get(c.getId());
			assertTrue("Updated column incorrect.", c.getInterestRate() == c2.getInterestRate());
			
			// delete:
			contracts.delete(c2);
			all = contracts.getAll();
			assertEquals("Wrong number of entities after delete.", count - 1, all.size());
			
			persons.delete(person);
			houses.delete(house);
			agents.delete(agent);
			ctx.Close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


}
