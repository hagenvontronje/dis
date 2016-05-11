package org.dis.sheet02.dal;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.dal.dbcontext.HibernateContext;
import org.dis.sheet02.dal.dbcontext.HibernateEntitySet;
import org.dis.sheet02.entities.Appartment;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;
import org.hibernate.criterion.Criterion;

public class RealEstateHibernateContext extends HibernateContext implements RealEstateContext {

	private final EntitySet<Person> persons;
	private final EntitySet<Appartment> appartments;
	private final EntitySet<EstateAgent> agents;
	private final EntitySet<House> houses;
	private final EntitySet<PurchaseContract> purchases;
	private final EntitySet<TenancyContract> tenancies;

	public RealEstateHibernateContext() {
		this.persons = createEntitySet(	Person.class, 
										(p) -> p.getId(), 
										(p, id) -> p.setId((int)id),
										() -> null);
		this.appartments = createEntitySet(	Appartment.class, 
											(p) -> p.getId(), 
											(p, id) -> p.setId((int)id),
											() -> Appartment.getUserRestriction());
		this.agents = createEntitySet(	EstateAgent.class, 
                        				(p) -> p.getId(), 
                        				(p, id) -> p.setId((int)id),
                        				() -> null);
		this.houses = createEntitySet(	House.class, 
										(p) -> p.getId(), 
										(p, id) -> p.setId((int)id),
										() -> House.getUserRestriction());
		this.purchases = createEntitySet(	PurchaseContract.class, 
											(p) -> p.getId(), 
											(p, id) -> p.setId((int)id),
											() -> PurchaseContract.getUserRestriction());
		this.tenancies = createEntitySet(	TenancyContract.class, 
                            				(p) -> p.getId(), 
                            				(p, id) -> p.setId((int)id),
                            				() -> TenancyContract.getUserRestriction());
	}
	
	private <T> EntitySet<T> createEntitySet(	
										Class<T> type, 
										Function<T, Serializable> idSelector, 
										BiConsumer<T, Serializable> idSetter,
										Supplier<Criterion> userRestriction) {
		return new HibernateEntitySet<T>(getSessionFactory(), type, idSelector, userRestriction);
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
	public EntitySet<Appartment> getAppartments() {
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
