package org.dis.sheet02.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	
	@OneToMany(mappedBy="house", orphanRemoval=true, 
			fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	List<PurchaseContract> purchases = new ArrayList<>();
	
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
