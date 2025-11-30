package com.kerneldc.searchspecification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.jpa.domain.Specification;

import com.kerneldc.searchspecification.domain.JpaEntity;
import com.kerneldc.searchspecification.enums.QueryOperatorEnum;
import com.kerneldc.searchspecification.exception.ApplicationException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntitySpecification<T extends JpaEntity> implements Specification<T> {

	private static final long serialVersionUID = 1L;
	protected static final String DATE_FORMAT = "yyyy-MM-dd";
	protected static final String INVALID_FIELD_NAME_MESSAGE = "Field name [{}] not found.";
	protected static final String INVALID_DATA_TYPE_MESSAGE = "Field data type [{}], not supported.";
	protected static final String INVALID_OPERATOR_FOR_DATATYPE = "Invalid operator [{}] for [{}] datatype.";
	protected static final String INVALID_DATE_FORMAT = "Invalid date format [{}].";
	record Filter(String field, QueryOperatorEnum operator, String value) {}
	private transient List<Filter> filterList = new ArrayList<>();
	private transient Class<T> entityClass;

	public EntitySpecification(Class<T> entity, String searchCriteria) {
		if (StringUtils.isEmpty(searchCriteria)) {
			return;
		}
		this.entityClass = entity;
		filterList = Arrays.stream(searchCriteria.split(","))
			    .map(criterion -> criterion.split("\\|"))
			    .filter(parts -> parts.length == 3)
//			    .peek(parts -> LOGGER.info("criterionParts: {}", Arrays.asList(parts)))
			    .map(parts -> new Filter(
			            parts[0],
			            QueryOperatorEnum.fromName(parts[1]),
			            parts[2]
			        ))
			    .toList();
	}

	@Override
	public Predicate toPredicate(Root<T> entity, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var spec = buildSpecificationFromFilters();
	    return spec == null ? null : spec.toPredicate(entity, query, criteriaBuilder);
	}

	private Specification<T> buildSpecificationFromFilters() {
		if (filterList.isEmpty()) {
			return null;
		}
		Specification<T> specification;
		try {
			specification = createSpecification(filterList.get(0));
			for (int i = 1; i < filterList.size(); i++) {
				specification = specification.and(createSpecification(filterList.get(i)));
			}
			return specification;
		} catch (ApplicationException e) {
			LOGGER.error("Due to exception(s), returning null Specification.");
			return null;
		}
	}

	private Specification<T> createSpecification(Filter inputFilter) throws ApplicationException {
		var field = inputFilter.field();
		var value = inputFilter.value();
		
		var fieldType = StringUtils.EMPTY;
		try {
			fieldType = entityClass.getDeclaredField(field).getType().getSimpleName();
		} catch (NoSuchFieldException e) {
			var exceptionMessage = MessageFormatter.format(INVALID_FIELD_NAME_MESSAGE, field).getMessage();
			LOGGER.error(exceptionMessage);
			throw new ApplicationException(exceptionMessage);
		}
		LOGGER.info("inputFilter: {}, fieldType: {}", inputFilter, fieldType);

		switch (fieldType) {
		case "String" -> {
			return handleStringFieldType(inputFilter, field, value);
		}
		case "Short", "Integer", "Double", "Float", "BigDecimal" -> {
			return handleNumberFieldType(inputFilter, field, value);
		}
		case "Date" -> {
			return handleDateFieldType(inputFilter, field, value);
		}
		case "LocalDateTime" -> {
			return handleLocalDateTimeFieldType(inputFilter, field, value);
		}
		default -> {
			var exceptionMessage = MessageFormatter.format(INVALID_DATA_TYPE_MESSAGE, field).getMessage();
			LOGGER.error(exceptionMessage);
			throw new ApplicationException(exceptionMessage);
		}
		}
	}

	private Specification<T> handleStringFieldType(Filter inputFilter, String field, String value) throws ApplicationException {
		switch (inputFilter.operator()) {
		case EQUALS -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(entity.get(field)), value.toLowerCase());
		}
		case NOT_EQUALS -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.notEqual(criteriaBuilder.lower(entity.get(field)), value.toLowerCase());
		}
		case STARTS_WITH -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(entity.get(field)), value.toLowerCase() + "%");
		}
		case CONTAINS -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(entity.get(field)), "%" + value.toLowerCase() + "%");
		}
		case NOT_CONTAINS -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.not(criteriaBuilder.like(criteriaBuilder.lower(entity.get(field)), "%" + value.toLowerCase() + "%"));
		}
		case ENDS_WITH -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(entity.get(field)), "%" + value.toLowerCase());
		}
		default -> {
			var exceptionMessage = MessageFormatter
					.format(INVALID_OPERATOR_FOR_DATATYPE, inputFilter.operator(), "String").getMessage();
			LOGGER.error(exceptionMessage);
			throw new ApplicationException(exceptionMessage);
		}
		}
	}
	

	private Specification<T> handleNumberFieldType(Filter inputFilter, String field, String value) throws ApplicationException {
		switch (inputFilter.operator()) {
		case EQUALS -> {
				return (entity, _, criteriaBuilder) -> criteriaBuilder.equal(entity.get(field), value);
		}
		case NOT_EQUALS -> {
				return (entity, _, criteriaBuilder) -> criteriaBuilder.notEqual(entity.get(field), value);
		}
		case GREATER_THAN, GT -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.greaterThan(entity.get(field), value);
		}
		case GREATER_THAN_OR_EQUAL_TO, GTE -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(entity.get(field), value);
		}
		case LESS_THAN, LT -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.lessThan(entity.get(field), value);
		}
		case LESS_THAN_OR_EQUAL_TO, LTE -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(entity.get(field), value);
		}
		default -> {
			var exceptionMessage = MessageFormatter
					.format(INVALID_OPERATOR_FOR_DATATYPE, inputFilter.operator(), "number").getMessage();
			LOGGER.error(exceptionMessage);
			throw new ApplicationException(exceptionMessage);
		}
		}
	}



	private Specification<T> handleDateFieldType(Filter inputFilter, String field, String value)
			throws ApplicationException {
		Date date;
		try {
			// avoid using SimpleDateFormat use DateTimeFormatter instead
			LocalDate ld = LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT));
			date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (DateTimeParseException e) {
			var exceptionMessage = MessageFormatter.format(INVALID_DATE_FORMAT, inputFilter.value()).getMessage();
			LOGGER.error(exceptionMessage);
			throw new ApplicationException(exceptionMessage);
		}

		switch (inputFilter.operator()) {
		case DATE_IS -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.equal(entity.get(field), date);
		}
		case DATE_IS_NOT -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.notEqual(entity.get(field), date);
		}
		case DATE_BEFORE -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.lessThan(entity.get(field), date);
		}
		case DATE_AFTER -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.greaterThan(entity.get(field), date);
		}
		default -> {
			var exceptionMessage = MessageFormatter
					.format(INVALID_OPERATOR_FOR_DATATYPE, inputFilter.operator(), "Date").getMessage();
			LOGGER.error(exceptionMessage);
			throw new ApplicationException(exceptionMessage);
		}
		}
	}


	private Specification<T> handleLocalDateTimeFieldType(Filter inputFilter, String field, String value) throws ApplicationException {
		LocalDateTime localDateTimeValue;
		try {
			localDateTimeValue = LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
		} catch (DateTimeParseException e) {
			var exceptionMessage = MessageFormatter.format(INVALID_DATE_FORMAT, inputFilter.value()).getMessage();
			LOGGER.error(exceptionMessage);
			throw new ApplicationException(exceptionMessage);			
		}
		switch (inputFilter.operator()) {
		case DATE_IS -> {
				return (entity, _, criteriaBuilder) -> criteriaBuilder.equal(entity.get(field), localDateTimeValue);
		}
		case DATE_IS_NOT -> {
				return (entity, _, criteriaBuilder) -> criteriaBuilder.notEqual(entity.get(field), localDateTimeValue);
		}
		case DATE_BEFORE -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.lessThan(entity.get(field), localDateTimeValue);
		}
		case DATE_AFTER -> {
			return (entity, _, criteriaBuilder) -> criteriaBuilder.greaterThan(entity.get(field), localDateTimeValue);
		}
		default -> {
			var exceptionMessage = MessageFormatter
					.format(INVALID_OPERATOR_FOR_DATATYPE, inputFilter.operator(), "LocalDateTime").getMessage();
			LOGGER.error(exceptionMessage);
			throw new ApplicationException(exceptionMessage);
			}
		}
	}
}
