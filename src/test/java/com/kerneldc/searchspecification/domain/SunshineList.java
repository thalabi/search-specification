package com.kerneldc.searchspecification.domain;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter

public class SunshineList extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	private String sector;
	
	@Setter(AccessLevel.NONE)
	private String lastName;
	
	@Setter(AccessLevel.NONE)
	private String firstName;
	
	private BigDecimal salary;
	
	private BigDecimal benefits;
	
	private String employer;
	
	private String jobTitle;
	
	private Short calendarYear;
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
		setLogicalKeyHolder();
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		setLogicalKeyHolder();
	}
	
	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(lastName, firstName, UUID.randomUUID().toString());
		setLogicalKeyHolder(logicalKeyHolder);
	}
}
