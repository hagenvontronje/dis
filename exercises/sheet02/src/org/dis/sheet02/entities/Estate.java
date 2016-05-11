package org.dis.sheet02.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dis.sheet02.services.LoginService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "ESTATE")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Estate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Column(name = "SQUARE_AREA", nullable=false)
	private double squareArea;
	
	
	@ManyToOne(targetEntity=EstateAgent.class, optional=false, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="MANAGER_ID", nullable=false)
	private EstateAgent manager;

	public Estate() {}
	
	public Estate(	String city, 
					String postalCode, 
					String street, 
					String streetNumber, 
					double squareArea, 
					EstateAgent manager)
	{
		this.city = city;
		this.postalCode = postalCode;
		this.street = street;
		this.streetNumber = streetNumber;
		this.squareArea = squareArea;
		this.manager = manager;
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

	public EstateAgent getManager() {
		return manager;
	}

	public void setManager(EstateAgent manager) {
		this.manager = manager;
	}
	
	public static void addUserRestriction(Criteria baseCriteria) {
		if (LoginService.User == null || LoginService.User.getId() == 0)
			return;
		baseCriteria.add(Restrictions.eq("manager", LoginService.User));
	}

}
