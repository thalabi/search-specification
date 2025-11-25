package com.kerneldc.searchspecification.repository;

import com.kerneldc.searchspecification.domain.EntityWithInvalidDataType;
import com.kerneldc.searchspecification.enums.IEntityEnum;
import com.kerneldc.searchspecification.enums.TableEnum;

public interface EntityWithInvalidDataTypeRepository extends BaseTableRepository<EntityWithInvalidDataType, Long> {

	@Override
	default IEntityEnum canHandle() {
		return TableEnum.ENTITY_WITH_INVALID_DATA_TYPE;
	}

}
