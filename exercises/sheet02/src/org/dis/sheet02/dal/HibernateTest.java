package org.dis.sheet02.dal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.EnumSet;

import org.dis.sheet02.entities.Appartment;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.junit.Test;

public class HibernateTest {

	private static final SessionFactory sessionFactory;
	static {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	@Test
	public void testCreateSchema() {
		StandardServiceRegistry registry = sessionFactory
				.getSessionFactoryOptions()
				.getServiceRegistry();
		Metadata metadata = new MetadataSources(registry).buildMetadata();
		new SchemaExport().create(EnumSet.of(TargetType.DATABASE), metadata);
	}
	
	
	@Test
	public void test() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			assertTrue(session.isOpen());

			Person person = new Person("Arthur", "Dent",
					"Definitely not on earth");
			assertTrue(person.getId() <= 0);

			session.beginTransaction();
			session.persist(person);
			assertTrue(person.getId() > 0);

			person.setAddress("The whole wide universe");
			session.flush();
			session.evict(person);
			session.getTransaction().commit();

			session = sessionFactory.getCurrentSession();
			assertTrue(session.isOpen());
			session.beginTransaction();

			@SuppressWarnings("unchecked")
			Collection<Person> persons = session.createCriteria(Person.class)
					.list();
			System.out.println("Persons:");
			for (Person p : persons)
				System.out.printf(
						"Person: id=%d, first_name=%s, last_name=%s, address=%s%n",
						p.getId(), p.getFirstName(), p.getLastName(),
						p.getAddress());
			person = session.load(Person.class, person.getId());
			session.delete(person);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			// The registry would be destroyed by the SessionFactory, but we had
			// trouble building the SessionFactory
			// so destroy it manually.
			if (session != null && session.getTransaction()
					.getStatus() == TransactionStatus.ACTIVE)
				session.getTransaction().rollback();
			fail(e.getMessage());
		}
	}

}