package com.kerneldc.searchspecification.repository;

import com.kerneldc.searchspecification.domain.TestEntity;
import com.kerneldc.searchspecification.enums.IEntityEnum;
import com.kerneldc.searchspecification.enums.TableEnum;

public interface TestEntityRepository extends BaseTableRepository<TestEntity, Long> {

	@Override
	default IEntityEnum canHandle() {
		return TableEnum.TEST_ENTITY;
	}

}
