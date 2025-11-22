package com.kerneldc.searchspecification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;


class EntitySpecificationTest  {

	@Test
	void test1() {
		var f1 = "uuuu-MM-dd HH:mm:ss.SSSZ";
		var dtp1 = DateTimeFormatter.ofPattern(f1);
		var n = OffsetDateTime.of(2025, 11, 21, 15, 43, 13, 0, ZoneOffset.UTC);
		System.out.println(dtp1.format(n));
		assertThat(dtp1.format(n).length(), greaterThan(1));
	}

}
