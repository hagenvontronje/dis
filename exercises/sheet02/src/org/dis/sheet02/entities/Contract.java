package org.dis.sheet02.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRACT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	
	@Column(name = "CONTRACT_NUMBER", nullable=false)
	private int contractNumber;

	@Column(name = "DATE", nullable=false)
	private Date date;

	@Column(name = "PLACE", nullable=false)
	private String place;

	@Column(name = "PERSON_ID", nullable=false)
//	@ManyToOne(targetEntity=Person.class, optional=false)
	private int personId;

	public Contract() {

	}

	public Contract(int contractNumber, Date date, String place, int personID) {
		this.contractNumber = contractNumber;
		this.date = date;
		this.place = place;
		this.personId = personID;
	}

	public int getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(int contract_number) {
		this.contractNumber = contract_number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisplayName() {
		return String.format("%d (%s)",
				contractNumber,
				this instanceof TenancyContract ? "Tenancy" : "Purchase");
	}
}
