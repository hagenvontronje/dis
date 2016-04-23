package org.dis.sheet02.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "TENANCY_CONTRACT")
public class TenancyContract extends Contract {

	public TenancyContract(int contractNumber, Date date, String place,
			int personId, int appartmentId, Date startDate, int duration,
			double additionalCosts) {
		super(contractNumber, date, place, personId);
		this.startDate = startDate;
		this.duration = duration;
		this.additionalCosts = additionalCosts;
		this.appartmentId = appartmentId;
	}

	public TenancyContract() {

	}

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "DURATION")
	private int duration;

	@Column(name = "ADDITIONAL_COSTS")
	private double additionalCosts;

	@Column(name = "APPARTMENT_ID")
	private int appartmentId;
}
