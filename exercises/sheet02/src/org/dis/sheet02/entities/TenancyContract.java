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
@Table(name = "TENANCY_CONTRACT")
@Inheritance(strategy = InheritanceType.JOINED)
public class TenancyContract extends Contract {

	@Column(name = "START_DATE", nullable=false)
	private Date startDate;

	@Column(name = "DURATION", nullable=false)
	private int duration;

	@Column(name = "ADDITIONAL_COSTS", nullable=false, scale=10, precision=2)
	private double additionalCosts;


	@ManyToOne(targetEntity=Apartment.class, optional=false, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="APARTMENT_ID", nullable=false)
	private Apartment apartment;

	public TenancyContract(int contractNumber, Date date, String place,
			Person person, Apartment appartment, Date startDate, int duration,
			double additionalCosts) {
		super(contractNumber, date, place, person);
		this.setStartDate(startDate);
		this.setDuration(duration);
		this.setAdditionalCosts(additionalCosts);
		this.setApartment(appartment);
	}

	public TenancyContract() {

	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getAdditionalCosts() {
		return additionalCosts;
	}

	public void setAdditionalCosts(double additionalCosts) {
		this.additionalCosts = additionalCosts;
	}

	public static void addUserRestriction(Criteria baseCriteria) {
		if (LoginService.User == null)
			return;
		baseCriteria.createCriteria("apartment")
					.add(Restrictions.eq("manager", LoginService.User));
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}
}
