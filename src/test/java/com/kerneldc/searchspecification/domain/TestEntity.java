package com.kerneldc.searchspecification.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class TestEntity extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Setter(AccessLevel.NONE)
	private String naturalKey;
	
	private Date dateField;
	
	private Byte unhandledDataTypeField;


	public void setNaturalKey(String naturalKey) {
		this.naturalKey = naturalKey;
		setLogicalKeyHolder();
	}

	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(naturalKey);
		setLogicalKeyHolder(logicalKeyHolder);
	}
}
