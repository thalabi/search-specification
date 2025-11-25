package com.kerneldc.searchspecification.enums;

import com.kerneldc.searchspecification.domain.AbstractEntity;

public interface IEntityEnum {

	Class<? extends AbstractEntity> getEntity();
	boolean isImmutable();
	String[] getWriteColumnOrder();
}