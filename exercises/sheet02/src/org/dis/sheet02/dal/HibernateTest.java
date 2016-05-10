package org.dis.sheet02.dal;

import static org.junit.Assert.*;

import java.net.URL;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Test;


public class HibernateTest {

	
	
	@Test
	public void test() {
		StandardServiceRegistry registry = null;
		try {
			ClassLoader cl = this.getClass().getClassLoader();
			URL cfgUrl = cl.getResource("hibernate.cfg.xml");
			System.out.println(cl.getResource("./"));
//			registry = new StandardServiceRegistryBuilder()
//					.configure() // configures settings from hibernate.cfg.xml
//					.build();
//			SessionFactory sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			assertTrue(session.isOpen());
		}
		catch (Exception e) {
			e.printStackTrace();
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			if (registry != null)
				StandardServiceRegistryBuilder.destroy( registry );
			fail(e.getMessage());
		}
	}

}