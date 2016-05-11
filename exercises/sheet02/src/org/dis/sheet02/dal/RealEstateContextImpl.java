package org.dis.sheet02.dal;

import org.dis.sheet02.dal.dbcontext.DbContextImpl;
import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.Apartment;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;

/**
 * Custom implementation of {@link RealEstateContext}.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 */
public class RealEstateContextImpl extends DbContextImpl implements RealEstateContext {

	public RealEstateContextImpl() {
		super(	Person.class, 
				House.class, 
				Apartment.class,
				EstateAgent.class,
				TenancyContract.class,
				PurchaseContract.class);
	}

	/* (non-Javadoc)
	 * @see org.dis.sheet02.dal.RealEstateContext#getPersons()
	 */
	@Override
	public EntitySet<Person> getPersons() {
		return getEntitySet(Person.class);
	}

	/* (non-Javadoc)
	 * @see org.dis.sheet02.dal.RealEstateContext#getHouses()
	 */
	@Override
	public EntitySet<House> getHouses() {
		return getEntitySet(House.class);
	}

	/* (non-Javadoc)
	 * @see org.dis.sheet02.dal.RealEstateContext#getAppartments()
	 */
	@Override
	public EntitySet<Apartment> getAppartments() {
		return getEntitySet(Apartment.class);
	}

	/* (non-Javadoc)
	 * @see org.dis.sheet02.dal.RealEstateContext#getAgents()
	 */
	@Override
	public EntitySet<EstateAgent> getAgents() {
		return getEntitySet(EstateAgent.class);
	}

	/* (non-Javadoc)
	 * @see org.dis.sheet02.dal.RealEstateContext#getTenancyContracts()
	 */
	@Override
	public EntitySet<TenancyContract> getTenancyContracts() {
		return getEntitySet(TenancyContract.class);
	}

	/* (non-Javadoc)
	 * @see org.dis.sheet02.dal.RealEstateContext#getPurchaseContracts()
	 */
	@Override
	public EntitySet<PurchaseContract> getPurchaseContracts() {
		return getEntitySet(PurchaseContract.class);
	}
}
