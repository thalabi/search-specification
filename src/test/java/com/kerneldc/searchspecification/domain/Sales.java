package com.kerneldc.searchspecification.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter

public class Sales extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Setter(AccessLevel.NONE)
	private LocalDateTime transactionDate;
	@Setter(AccessLevel.NONE)
	private String product;
	@Setter(AccessLevel.NONE)
	private Double price;
	@Setter(AccessLevel.NONE)
	private String paymentType;
	@Setter(AccessLevel.NONE)
	private String name;
	private String city;
	private String state;
	private String country;
	private LocalDateTime accountCreated;
	private LocalDateTime lastLogin;
	private Float latitude;
	private Float longitude;	
	private String usZip;
	
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
		setLogicalKeyHolder();
	}

	public void setProduct(String product) {
		this.product = product;
		setLogicalKeyHolder();
	}

	public void setPrice(Double price) {
		this.price = price;
		setLogicalKeyHolder();
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
		setLogicalKeyHolder();
	}

	public void setName(String name) {
		this.name = name;
		setLogicalKeyHolder();
	}

	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(Objects.toString(transactionDate, StringUtils.EMPTY), product, price, paymentType, name);
		setLogicalKeyHolder(logicalKeyHolder);
	}

}
