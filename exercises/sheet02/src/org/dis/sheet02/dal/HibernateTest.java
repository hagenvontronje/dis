package org.dis.sheet02.dal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.EnumSet;
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
			session.persist(person);
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
		testEntity(House.class, 
				() -> new House("Hamburg", "22347", "Somstreet", "123b", 150.32, 1, 120000.73, false, 0), 
				(h) -> h.setCity("Triton 73b"),
				(h) -> h.getId());
	}

}