package org.dis.sheet02.dal.dbcontext;

import java.sql.SQLException;
import java.util.EnumSet;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

public class HibernateContext implements DbContext {

	private static SessionFactory sessionFactory;
	
	static {
		
	}
	
	protected SessionFactory getSessionFactory() {
		if (sessionFactory != null)
			return sessionFactory;
		Configuration configuration = new Configuration().configure();
		sessionFactory = configuration.buildSessionFactory();
		return sessionFactory;
	}
	
	public HibernateContext() {
	}
	
	@Override
	public void Close() throws SQLException {
//		try {
//			if (sessionFactory != null)
//				sessionFactory.close();
//			sessionFactory = null;
//		} catch (Exception e) {
//			throw new SQLException(e);
//		}
	}

	@Override
	public void CreateSchema() throws SQLException {
		try {
			StandardServiceRegistry registry = sessionFactory
					.getSessionFactoryOptions()
					.getServiceRegistry();
			Metadata metadata = new MetadataSources(registry).buildMetadata();
			new SchemaExport().create(EnumSet.of(TargetType.DATABASE), metadata);
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

}
