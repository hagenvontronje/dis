package org.dis.sheet02.entities;

import javax.persistence.Column;
import javax.persistence.Id;

public class Estate {

	@Id
	@Column(name = "ID")
	private int id = -1;

	@Column(name = "CITY")
	private String city;

	@Column(name = "POSTAL_CODE")
	private String postalCode;

	@Column(name = "STREET")
	private String street;

	@Column(name = "STREET_NUMBER")
	private String streetNumber;

	@Column(name = "SQUARE_AREA")
	private double squareArea;

	@Column(name = "ESTATE_AGENT_ID")
	private int managerId;

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

}
