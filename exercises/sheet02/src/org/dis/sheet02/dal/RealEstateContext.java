package org.dis.sheet02.dal;

import java.sql.Connection;

import org.dis.sheet02.entities.Appartment;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;

public class RealEstateContext {

	private final Connection connection;

	private final EntitySet<Person> persons;
	private final EntitySet<House> houses;
	private final EntitySet<Appartment> appartments;
	private final EntitySet<EstateAgent> agents;
	private final EntitySet<TenancyContract> tenancyContracts;
	private final EntitySet<PurchaseContract> purchaseContracts;

	public RealEstateContext() {
		connection = DB2ConnectionManager.getInstance().getConnection();
		persons = new EntitySet<Person>(connection, Person.class);
		houses = new EntitySet<>(connection, House.class);
		appartments = new EntitySet<>(connection, Appartment.class);
		agents = new EntitySet<EstateAgent>(connection, EstateAgent.class);
		tenancyContracts = new EntitySet<TenancyContract>(connection,
				TenancyContract.class);
		purchaseContracts = new EntitySet<PurchaseContract>(connection,
				PurchaseContract.class);
	}

	public <TEntity> EntitySet<TEntity> getEnitySet(Class<TEntity> entityType) {
		return new EntitySet<>(connection, entityType);
	}

	public EntitySet<Person> getPersons() {
		return persons;
	}

	public EntitySet<House> getHouses() {
		return houses;
	}

	public EntitySet<Appartment> getAppartments() {
		return appartments;
	}

	public EntitySet<EstateAgent> getAgents() {
		return agents;
	}

	public EntitySet<TenancyContract> getTenancyContracts() {
		return tenancyContracts;
	}

	public EntitySet<PurchaseContract> getPurchaseContracts() {
		return purchaseContracts;
	}
}
