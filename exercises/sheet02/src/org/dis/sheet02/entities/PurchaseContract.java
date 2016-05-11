package org.dis.sheet02.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dis.sheet02.services.LoginService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "PURCHASE_CONTRACT")
@Inheritance(strategy = InheritanceType.JOINED)
public class PurchaseContract extends Contract {

	@Column(name = "NUMBER_OF_INSTALLMENTS", nullable=false)
	private int numberOfInstallments;

	@Column(name = "INTEREST_RATE", nullable=false)
	private double interestRate;
	
	@ManyToOne(targetEntity=House.class, optional=false, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="HOUSE_ID", nullable=false)
	private House house;

	public PurchaseContract(int contractNumber, Date date, String place,
			Person person, House house, int numInstallments,
			double interestRate) {
		super(contractNumber, date, place, person);
		this.setNumberOfInstallments(numInstallments);
		this.setInterestRate(interestRate);
		this.setHouse(house);
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

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public static void addUserRestriction(Criteria baseCriteria) {
		if (LoginService.User == null)
			return;
		baseCriteria.createCriteria("house")
					.add(Restrictions.eq("manager", LoginService.User));
	}
}
