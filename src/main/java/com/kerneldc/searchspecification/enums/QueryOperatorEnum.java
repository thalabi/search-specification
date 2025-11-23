package com.kerneldc.searchspecification.enums;

public enum QueryOperatorEnum {

	EQUALS("equals"), NOT_EQUALS("notEquals"),
	GREATER_THAN("greaterThan"), GT("gt"),
	GREATER_THAN_OR_EQUAL_TO("greaterThanOrEqualTo"),  GTE("gte"),
	LESS_THAN("lessThan"), LT("lt"),
	LESS_THAN_OR_EQUAL_TO("lessThanOrEqualTo"), LTE("lte"),
	STARTS_WITH("startsWith"), CONTAINS("contains"), NOT_CONTAINS("notContains"), ENDS_WITH("endsWith"),
	DATE_IS("dateIs"), DATE_IS_NOT("dateIsNot"), DATE_BEFORE("dateBefore"), DATE_AFTER("dateAfter");

	private String operator;
	public String getOperator() {
		return operator;
	}
	QueryOperatorEnum(String operator) {
		this.operator = operator;
	}
	public static QueryOperatorEnum fromName(String name) {
		for (QueryOperatorEnum e : QueryOperatorEnum.values()) {
			if (e.getOperator().equalsIgnoreCase(name)) {
				return e;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid operator [%s]", name));
	}

}
