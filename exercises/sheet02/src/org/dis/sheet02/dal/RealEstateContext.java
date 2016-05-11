package org.dis.sheet02.dal;

import java.sql.SQLException;

import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.Apartment;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.Person;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;

public interface RealEstateContext {

	EntitySet<Person> getPersons();

	EntitySet<House> getHouses();

	EntitySet<Apartment> getAppartments();

	EntitySet<EstateAgent> getAgents();

	EntitySet<TenancyContract> getTenancyContracts();

	EntitySet<PurchaseContract> getPurchaseContracts();

	void CreateSchema() throws SQLException;

	void Close() throws SQLException;

}