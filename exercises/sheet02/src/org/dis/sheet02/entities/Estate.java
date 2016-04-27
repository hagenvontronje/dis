package org.dis.sheet02.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public abstract class Estate {

	@Id
	@Column(name = "ID")
	private int id = -1;

	@Column(name = "CITY", nullable=false)
	private String city;

	@Column(name = "POSTAL_CODE", nullable=false)
	private String postalCode;

	@Column(name = "STREET", nullable=false)
	private String street;

	@Column(name = "STREET_NUMBER", nullable=false)
	private String streetNumber;

	@Column(name = "SQUARE_AREA", nullable=false, scale=10, precision=2)
	private double squareArea;

	@Column(name = "ESTATE_AGENT_ID")
	@ManyToOne(targetEntity=EstateAgent.class, optional=false)
	private int managerId;

	public Estate() {}
	
	public Estate(	String city, 
					String postalCode, 
					String street, 
					String streetNumber, 
					double squareArea, 
					int managerId)
	{
		this.city = city;
		this.postalCode = postalCode;
		this.street = street;
		this.streetNumber = streetNumber;
		this.squareArea = squareArea;
		this.managerId = managerId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public double getSquareArea() {
		return squareArea;
	}

	public void setSquareArea(double squareArea) {
		this.squareArea = squareArea;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int estateAgentId) {
		this.managerId = estateAgentId;
	}

	public String getDisplayName() {
		return String.format("%s, %s (%s)",
				String.format("%s %s", 
						getCity(),
						getPostalCode()).trim(),
				String.format("%s %s",
						getStreet(),
						getStreetNumber()).trim(),
				this instanceof House ? "H" : "A");
	}

}
