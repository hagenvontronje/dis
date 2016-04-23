package org.dis.sheet02;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "APPARTMENT")
public class Appartment extends Estate {

	@Column(name = "FLOOR")
	private int floor;

	@Column(name = "RENT")
	private double rent;

	@Column(name = "ROOMS")
	private int rooms;

	@Column(name = "BALCONY")
	private int balcony;

	@Column(name = "BUILT_IN_KITCHEN")
	private int builtInKitchen;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public double getRent() {
		return rent;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public boolean hasBalcony() {
		return balcony == 1;
	}

	public void hasBalcony(boolean hasBalcony) {
		this.balcony = hasBalcony ? 1 : 0;
	}

	public boolean hasBuiltInKitchen() {
		return builtInKitchen == 1;
	}

	public void hasBuiltInKitchen(boolean hasBuiltInKitchen) {
		this.builtInKitchen = hasBuiltInKitchen ? 1 : 0;
	}
}
