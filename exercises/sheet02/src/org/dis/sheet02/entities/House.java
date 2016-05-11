package org.dis.sheet02.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.dis.sheet02.services.LoginService;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

@Entity
@Table(name = "HOUSE")
@Inheritance(strategy = InheritanceType.JOINED)
public class House extends Estate {

	@Column(name = "FLOORS", nullable=false)
	private int floors;

	@Column(name = "PRICE", nullable=false, scale=15, precision=2)
	private double price;

	@Column(name = "GARDEN", nullable=false)
	private int garden;
	
	
	public House() {}
	
	public House(	String city, 
					String postalCode, 
					String street, 
					String streetNumber, 
					double squareArea,
					int floors,
					double price,
					boolean garden,
					EstateAgent manager)
	{
		super(city, postalCode, street, streetNumber, squareArea, manager);
		this.floors = floors;
		this.price = price;
		hasGarden(garden);
	}

	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean hasGarden() {
		return garden == 1;
	}

	public void hasGarden(boolean garden) {
		this.garden = garden ? 1 : 0;
	}

}
