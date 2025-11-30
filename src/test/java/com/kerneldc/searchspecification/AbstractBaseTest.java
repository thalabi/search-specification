package com.kerneldc.searchspecification;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.TestInfo;

public abstract class AbstractBaseTest {

	private static final String NEW_LINE = System.getProperty("line.separator");
	
	protected static void printTestName(TestInfo testInfo) {
		System.out.println(StringUtils.repeat("=", 80) + NEW_LINE + testInfo.getDisplayName() + NEW_LINE + StringUtils.repeat("=", 80));
	}

	private static String toRegex(String slf4jPattern) {
	    // Escape regex meta characters except braces
	    String escaped = slf4jPattern.replaceAll("([.\\\\+*?\\[^$()|])", "\\\\$1");

	    // Replace SLF4J placeholder {} with regex wildcard
	    return escaped.replace("{}", ".*?");
	}

	protected static Matcher<String> containsRegex(String slf4jPattern) {
		
		var regex = toRegex(slf4jPattern);
		
	    return new TypeSafeMatcher<>() {
	        private final Pattern pattern = Pattern.compile(regex);

	        @Override
	        protected boolean matchesSafely(String s) {
	            return pattern.matcher(s).find();
	        }

	        @Override
	        public void describeTo(Description description) {
	            description.appendText("a string containing regex: ").appendText(regex);
	        }
	    };
	}
}
