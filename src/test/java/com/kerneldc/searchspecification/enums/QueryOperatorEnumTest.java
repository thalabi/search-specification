package com.kerneldc.searchspecification.enums;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.kerneldc.searchspecification.AbstractBaseTest;

class QueryOperatorEnumTest extends AbstractBaseTest {

	@Test
	void testFromNameFails(TestInfo testInfo) {
		printTestName(testInfo);
		assertThrows(IllegalArgumentException.class, () -> QueryOperatorEnum.fromName("invalid-name"));
	}
	
}
