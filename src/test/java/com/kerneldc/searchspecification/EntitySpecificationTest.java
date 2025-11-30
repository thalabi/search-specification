package com.kerneldc.searchspecification;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.kerneldc.searchspecification.domain.Sales;
import com.kerneldc.searchspecification.domain.SunshineList;
import com.kerneldc.searchspecification.domain.TestEntity;
import com.kerneldc.searchspecification.enums.TableEnum;
import com.kerneldc.searchspecification.repository.SalesRepository;
import com.kerneldc.searchspecification.repository.SunshineListRepository;
import com.kerneldc.searchspecification.repository.TestEntityRepository;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(OutputCaptureExtension.class)
class EntitySpecificationTest extends AbstractBaseTest {

	@Autowired
	private SalesRepository salesRepository;
	@Autowired
	private SunshineListRepository sunshineListRepository;
	@Autowired
	private TestEntityRepository testEntityRepository;

	@Test
	void testStringFieldEquals(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "product|equals|product1");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getProduct(), is("product1"));
	}
	
	@Test
	void testStringFieldNotEquals(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "product|notequals|product1");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getProduct(), is("product2"));
	}
	
	@Test
	void testStringFieldStartsWith(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "product|startsWith|p");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
	}
	
	@Test
	void testStringFieldContains(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "product|contains|duct");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
	}
	
	@Test
	void testStringFieldNotContains(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "product|notcontains|abc");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
	}
	
	@Test
	void testStringFieldEndWith(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "product|endsWith|1");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getProduct(), is("product1"));
	}

	@Test
	void testStringFieldWrongOperator(TestInfo testInfo, CapturedOutput logOutput) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "product|DATeIS|1");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
		assertThat(logOutput.getOut(), containsRegex(EntitySpecification.INVALID_OPERATOR_FOR_DATATYPE));
	}


	@Test
	void testNumberFieldEquals(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "price|equals|100.01");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getPrice(), is(100.01d));
	}
	
	@Test
	void testNumberFieldNotEquals(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "price|notequals|100.01");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getPrice(), is(200.0d));
	}
	
	@Test
	void testNumberFieldGreaterThan(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "price|greaterThan|100.01");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getPrice(), is(greaterThan(100.01d)));
	}
	
	@Test
	void testNumberFieldGreaterThanOrEqual(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var entitySpec = new EntitySpecification<>(Sales.class, "price|greaterThanOrEqualTo|100");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
		assertThat(salesList.get(0).getPrice(), is(greaterThanOrEqualTo(100d)));
		assertThat(salesList.get(1).getPrice(), is(greaterThanOrEqualTo(100d)));
	}
//	
//	@Test
//	void testLatitudeEquals(TestInfo testInfo) {
//		printTestName(testInfo);
//		var entitySpec = new EntitySpecification<>(Sales.class, "latitude|equals|45.5019");
//		salesRepository.saveAll(List.of(createSales1(), createSales2()));
//		
//		var salesList = salesRepository.findAll(entitySpec);
//		assertThat(salesList.size(), is(1));
//		assertThat(salesList.get(0).getLatitude(), is(45.5019f));
//	}
	
	@Test
	void testNumberFieldLessThan(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));

		var entitySpec = new EntitySpecification<>(Sales.class, "latitude|lt|45.5019");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getLatitude(), is(43.6532f));
	}
	
	@Test
	void testNumberFieldLessThanOrEqual(TestInfo testInfo) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));

		var entitySpec = new EntitySpecification<>(Sales.class, "latitude|lessthanorequalto|46.0001");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
	}
	
	@Test
	void testNumberFieldWrongOperator(TestInfo testInfo, CapturedOutput logOutput) {
		printTestName(testInfo);
		salesRepository.saveAll(List.of(createSales1(), createSales2()));

		var entitySpec = new EntitySpecification<>(Sales.class, "latitude|contains|46.0001");
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
		assertThat(logOutput.getOut(), containsRegex(EntitySpecification.INVALID_OPERATOR_FOR_DATATYPE));
	}
	
	@Test
	void testDateFieldDateIs(TestInfo testInfo) {
		printTestName(testInfo);
		TableEnum table = TableEnum.TEST_ENTITY;
		
		var entityClass = table.getEntity();
		var entitySpec = new EntitySpecification<>(entityClass, "dateField|dateIs|2025-11-26");
		testEntityRepository.saveAll(List.of(testEntity1(), testEntity2()));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		var testEntityList = testEntityRepository.findAll((Specification)entitySpec);
		assertThat(testEntityList.size(), is(1));
	}
	
	@Test
	void testDateFieldDateIsNot(TestInfo testInfo) {
		printTestName(testInfo);
		TableEnum table = TableEnum.TEST_ENTITY;
		
		var entityClass = table.getEntity();
		var entitySpec = new EntitySpecification<>(entityClass, "dateField|dateIsNot|9999-01-01");
		testEntityRepository.saveAll(List.of(testEntity1(), testEntity2()));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		var testEntityList = testEntityRepository.findAll((Specification)entitySpec);
		assertThat(testEntityList.size(), is(2));
	}

	@Test
	void testDateFieldDateBefore(TestInfo testInfo) {
		printTestName(testInfo);
		TableEnum table = TableEnum.TEST_ENTITY;
		
		var entityClass = table.getEntity();
		var entitySpec = new EntitySpecification<>(entityClass, "dateField|dateBefore|2025-11-27");
		testEntityRepository.saveAll(List.of(testEntity1(), testEntity2()));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		var testEntityList = testEntityRepository.findAll((Specification)entitySpec);
		assertThat(testEntityList.size(), is(1));
	}

	@Test
	void testDateFieldDateAfter(TestInfo testInfo) {
		printTestName(testInfo);
		TableEnum table = TableEnum.TEST_ENTITY;
		
		var entityClass = table.getEntity();
		var entitySpec = new EntitySpecification<>(entityClass, "dateField|dateAfter|2025-11-27");
		testEntityRepository.saveAll(List.of(testEntity1(), testEntity2()));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		var testEntityList = testEntityRepository.findAll((Specification)entitySpec);
		assertThat(testEntityList.size(), is(1));
	}

	@Test
	void testDateFieldWrongOperator(TestInfo testInfo, CapturedOutput logOutput) {
		printTestName(testInfo);
		TableEnum table = TableEnum.TEST_ENTITY;
		
		var entityClass = table.getEntity();
		var entitySpec = new EntitySpecification<>(entityClass, "dateField|equals|2025-11-27");
		testEntityRepository.saveAll(List.of(testEntity1(), testEntity2()));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		var testEntityList = testEntityRepository.findAll((Specification)entitySpec);
		assertThat(testEntityList.size(), is(2));
		assertThat(logOutput.getOut(), containsRegex(EntitySpecification.INVALID_OPERATOR_FOR_DATATYPE));
	}

	@Test
	void testDateFieldInvalidDateFormat(TestInfo testInfo, CapturedOutput logOutput) {
		printTestName(testInfo);
		TableEnum table = TableEnum.TEST_ENTITY;
		
		var entityClass = table.getEntity();
		var entitySpec = new EntitySpecification<>(entityClass, "dateField|dateIs|20251127");
		testEntityRepository.saveAll(List.of(testEntity1(), testEntity2()));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		var testEntityList = testEntityRepository.findAll((Specification)entitySpec);
		assertThat(testEntityList.size(), is(2));
		assertThat(logOutput.getOut(), containsRegex(EntitySpecification.INVALID_DATE_FORMAT));
	}


	
	@Test
	void testLocalDateTimeFieldDateIs(TestInfo testInfo) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(Sales.class, "transactionDate|dateIs|2024-02-16T11:04:29.000Z");
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getTransactionDate(), is(LocalDateTime.of(2024,  02, 16, 11, 04, 29)));
	}
	
	@Test
	void testLocalDateTimeFieldDateIsNot(TestInfo testInfo) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(Sales.class, "transactionDate|dateIsNot|2024-02-16T11:04:29.000Z");
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getTransactionDate(), not(LocalDateTime.of(2024,  02, 16, 11, 04, 29)));
	}
	
	@Test
	void testLocalDateTimeFieldDateBefore(TestInfo testInfo) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(Sales.class, "transactionDate|dateBefore|2024-02-21T00:00:00.000Z");
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getTransactionDate(), is(LocalDateTime.of(2024,  02, 16, 11, 04, 29)));
	}

	@Test
	void testLocalDateTimeFieldDatecAfter(TestInfo testInfo) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(Sales.class, "transactionDate|dateAfter|2024-02-16T11:04:29.000Z");
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(1));
		assertThat(salesList.get(0).getTransactionDate(), is(LocalDateTime.of(2024,  02, 21, 12, 39, 57)));
	}

	@Test
	void testLocalDateTimeFieldWrongOperator(TestInfo testInfo, CapturedOutput logOutput) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(Sales.class, "transactionDate|equals|2024-02-16T11:04:29.000Z");
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
		assertThat(logOutput.getOut(), containsRegex(EntitySpecification.INVALID_OPERATOR_FOR_DATATYPE));
	}
	
	@Test
	void testLocalDateTimeFieldInvalidDateFormat(TestInfo testInfo, CapturedOutput logOutput) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(Sales.class, "transactionDate|equals|20240216T110429000Z");
		salesRepository.saveAll(List.of(createSales1(), createSales2()));
		
		var salesList = salesRepository.findAll(entitySpec);
		assertThat(salesList.size(), is(2));
		assertThat(logOutput.getOut(), containsRegex(EntitySpecification.INVALID_DATE_FORMAT));
	}
	
	@Test
	void testSalaryEquals(TestInfo testInfo) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(SunshineList.class, "salary|equals|100001.99");
		sunshineListRepository.saveAll(List.of(createSunshineList1(), createSunshineList2()));
		
		var sunshineListList = sunshineListRepository.findAll(entitySpec);
		assertThat(sunshineListList.size(), is(1));
		assertThat(sunshineListList.get(0).getSalary(), is(new BigDecimal("100001.99")));
	}

	
	@Test
	void testFirstNameAndLastName(TestInfo testInfo) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(SunshineList.class, "firstName|equals|May,lastName|equals|Family");
		sunshineListRepository.saveAll(List.of(createSunshineList1(), createSunshineList2()));
		
		var sunshineListList = sunshineListRepository.findAll(entitySpec);
		assertThat(sunshineListList.size(), is(1));
	}

	
	@Test
	void testEmptySeartchCriteria(TestInfo testInfo) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(SunshineList.class, StringUtils.EMPTY);
		sunshineListRepository.saveAll(List.of(createSunshineList1(), createSunshineList2()));
		
		var sunshineListList = sunshineListRepository.findAll(entitySpec);
		assertThat(sunshineListList.size(), is(2));
	}
	
	@Test
	void testEmptySeartchCriteriaValue(TestInfo testInfo) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(SunshineList.class, "firstName|equals|");
		sunshineListRepository.saveAll(List.of(createSunshineList1(), createSunshineList2()));
		
		var sunshineListList = sunshineListRepository.findAll(entitySpec);
		assertThat(sunshineListList.size(), is(2));
	}


	@Test
	void testInvalidFieldType(TestInfo testInfo, CapturedOutput logOutput) {
		printTestName(testInfo);
		var entitySpec = new EntitySpecification<>(TestEntity.class, "unhandledDataTypeField|equals|1");
		testEntityRepository.saveAll(List.of(testEntity1(), testEntity2()));

		var testEntityList = testEntityRepository.findAll(entitySpec);
		assertThat(testEntityList.size(), is(2));
		assertThat(logOutput.getOut(), containsRegex(EntitySpecification.INVALID_DATA_TYPE_MESSAGE));
	}
	@Test
	void testInvalidFieldName(TestInfo testInfo, CapturedOutput logOutput) {
		printTestName(testInfo);
		TableEnum table = TableEnum.TEST_ENTITY;
		
		var entityClass = table.getEntity();
		var entitySpec = new EntitySpecification<>(entityClass, "invalidField|equals|xyz");
		testEntityRepository.saveAll(List.of(testEntity1(), testEntity2()));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		var testEntityList = testEntityRepository.findAll((Specification)entitySpec);
		assertThat(testEntityList.size(), is(2));
		assertThat(logOutput.getOut(), containsRegex(EntitySpecification.INVALID_FIELD_NAME_MESSAGE));
	}

	private Sales createSales1() {
		var s1 = new Sales();
		s1.setProduct("product1");
		s1.setPrice(100.01d);
		s1.setLatitude(43.6532f);
		s1.setTransactionDate(LocalDateTime.of(2024,  02, 16, 11, 04, 29));
		return s1;
	}
	private Sales createSales2() {
		var s1 = new Sales();
		s1.setProduct("product2");
		s1.setPrice(200d);
		s1.setLatitude(45.5019f);
		s1.setTransactionDate(LocalDateTime.of(2024,  02, 21, 12, 39, 57));
		return s1;
	}
	private SunshineList createSunshineList1() {
		var sl = new SunshineList();
		sl.setSector("Edu");
		sl.setLastName("Family");
		sl.setFirstName("May");
		sl.setSalary(new BigDecimal("100001.99"));
		sl.setBenefits(BigDecimal.ZERO);
		sl.setEmployer("TDSB");
		sl.setJobTitle("Principal");
		sl.setCalendarYear((short) 2024);
		return sl;
	}

	private SunshineList createSunshineList2() {
		var sl = new SunshineList();
		sl.setSector("IT");
		sl.setLastName("Family");
		sl.setFirstName("Hamza");
		sl.setSalary(new BigDecimal("77000"));
		sl.setBenefits(BigDecimal.ZERO);
		sl.setEmployer("MIT");
		sl.setJobTitle("Accountant");
		sl.setCalendarYear((short) 2023);
		return sl;
	}

	private TestEntity testEntity1() {
		var te1 = new TestEntity();
		te1.setNaturalKey("someString");
		try {
			te1.setDateField(new SimpleDateFormat(EntitySpecification.DATE_FORMAT).parse("2025-11-26"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		te1.setUnhandledDataTypeField((byte) 1);
		return te1;
	}
	private TestEntity testEntity2() {
		var te1 = new TestEntity();
		te1.setNaturalKey("someString two");
		try {
			te1.setDateField(new SimpleDateFormat(EntitySpecification.DATE_FORMAT).parse("2025-11-29"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		te1.setUnhandledDataTypeField((byte) 1);
		return te1;
	}
}
