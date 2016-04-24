package org.dis.sheet02.dal;

import org.dis.sheet02.dal.dbcontext.DbContext;
import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.Appartment;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;

/**
 * Database context for real estate database.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 */
public class RealEstateContext extends DbContext {

	public RealEstateContext() {
		super(	Person.class, 
				House.class, 
				Appartment.class,
				EstateAgent.class,
				TenancyContract.class,
				PurchaseContract.class);
	}

	public EntitySet<Person> getPersons() {
		return getEntitySet(Person.class);
	}

	public EntitySet<House> getHouses() {
		return getEntitySet(House.class);
	}

	public EntitySet<Appartment> getAppartments() {
		return getEntitySet(Appartment.class);
	}

	public EntitySet<EstateAgent> getAgents() {
		return getEntitySet(EstateAgent.class);
	}

	public EntitySet<TenancyContract> getTenancyContracts() {
		return getEntitySet(TenancyContract.class);
	}

	public EntitySet<PurchaseContract> getPurchaseContracts() {
		return getEntitySet(PurchaseContract.class);
	}
}
