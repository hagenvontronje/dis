package org.dis.sheet02.entities;

import java.util.Date;

import javax.persistence.Column;

public class Contract {
	@Column(name = "CONTRACT_NUMBER")
	private int contractNumber;

	@Column(name = "DATE")
	private Date date;

	@Column(name = "PLACE")
	private String place;

	@Column(name = "PERSON_ID")
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
}
