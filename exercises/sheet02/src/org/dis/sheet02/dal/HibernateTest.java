package org.dis.sheet02.dal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
	
	@SuppressWarnings("unchecked")
	public <T> void testEntity(	Class<T> entityClass,
								Supplier<T> newEntitySupplier, 
								Consumer<T> entityModificator,
								Function<T, Integer> idSelector) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			assertTrue(session.isOpen());

			long originalCount = (long) session
					.createCriteria(entityClass)
					.setProjection(Projections.rowCount())
					.uniqueResult();
			
			
			T person = newEntitySupplier.get();
			assertTrue(idSelector.apply(person) <= 0);

			session.beginTransaction();
			
			person = (T) session.merge(person);
			assertTrue(idSelector.apply(person) > 0);

			long newCount = (long) session
					.createCriteria(entityClass)
					.setProjection(Projections.rowCount())
					.uniqueResult();
			assertTrue(newCount == originalCount + 1);
			
			entityModificator.accept(person);
//			session.flush();
//			session.evict(person);
			session.getTransaction().commit();

			session = sessionFactory.getCurrentSession();
			assertTrue(session.isOpen());
			session.beginTransaction();

			person = session.load(entityClass, idSelector.apply(person));
			session.delete(person);
			newCount = (long) session
					.createCriteria(entityClass)
					.setProjection(Projections.rowCount())
					.uniqueResult();
			assertTrue(newCount == originalCount);
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
	
	@Test
	public void testPerson() {
		testEntity(Person.class, 
				() -> new Person("Arthur", "Testman", "Teststreet 123b"), 
				(p) -> p.setFirstName("Bertha"), 
				(p) -> p.getId());
	}
	
	@Test
	public void testEstateAgent() {
		testEntity(EstateAgent.class, 
				() -> new EstateAgent("Arthur Dent", "Not on earth", "adent", "adent"), 
				(p) -> p.setName("A. Dent"), 
				(p) -> p.getId());
	}
	
	@Test 
	public void testHouse() {
		Session session = sessionFactory.getCurrentSession();
		assertTrue(session.isOpen());
		session.beginTransaction();
		final EstateAgent manager = (EstateAgent) session.merge(
				new EstateAgent("Test", "Test", "", ""));
		System.out.printf("Temporary Agent has id: %d\n", manager.getId());
		session.getTransaction().commit();
		testEntity(House.class, 
				() -> {
					return new House("Hamburg", "22347", "Somstreet", "123b", 150.32, 1, 120000.73, false,
							manager);
				}, 
				(h) -> h.setCity("Triton 73b"),
				(h) -> h.getId());
		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		EstateAgent dbVersion = session.load(manager.getClass(), manager.getId());
		session.delete(dbVersion);
		session.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testPrototypeQuery() {
		Session session = sessionFactory.getCurrentSession();
		assertTrue(session.isOpen());
		session.beginTransaction();
		long origHousesCount = (long) session
				.createCriteria(House.class)
				.setProjection(Projections.rowCount())
				.uniqueResult();
		final EstateAgent manager = (EstateAgent) session.merge(
				new EstateAgent("Test", "Test", "", ""));
		final EstateAgent manager2 = (EstateAgent) session.merge(
				new EstateAgent("Test2", "Test2", "", ""));
		System.out.printf("Temporary Agent has id: %d\n", manager.getId());
		session.merge(
				new House("Hamburg", "22347", "Somstreet", "123b", 150.32, 1, 120000.73, false,
						manager));
		session.merge(
				new House("Hamburg", "22347", "Somstreet", "123b", 150.32, 1, 120000.73, false,
						manager2));
		session.getTransaction().commit();
		
		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<House> houses = session.createCriteria(House.class).add(Restrictions.eq("manager", manager)).list();
		assertTrue(houses.size() == 1);
		Object dbVersion = session.load(manager.getClass(), manager.getId());
		session.delete(dbVersion);
		dbVersion = session.load(manager2.getClass(), manager2.getId());
		session.delete(dbVersion);
		session.flush();
		long finalHousesCount = (long) session
				.createCriteria(House.class)
				.setProjection(Projections.rowCount())
				.uniqueResult();
		assertTrue(finalHousesCount == origHousesCount);
		session.getTransaction().commit();
	}

}