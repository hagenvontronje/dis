package org.dis.sheet02.dal.dbcontext;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.dis.sheet02.dal.DB2ConnectionManager;
import org.dis.sheet02.entities.Person;
import org.junit.Test;

public class EntitySetTest {

	@Test
	public void testSelectAll() {

		try {
			EntitySet<Person> persons = new EntitySetImpl<Person>(
					DB2ConnectionManager.getInstance().getConnection(),
					Person.class);
			List<Person> personlist = persons.getAll();
			System.out.println(String.format("%d persons found in DB:",
					personlist.size()));
			for (Person person : personlist)
				System.out.println(String.format(
						"<Id=%d, FirstName=%s, LastName=%s>", person.getId(),
						person.getFirstName(), person.getLastName()));
			System.out.println("=== END ===");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSaveEntity() {
		try {
			EntitySet<Person> persons = new EntitySetImpl<Person>(
					DB2ConnectionManager.getInstance().getConnection(),
					Person.class);
			Person person = new Person("Hagen", "von Tronje", null);
			persons.save(person);
			assertTrue(person.getId() > 0);
			persons.delete(person);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testGet() {
		try {
			EntitySet<Person> persons = new EntitySetImpl<Person>(
					DB2ConnectionManager.getInstance().getConnection(),
					Person.class);
			Person person = new Person("Hagen", "von Tronje", null);
			persons.save(person);
			assertTrue(person.getId() > 0);
			Person retrievedPerson = persons.get(person.getId());
			assertTrue(person.getId() == retrievedPerson.getId());
			persons.delete(person);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
