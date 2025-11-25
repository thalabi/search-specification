package com.kerneldc.searchspecification.repository;

import com.kerneldc.searchspecification.domain.Sales;
import com.kerneldc.searchspecification.enums.IEntityEnum;
import com.kerneldc.searchspecification.enums.TableEnum;

public interface SalesRepository extends BaseTableRepository<Sales, Long> {

	@Override
	default IEntityEnum canHandle() {
		return TableEnum.SALES;
	}

}
