package org.dis.sheet02.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "HOUSE")
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
					int managerId)
	{
		super(city, postalCode, street, streetNumber, squareArea, managerId);
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
