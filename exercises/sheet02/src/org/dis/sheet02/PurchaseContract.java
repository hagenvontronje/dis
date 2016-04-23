package org.dis.sheet02;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PURCHASE_CONTRACT")
public class PurchaseContract extends Contract {

	@Column(name = "NUMBER_OF_INSTALLMENTS")
	private int numberOfInstallments;

	@Column(name = "INTEREST_RATE")
	private double interestRate;

	@Column(name = "HOUSE_ID")
	private int houseId;

	public PurchaseContract(int contractNumber, Date date, String place,
			int personId, int houseId, int numInstallments,
			double interestRate) {
		super(contractNumber, date, place, personId);
		this.setNumberOfInstallments(numInstallments);
		this.setInterestRate(interestRate);
		this.setHouseId(houseId);
	}

	public PurchaseContract() {

	}

	public int getNumberOfInstallments() {
		return numberOfInstallments;
	}

	public void setNumberOfInstallments(int numberOfInstallments) {
		this.numberOfInstallments = numberOfInstallments;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

}
