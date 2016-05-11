package org.dis.sheet02.dal.dbcontext;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class HibernateEntitySet<TEntity> implements EntitySet<TEntity> {

	private final SessionFactory sessionFactory;
	private final Class<TEntity> entityType;
	private final Function<TEntity, Serializable> idSelector;
	private final Consumer<Criteria> userRestrictionSupplier;
	
	public HibernateEntitySet(	SessionFactory factory,
                    			Class<TEntity> entityType,
                    			Function<TEntity, Serializable> idSelector,
                    			Consumer<Criteria> userRestrictionSupplier) {
		this.sessionFactory = factory;
		this.entityType = entityType;
		this.idSelector = idSelector;
		this.userRestrictionSupplier = userRestrictionSupplier;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TEntity save(TEntity entity) throws SQLException {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			entity = (TEntity)session.merge(entity);
			session.flush();
			session.getTransaction().commit();
			return entity;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public List<TEntity> getAll() throws SQLException {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Criteria critetia = session
					.createCriteria(getEntityType());
			userRestrictionSupplier.accept(critetia);
			@SuppressWarnings("unchecked")
			List<TEntity> list = critetia.list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public void delete(TEntity entity) throws SQLException {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			TEntity persistedEntity = session.load(	getEntityType(), 
													idSelector.apply(entity));
			session.delete(persistedEntity);
			session.getTransaction().commit();
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public TEntity get(Object id) throws SQLException {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			TEntity entity = (TEntity) session.createCriteria(getEntityType())
                        			.add(Restrictions.idEq(id))
                        			.uniqueResult();
			session.getTransaction().commit();
			return entity;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public Class<TEntity> getEntityType() {
		return entityType;
	}

}
