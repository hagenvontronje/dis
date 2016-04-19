package org.dis.sheet02;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="HOUSE")
public class House extends Estate {

	@Column(name="FLOORS")
	private int floors;

	@Column(name="PRICE")
	private double price;

	@Column(name="GARDEN")
	private int garden;
	
	public int getFloors() {
		return floors;
	}

	public void setFloors(int _floors) {
		this.floors = _floors;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double _price) {
		this.price = _price;
	}

	public boolean hasGarden() {
		return garden == 1;
	}

	public void hasGarden(boolean _garden) {
		this.garden = _garden ? 1 : 0;
	}

}
