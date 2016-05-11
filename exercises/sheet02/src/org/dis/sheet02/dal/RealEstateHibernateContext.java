package org.dis.sheet02.dal;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.dal.dbcontext.HibernateContext;
import org.dis.sheet02.dal.dbcontext.HibernateEntitySet;
import org.dis.sheet02.entities.Apartment;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;
import org.hibernate.Criteria;

public class RealEstateHibernateContext 
		extends HibernateContext 
		implements RealEstateContext 
{
	private final EntitySet<Person> persons;
	private final EntitySet<Apartment> appartments;
	private final EntitySet<EstateAgent> agents;
	private final EntitySet<House> houses;
	private final EntitySet<PurchaseContract> purchases;
	private final EntitySet<TenancyContract> tenancies;

	public RealEstateHibernateContext() {
		this.persons = createEntitySet(	Person.class, 
										(p) -> p.getId(), 
										(p, id) -> p.setId((int)id),
										(c) -> {});
		this.appartments = createEntitySet(	
									Apartment.class, 
									(p) -> p.getId(), 
									(p, id) -> p.setId((int)id),
									(c) -> Apartment.addUserRestriction(c));
		this.agents = createEntitySet(	
									EstateAgent.class, 
                    				(p) -> p.getId(), 
                    				(p, id) -> p.setId((int)id),
                    				(c) -> {});
		this.houses = createEntitySet(	
									House.class, 
									(p) -> p.getId(), 
									(p, id) -> p.setId((int)id),
									(c) -> House.addUserRestriction(c));
		this.purchases = createEntitySet(	
								PurchaseContract.class, 
								(p) -> p.getId(), 
								(p, id) -> p.setId((int)id),
								(x) -> PurchaseContract.addUserRestriction(x));
		this.tenancies = createEntitySet(	
								TenancyContract.class, 
                				(p) -> p.getId(), 
                				(p, id) -> p.setId((int)id),
                				(c) -> TenancyContract.addUserRestriction(c));
	}
	
	private <T> EntitySet<T> createEntitySet(	
								Class<T> type, 
								Function<T, Serializable> idSelector, 
								BiConsumer<T, Serializable> idSetter,
								Consumer<Criteria> userRestrictionAppender) 
	{
		return new HibernateEntitySet<T>(	getSessionFactory(), 
                            				type, 
                            				idSelector, 
                            				userRestrictionAppender);
	}
	
	@Override
	public EntitySet<Person> getPersons() {
		return persons;
	}

	@Override
	public EntitySet<House> getHouses() {
		return houses;
	}

	@Override
	public EntitySet<Apartment> getAppartments() {
		return appartments;
	}

	@Override
	public EntitySet<EstateAgent> getAgents() {
		return agents;
	}

	@Override
	public EntitySet<TenancyContract> getTenancyContracts() {
		return tenancies;
	}

	@Override
	public EntitySet<PurchaseContract> getPurchaseContracts() {
		return purchases;
	}

}
