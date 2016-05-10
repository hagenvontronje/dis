package org.dis.sheet02.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "APPARTMENT")
@Inheritance(strategy = InheritanceType.JOINED)
public class Appartment extends Estate {

	@Column(name = "FLOOR", nullable=false)
	private int floor;

	@Column(name = "RENT", nullable=false)
	private double rent;

	@Column(name = "ROOMS", nullable=false)
	private int rooms;

	@Column(name = "BALCONY", nullable=false)
	private int balcony;

	@Column(name = "BUILT_IN_KITCHEN", nullable=false)
	private int builtInKitchen;


	public Appartment() {}
	
	public Appartment(	String city, 
					String postalCode, 
					String street, 
					String streetNumber, 
					double squareArea,
					int floor,
					double rent,
					int rooms,
					boolean hasBalcony,
					boolean hasBuildInKitchen,
					int managerId)
	{
		super(city, postalCode, street, streetNumber, squareArea, managerId);
		this.floor = floor;
		this.rent = rent;
		this.rooms = rooms;
		hasBalcony(hasBalcony);
		hasBuiltInKitchen(hasBuildInKitchen);
	}
	
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
