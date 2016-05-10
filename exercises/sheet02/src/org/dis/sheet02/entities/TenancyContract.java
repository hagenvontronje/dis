package org.dis.sheet02.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

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

	@Column(name = "APPARTMENT_ID")
//	@ManyToOne(targetEntity=Appartment.class, optional=false)
	private int appartmentId;

	public TenancyContract(int contractNumber, Date date, String place,
			int personId, int appartmentId, Date startDate, int duration,
			double additionalCosts) {
		super(contractNumber, date, place, personId);
		this.setStartDate(startDate);
		this.setDuration(duration);
		this.setAdditionalCosts(additionalCosts);
		this.setAppartmentId(appartmentId);
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

	public int getAppartmentId() {
		return appartmentId;
	}

	public void setAppartmentId(int appartmentId) {
		this.appartmentId = appartmentId;
	}
}
