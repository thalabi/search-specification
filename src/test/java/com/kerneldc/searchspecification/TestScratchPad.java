package com.kerneldc.searchspecification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

class TestScratchPad extends AbstractBaseTest {

	@Disabled
	@Test
	void testInvalidFieldName(TestInfo testInfo) {
		printTestName(testInfo);

		LocalDate d = LocalDate.parse("2025-11-26", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Date legacy = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());

		System.out.println(legacy);
		assertThat(legacy, notNullValue());
	}

}
