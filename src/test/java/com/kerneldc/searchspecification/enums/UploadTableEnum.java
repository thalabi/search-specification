package com.kerneldc.searchspecification.enums;

import java.util.Arrays;

import com.google.common.base.Enums;
import com.kerneldc.searchspecification.domain.AbstractPersistableEntity;
import com.kerneldc.searchspecification.domain.Sales;
import com.kerneldc.searchspecification.domain.SunshineList;

public enum UploadTableEnum implements IEntityEnum {
	SALES(Sales.class),
	SUNSHINE_LIST(SunshineList.class),
	;
	
	Class<? extends AbstractPersistableEntity> entity;
	String[] writeColumnOrder;
	
	UploadTableEnum(Class<? extends AbstractPersistableEntity> entity) {
		this.entity = entity;
	}
	UploadTableEnum(Class<? extends AbstractPersistableEntity> entity, String[] writeColumnOrder) {
		this.entity = entity;
		// tag SOURCECSVLINENUMBER to the end of the writeColumnOrder
		this.writeColumnOrder = Arrays.copyOf(writeColumnOrder, writeColumnOrder.length+1);
		this.writeColumnOrder[this.writeColumnOrder.length-1] = "SOURCECSVLINENUMBER";  
	}
	
	@Override
	public Class<? extends AbstractPersistableEntity> getEntity() {
		return entity;
	}

	@Override
	public boolean isImmutable() {
		return false;
	}
	
	@Override
	public String[] getWriteColumnOrder() {
		return writeColumnOrder;
	}
	
//	@Override
	public static IEntityEnum valueIfPresent(String name) {
	    return Enums.getIfPresent(UploadTableEnum.class, name).orNull();
	}
}
