package com.kerneldc.searchspecification.repository;

import java.math.BigDecimal;
import java.util.List;

import com.kerneldc.searchspecification.domain.SunshineList;
import com.kerneldc.searchspecification.enums.IEntityEnum;
import com.kerneldc.searchspecification.enums.UploadTableEnum;

public interface SunshineListRepository extends BaseTableRepository<SunshineList, Long> {

	List<SunshineList> findBySalary(BigDecimal salary);
	List<SunshineList> findByBenefits(BigDecimal benefits);
	
	@Override
	default IEntityEnum canHandle() {
		return UploadTableEnum.SUNSHINE_LIST;
	}

}
